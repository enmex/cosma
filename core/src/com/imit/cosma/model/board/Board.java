package com.imit.cosma.model.board;

import static com.imit.cosma.config.Config.getInstance;

import com.imit.cosma.ai.AI;
import com.imit.cosma.model.board.content.DamageKit;
import com.imit.cosma.model.board.content.HealthKit;
import com.imit.cosma.model.board.content.Space;
import com.imit.cosma.model.board.content.SupplyKit;
import com.imit.cosma.model.board.state.BlackHoleSpawnState;
import com.imit.cosma.model.board.state.BoardState;
import com.imit.cosma.model.board.state.IdleBoardState;
import com.imit.cosma.model.board.state.ShipAttackingOneTargetBoardState;
import com.imit.cosma.model.board.state.ShipMovingBoardState;
import com.imit.cosma.model.board.weather.SpaceDebris;
import com.imit.cosma.model.board.weather.SpaceWeather;
import com.imit.cosma.model.rules.Attack;
import com.imit.cosma.model.rules.side.Enemy;
import com.imit.cosma.model.rules.side.NeutralSide;
import com.imit.cosma.model.rules.side.Player;
import com.imit.cosma.model.rules.side.Side;
import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.model.spaceship.ShipRandomizer;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.model.spaceship.SpaceshipBuilder;
import com.imit.cosma.pkg.random.Randomizer;
import com.imit.cosma.util.Cloneable;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Board implements Cloneable {
    private Set<Point> emptySet;

    private Cell[][] cells;
    private Cell selected;
    private Cell interacted;
    private Set<Point> interactedCells;
    private Set<Point> emptyCells;

    private int turnCount;

    private Side turn;

    private Point selectedPoint;

    private int playerAdvantagePoints, enemyAdvantagePoints;

    private AI enemy;
    private Path currentPath;

    private Set<Point> availableForMove, availableForAttack;

    private Side playerSide, enemySide, otherSide;

    public Board() {
        cells = new Cell[getInstance().BOARD_SIZE][getInstance().BOARD_SIZE];
        emptySet = new HashSet<>();
        emptyCells = new HashSet<>();
        selected = new Cell(new Space());
        interacted = new Cell(new Space());
        playerAdvantagePoints = 0;
        enemyAdvantagePoints = 0;

        SpaceshipBuilder spaceshipBuilder = new SpaceshipBuilder();

        playerSide = new Player(8);
        enemySide = new Enemy(8);
        otherSide = new NeutralSide();

        /*
        for(int y = 0; y < getInstance().BOARD_SIZE; y++) {
            for(int x = 0; x < getInstance().BOARD_SIZE; x++) {
                cells[y][x] = new Cell();
            }
        }

         */
        //initialise player ships
        for (int y = 0; y < getInstance().SPACESHIP_ROWS; y++) {
            for (int x = 0; x < getInstance().BOARD_SIZE; x++) {
                Spaceship spaceship = spaceshipBuilder.setSide(playerSide)
                        .addSkeleton()
                        .addWeapon(ShipRandomizer.getRandomAmount())
                        .setMovingStyle().build();
                cells[y][x] = new Cell(spaceship);


            }
        }

        //initialise space cells
        for (int y = getInstance().SPACESHIP_ROWS; y < getInstance().BOARD_SIZE; y++) {
            for (int x = 0; x < getInstance().BOARD_SIZE; x++) {
                cells[y][x] = new Cell();
                emptyCells.add(new Point(x, y));
            }
        }

        //initialise enemy ships
        for (int y = getInstance().BOARD_SIZE - getInstance().SPACESHIP_ROWS; y < getInstance().BOARD_SIZE; y++) {
            for (int x = 0; x < getInstance().BOARD_SIZE; x++) {
                Spaceship spaceship = spaceshipBuilder.setSide(enemySide)
                        .addSkeleton()
                        .addWeapon(ShipRandomizer.getRandomAmount())
                        .setMovingStyle().build();
                cells[y][x] = new Cell(spaceship);
            }
        }
        /*
        for (int y = 0; y < getInstance().BOARD_SIZE; y++) {
            for (int x = 0; x < getInstance().BOARD_SIZE; x++) {
                cells[y][x] = new Cell();
            }
        }


        cells[0][0] = new Cell(spaceshipBuilder.setSide(playerSide)
                .addSkeleton(Skeleton.DESTROYER)
                .addWeapon(Weapon.ION_CANNON)
                .setMovingStyle(new WeakRookMovingStyle()).build());

        cells[7][0] = new Cell(spaceshipBuilder.setSide(enemySide)
                .addSkeleton(Skeleton.DESTROYER)
                .addWeapon(Weapon.ION_CANNON)
                .setMovingStyle(new WeakRookMovingStyle()).build());

         */

        turn = playerSide;
        interactedCells = new HashSet<>();

        availableForMove = new HashSet<>();
        availableForAttack = new HashSet<>();
    }

    public void initAI(){
        enemy = new AI(clone());
    }

    public BoardState getCurrentState(Point selected) {
        return getCurrentState(selected.x, selected.y);
    }
    private BoardState getCurrentState(int selectedX, int selectedY){
        if(inBoard(selectedX, selectedY)){
            if (turn.isPlayer()) {
                return calculateCurrentPlayerState(selectedX, selectedY);
            } else if (turn instanceof Enemy) {
                return calculateCurrentEnemyState();
            } else {
                return calculateCurrentOtherState();
            }
        }
        return new IdleBoardState();
    }

    public BoardState calculateCurrentPlayerState(Point selectedPoint){
        currentPath = new Path(this.selectedPoint, selectedPoint);

        if(selected.isShip() && selected.getStepMode() != StepMode.COMPLETED && selected.getSide() == turn) {
            if (selectedCanMoveTo(selectedPoint)) {
                setSelectedPosition(selectedPoint);

                turn.updateTurns();
                enemy.savePlayerTurn(currentPath, StepMode.MOVE);

                return new ShipMovingBoardState(turn.isPlayer() ? getCell(selectedPoint) : selected);
            } else if (selectedCanFireTo(selectedPoint)) {
                damageShip(selectedPoint, selected.getDamageAmount());

                turn.updateTurns();
                enemy.savePlayerTurn(currentPath, StepMode.ATTACK);
                return new ShipAttackingOneTargetBoardState(selected, interacted);
            }
        }

        return new IdleBoardState();
    }

    public Path getCurrentPath() {
        return currentPath;
    }

    public BoardState calculateCurrentEnemyState(){
        enemy.update(clone());

        currentPath = enemy.getPath();
        Point source = currentPath.getSource();
        Point target = currentPath.getTarget();

        setSelectedEnemyTurn(source);

        if (isShip(target)) {
            selected.setStepMode(StepMode.ATTACK);
        }

        if (selected.isShip() && selected.getStepMode() != StepMode.COMPLETED && selected.getSide().equals(turn)) {
            if (selectedCanMoveTo(target.x, target.y)) {
                setSelectedPosition(target.x, target.y);
                turn.updateTurns();

                setSelectedEnemyTurn(target);

                return new ShipMovingBoardState(selected);
            } else if (selectedCanFireTo(target.x, target.y)) {
                damageShip(target.x, target.y, selected.getDamageAmount());
                turn.updateTurns();

                return new ShipAttackingOneTargetBoardState(selected, interacted);
            }
        }
        return new IdleBoardState();
    }

    public void updateSide(){
        if(sideCompletedTurn()){
            changeTurn();

            for(Point point : interactedCells){
                cells[point.y][point.x].setStepMode(StepMode.MOVE);
            }
            interactedCells.clear();
        }
    }

    public boolean isShip(Point target) {
        return isShip(target.x, target.y);
    }

    public boolean isShip(int x, int y) {
        return cells[y][x].isShip();
    }

    public boolean isShipSelected() {
        return selected.isShip();
    }

    public boolean isEnemyShip(int x, int y){
        return isShip(x, y)
                && (selected.getSide().isPlayer() && !cells[y][x].getSide().isPlayer()
                || !selected.getSide().isPlayer() && cells[y][x].getSide().isPlayer());
    }

    public void damageShip(int shipX, int shipY, int damage){
        interacted.setContent(cells[shipY][shipX].getContent().clone());

        cells[shipY][shipX].setDamage(damage);
        selected.setStepMode(StepMode.COMPLETED);
        interactedCells.add(selectedPoint);

        if(turn.isPlayer()){
            playerAdvantagePoints += Math.min(damage, cells[shipY][shipX].getHealthPoints());
        }
        else{
            enemyAdvantagePoints += Math.min(damage, cells[shipY][shipX].getHealthPoints());
        }

        if(cells[shipY][shipX].getContent().getHealthPoints() <= 0){
            destroyShip(shipX, shipY, Cell.initWithSpace());
        }
    }

    public void damageShip(Point target, int damage) {
        damageShip(target.x, target.y, damage);
    }

    public void healShip(int shipX, int shipY, int healthPoints) {
        interacted.setContent(cells[shipY][shipX].getContent().clone());

        cells[shipY][shipX].addHealthPoints(healthPoints);
        selected.setStepMode(StepMode.COMPLETED);
        interactedCells.add(selectedPoint);
    }

    public void healShip(Point target, int healthPoints) {
        healShip(target.x, target.y, healthPoints);
    }

    private void destroyShip(int shipX, int shipY, Cell replacement){
        cells[shipY][shipX] = replacement;
        if(turn.isPlayer()) {
            enemySide.removeShip();
        }
        else {
            playerSide.removeShip();
        }
    }

    private void destroyShip(Point target, Cell replacement) {
        destroyShip(target.x, target.y, replacement);
    }

    public boolean isPassable(int x, int y) {
        return inBoard(x, y) && cells[y][x].isPassable();
    }

    public Cell getSelected() {
        return selected;
    }

    public boolean selectedCanMoveTo(int x, int y){
        return selected.isShip()
                && cells[y][x] != selected
                && selected.canMoveTo(selectedPoint.x, selectedPoint.y, x, y)
                && selected.getStepMode() == StepMode.MOVE && isPassable(x, y);
    }

    public boolean selectedCanFireTo(int x, int y){
        return selected != null && isShipSelected() && isShip(x, y)
                && selected.getSide() != cells[y][x].getSide() && selected.getStepMode() == StepMode.ATTACK;
    }

    public Cell getCell(int x, int y){
        return cells[y][x];
    }

    public Set<Point> getAvailableCellsForMove() {
        return selected.isShip() && selected.getStepMode() == StepMode.MOVE && selected.getSide() == turn ? availableForMove : emptySet;
    }
    public Set<Point> getAvailableCellsForMove(int x, int y){
        return isShip(x, y) && cells[y][x].getStepMode() == StepMode.MOVE ? cells[y][x].getMoves().getAvailable(this, x, y) : emptySet;
    }

    public Set<Point> getAvailableCellsForFire(){
        return selected.isShip() && selected.getStepMode() == StepMode.ATTACK ? availableForAttack : emptySet;
    }

    public Set<Point> getAvailableCellsForFire(int x, int y){
        return isShip(x, y) && cells[y][x].getStepMode() == StepMode.ATTACK ? Attack.getAvailable(this, x, y) : emptySet;
    }

    private void setSelectedPosition(Point destination) {
        interacted.setContent(cells[destination.y][destination.x].getContent().clone());

        emptyCells.remove(destination);
        emptyCells.add(selectedPoint);

        selected.swapContents(cells[destination.y][destination.x]);
        cells[destination.y][destination.x].setStepMode(StepMode.ATTACK);
        interactedCells.add(destination);
    }

    public Point getAtlasCoords(int x, int y){
        return cells[y][x].getAtlasCoord();
    }

    public boolean inBoard(int x, int y){
        return x >=0 && x < 8 && y >= 0 && y < 8;
    }

    public Point getSelectedPoint() {
        return selectedPoint;
    }

    public Side getTurn(){
        return turn;
    }

    public Side getSide(int x, int y){
        return cells[y][x].getSide();
    }

    public float getDefaultRotation(int x, int y){
        return cells[y][x].getSide().getDefaultRotation();
    }

    public void setSelectedPlayerTurn(Point target) {
        setSelectedPlayerTurn(target.x, target.y);
    }

    public void setSelectedEnemyTurn(Point target) {
        setSelectedEnemyTurn(target.x, target.y);
    }

    public void setSelectedPlayerTurn(int x, int y){
        if(inBoard(x, y) && turn.isPlayer()){
            setSelected(x, y);
        }
    }

    public void setSelectedEnemyTurn(int x, int y) {
        if(inBoard(x, y) && !turn.isPlayer()) {
            setSelected(x, y);
        }
    }

    private void setSelected(int x, int y) {
        selected = cells[y][x];
        selectedPoint.set(x, y);

        if(selected.isShip()) {
            availableForAttack = Attack.getAvailable(this);
            availableForMove = selected.getMoves().getAvailable(this, selectedPoint);
        }
    }

    private void setSelected(Point target) {
        setSelected(target.x, target.y);
    }

    public int getDamagePoints(Point target){
        return getDamagePoints(target.x, target.y);
    }

    public int getDamagePoints(int x, int y){
        return cells[y][x].getDamageAmount();
    }

    public int getMaxHealthPoints(int x, int y){
        return cells[y][x].getMaxHealthPoints();
    }

    public int getMaxHealthPoints(Point target){
        return getMaxHealthPoints(target.x, target.y);
    }

    public int getHealthPoints(Point target){
        return getHealthPoints(target.x, target.y);
    }

    public int getHealthPoints(int x, int y){
        return cells[y][x].getHealthPoints();
    }

    public StepMode doArtificialTurn(Path path){
        StepMode mode = StepMode.COMPLETED;

        Point source = path.getSource();
        Point target = path.getTarget();

        setSelected(source);

        if(selectedCanMoveTo(target.x, target.y)){
            setSelectedPosition(target.x, target.y);

            setSelected(target);
            turn.updateTurns();

            mode = StepMode.MOVE;
        }
        else if(selectedCanFireTo(target.x, target.y)){
            damageShip(target.x, target.y, selected.getDamageAmount());
            turn.updateTurns();

            mode = StepMode.ATTACK;
        }

        return mode;
    }

    public void set(Board board){
        for (int y = 0; y < getInstance().BOARD_SIZE; y++) {
            for (int x = 0; x < getInstance().BOARD_SIZE; x++) {
                this.cells[y][x].setContent(board.cells[y][x].getContent().clone());
            }
        }
        this.selected = board.selected;

        this.interactedCells = new HashSet<>();
        this.interactedCells.addAll(board.interactedCells);

        this.playerSide = board.playerSide.clone();
        this.enemySide = board.enemySide.clone();
        this.selectedPoint = board.selectedPoint.clone();
        this.enemy = board.enemy;
        this.currentPath = board.currentPath;

        this.turn = board.turn.isPlayer() ? playerSide : enemySide;
    }

    @Override
    public Board clone(){
        Board board = new Board();

        board.interactedCells = new HashSet<>();

        board.interactedCells.addAll(interactedCells);

        board.enemy = enemy;
        board.selected = selected;
        board.cells = new Cell[getInstance().BOARD_SIZE][getInstance().BOARD_SIZE];
        for (int y = 0; y < getInstance().BOARD_SIZE; y++) {
            for (int x = 0; x < getInstance().BOARD_SIZE; x++) {
                board.cells[y][x] = new Cell(cells[y][x].getContent().clone());
            }
        }
        board.selectedPoint = selectedPoint.clone();
        board.playerSide = playerSide.clone();
        board.enemySide = enemySide.clone();
        board.currentPath = currentPath;
        board.emptySet = emptySet;

        board.turn = turn.isPlayer() ? board.playerSide : board.enemySide;

        return board;
    }

    public int getPlayerAdvantagePoints(){
        return playerAdvantagePoints;
    }

    public int getEnemyAdvantagePoints(){
        return enemyAdvantagePoints;
    }

    //TODO refactor
    public BoardState calculateCurrentOtherState() {
        double chance = 0.2; //TODO config
        double generatedValue = Math.random();

        if (generatedValue < chance) {

        }

        return new IdleBoardState();
    }

    private BoardState calculateSpawnBlackHoleState() {
        Point blackHoleSpawnPoint = Randomizer.generatePoint(0, getInstance().BOARD_SIZE - 1);

        Cell cellWithBlackHole = Cell.initWithBlackHole();

        if (isShip(blackHoleSpawnPoint)) {
            Spaceship victimSpaceship = (Spaceship) cells[blackHoleSpawnPoint.y][blackHoleSpawnPoint.x].getContent();

            destroyShip(blackHoleSpawnPoint, cellWithBlackHole);

            return new BlackHoleSpawnState(blackHoleSpawnPoint, victimSpaceship);
        } else {
            setCell(blackHoleSpawnPoint, cellWithBlackHole);

            return new BlackHoleSpawnState(blackHoleSpawnPoint);
        }
    }

    private BoardState calculateSpaceDebrisSpawnState() {
        Map<Point, Integer> targets = new HashMap<>();
        SpaceWeather debris = new SpaceDebris();

        for (int i = 0; i < debris.getPiecesNumber(); i++) {
            Point target = Randomizer.generatePoint(0, getInstance().BOARD_SIZE);
            int damage = 0;

            if(isShip(target)) {
                damage = debris.generateDamage();
                damageShip(target, damage);
            }

            targets.put(target, damage);
        }

        return //TODO state
    }

    private BoardState calculateSupplyKitSpawn() {
        Point spawnPoint = Randomizer.generatePoint(0, getInstance().BOARD_SIZE);
        SupplyKit supplyKit;

        int randomValue = (int) (Math.random() * 2);
        if (randomValue == 0) {
            supplyKit = new HealthKit();

            return //TODO
        } else {
            supplyKit = new DamageKit();

        }
    }

    private void setCell(Point target, Cell newCell) {
        cells[target.y][target.x] = newCell;
    }

    private void changeTurn() {
        turn.resetTurns();
        turn = turn.isPlayer() ? enemySide : playerSide;
    }

    public boolean sideCompletedTurn() {
        return turn.getTurns() == 2 || turn.getTurns() == 1 && turn.getShipsNumber() == 1 && availableForAttack.isEmpty();
    }

    public boolean isGameOver() {
        return playerSide.getShipsNumber() == 0 || enemySide.getShipsNumber() == 0;
    }

    public boolean isLoading() {
        return enemy.isLoading();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Arrays.deepEquals(cells, board.cells);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(emptySet, selected,
                interacted, interactedCells,
                emptyCells, turnCount,
                turn, selectedPoint,
                playerAdvantagePoints, enemyAdvantagePoints,
                enemy, currentPath,
                availableForMove, availableForAttack,
                playerSide, enemySide, otherSide);
        result = 31 * result + Arrays.hashCode(cells);
        return result;
    }
}

