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
import com.imit.cosma.model.board.state.SpaceDebrisAttackingState;
import com.imit.cosma.model.board.state.SupplyKitSpawnState;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Board implements Cloneable {
    private Set<Point> emptySet;

    private Cell[][] cells;
    private Cell selected;
    private Cell interacted;
    private Set<Point> interactedCells;
    private List<Point> emptyCells, contentCells;

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
        emptyCells = new ArrayList<>();
        contentCells = new ArrayList<>();
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
                contentCells.add(new Point(x, y));
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
                contentCells.add(new Point(x, y));
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
        if(inBoard(selected)){
            if (turn.isPlayer()) {
                return calculateCurrentPlayerState(selected);
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
            if (selectedCanMoveTo(target)) {
                setSelectedPosition(target);
                turn.updateTurns();

                setSelectedEnemyTurn(target);

                return new ShipMovingBoardState(selected);
            } else if (selectedCanFireTo(target)) {
                damageShip(target, selected.getDamageAmount());
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

    public boolean isEnemyShip(Point target){
        return isShip(target)
                && (selected.getSide().isPlayer() && !cells[target.y][target.x].getSide().isPlayer()
                || !selected.getSide().isPlayer() && cells[target.y][target.x].getSide().isPlayer());
    }

    public void damageShip(Point target, int damage){
        interacted.setContent(cells[target.y][target.x].getContent().clone());

        cells[target.y][target.x].setDamage(damage);
        selected.setStepMode(StepMode.COMPLETED);
        interactedCells.add(selectedPoint);

        if(turn.isPlayer()){
            playerAdvantagePoints += Math.min(damage, cells[target.y][target.x].getHealthPoints());
        }
        else{
            enemyAdvantagePoints += Math.min(damage, cells[target.y][target.x].getHealthPoints());
        }

        if(cells[target.y][target.x].getContent().getHealthPoints() <= 0){
            destroyShip(target, Cell.initWithSpace());
            emptyCells.add(target);
            contentCells.remove(target);
        }
    }

    public void healShip(Point target, int healthPoints) {
        interacted.setContent(cells[target.y][target.x].getContent().clone());

        cells[target.y][target.x].addHealthPoints(healthPoints);
        selected.setStepMode(StepMode.COMPLETED);
        interactedCells.add(selectedPoint);
    }

    private void destroyShip(Point target, Cell replacement) {
        cells[target.y][target.x] = replacement;
        if(turn.isPlayer()) {
            enemySide.removeShip();
        }
        else {
            playerSide.removeShip();
        }
    }

    public boolean isPassable(Point target) {
        return inBoard(target) && cells[target.y][target.x].isPassable();
    }

    public Cell getSelected() {
        return selected;
    }

    public boolean selectedCanMoveTo(Point target){
        return selected.isShip()
                && cells[target.y][target.x] != selected
                && selected.canMoveTo(selectedPoint.x, selectedPoint.y, target.x, target.y)
                && selected.getStepMode() == StepMode.MOVE && isPassable(target);
    }

    public boolean selectedCanFireTo(Point target){
        return selected != null && isShipSelected() && isShip(target)
                && selected.getSide() != cells[target.y][target.x].getSide()
                && selected.getStepMode() == StepMode.ATTACK;
    }

    public Cell getCell(Point target){
        return cells[target.y][target.x];
    }

    public Set<Point> getAvailableCellsForMove() {
        return selected.isShip() && selected.getStepMode() == StepMode.MOVE && selected.getSide() == turn
                ? availableForMove : emptySet;
    }

    public Set<Point> getAvailableForMove(Point target) {
        return isShip(target) && cells[target.y][target.x].getStepMode() == StepMode.MOVE
                ? cells[target.y][target.x].getMoves().getAvailable(this, target)
                : emptySet;
    }

    public Set<Point> getAvailableCellsForFire(){
        return selected.isShip() && selected.getStepMode() == StepMode.ATTACK ? availableForAttack : emptySet;
    }

    public Set<Point> getAvailableCellsForFire(Point target) {
        return isShip(target) && cells[target.y][target.x].getStepMode() == StepMode.ATTACK
                ? Attack.getAvailable(this, target)
                : emptySet;
    }

    private void setSelectedPosition(Point destination) {
        interacted.setContent(cells[destination.y][destination.x].getContent().clone());

        contentCells.add(destination);
        contentCells.remove(selectedPoint);
        
        emptyCells.remove(destination);
        emptyCells.add(selectedPoint);

        selected.swapContents(cells[destination.y][destination.x]);
        cells[destination.y][destination.x].setStepMode(StepMode.ATTACK);
        interactedCells.add(destination);
    }

    public Point getAtlasCoords(int x, int y){
        return cells[y][x].getAtlasCoord();
    }

    public boolean inBoard(Point target){
        return target.x >=0 && target.x < 8 && target.y >= 0 && target.y < 8;
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

    public void setSelectedPlayerTurn(Point target){
        if(inBoard(target) && turn.isPlayer()){
            setSelected(target);
        }
    }

    public void setSelectedEnemyTurn(Point target) {
        if(inBoard(target) && !turn.isPlayer()) {
            setSelected(target);
        }
    }

    private void setSelected(Point target) {
        selected = cells[target.y][target.x];
        selectedPoint.set(target);

        if(selected.isShip()) {
            availableForAttack = Attack.getAvailable(this, selectedPoint);
            availableForMove = selected.getMoves().getAvailable(this, selectedPoint);
        }
    }

    public int getDamagePoints(Point target){
        return cells[target.y][target.x].getDamageAmount();
    }

    public int getMaxHealthPoints(Point target){
        return cells[target.y][target.x].getMaxHealthPoints();
    }

    public int getHealthPoints(Point target){
        return cells[target.y][target.x].getHealthPoints();
    }

    public StepMode doArtificialTurn(Path path){
        StepMode mode = StepMode.COMPLETED;

        Point source = path.getSource();
        Point target = path.getTarget();

        setSelected(source);

        if(selectedCanMoveTo(target)){
            setSelectedPosition(target);

            setSelected(target);
            turn.updateTurns();

            mode = StepMode.MOVE;
        }
        else if(selectedCanFireTo(target)){
            damageShip(target, selected.getDamageAmount());
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
        double spaceWeatherSpawnChance = 0.2; //TODO config
        double supplyKitSpawnChance = 0.7;
        double generatedValue = Math.random();

        if (generatedValue < spaceWeatherSpawnChance) {
            return (int)(Math.random() * 2) == 0
                    ? calculateSpawnBlackHoleState()
                    : calculateSpaceDebrisSpawnState();
        }

        if (generatedValue < supplyKitSpawnChance) {
            return calculateSupplyKitSpawnState();
        }

        return new IdleBoardState();
    }

    private BoardState calculateSpawnBlackHoleState() {
        Point blackHoleSpawnPoint = Randomizer.generatePoint(0, getInstance().BOARD_SIZE - 1);

        Cell cellWithBlackHole = Cell.initWithBlackHole();

        if (isShip(blackHoleSpawnPoint)) {
            Spaceship victimSpaceship = (Spaceship) cells[blackHoleSpawnPoint.y][blackHoleSpawnPoint.x].getContent();

            destroyShip(blackHoleSpawnPoint, cellWithBlackHole);
            setCell(blackHoleSpawnPoint, cellWithBlackHole);

            return new BlackHoleSpawnState(blackHoleSpawnPoint, victimSpaceship);
        } else {
            setCell(blackHoleSpawnPoint, cellWithBlackHole);
            emptyCells.remove(blackHoleSpawnPoint);

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

        return new SpaceDebrisAttackingState();
    }

    private BoardState calculateSupplyKitSpawnState() {
        Point spawnPoint = Randomizer.getRandom(emptyCells);
        SupplyKit supplyKit;

        int randomValue = (int) (Math.random() * 2);
        
        supplyKit = randomValue == 0 ? new HealthKit() : new DamageKit();
        
        return new SupplyKitSpawnState(spawnPoint, supplyKit);
    }

    private void setCell(Point target, Cell newCell) {
        emptyCells.remove(target);
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

