package com.imit.cosma.model.board;

import static com.imit.cosma.config.Config.getInstance;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.imit.cosma.ai.AI;
import com.imit.cosma.config.Config;
import com.imit.cosma.model.board.content.DamageKit;
import com.imit.cosma.model.board.content.HealthKit;
import com.imit.cosma.model.board.content.Space;
import com.imit.cosma.model.board.content.SupplyKit;
import com.imit.cosma.model.board.state.BlackHoleSpawnState;
import com.imit.cosma.model.board.state.BoardState;
import com.imit.cosma.model.board.state.IdleBoardState;
import com.imit.cosma.model.board.state.ShipAttackingOneTargetBoardState;
import com.imit.cosma.model.board.state.ShipMovingBoardState;
import com.imit.cosma.model.board.state.SupplyKitSpawnState;
import com.imit.cosma.model.rules.Attack;
import com.imit.cosma.model.rules.move.MoveType;
import com.imit.cosma.model.rules.move.QueenMovingStyle;
import com.imit.cosma.model.rules.side.EnemySide;
import com.imit.cosma.model.rules.side.NeutralSide;
import com.imit.cosma.model.rules.side.PlayerSide;
import com.imit.cosma.model.rules.side.Side;
import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.model.spaceship.ShipRandomizer;
import com.imit.cosma.model.spaceship.Skeleton;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.model.spaceship.SpaceshipBuilder;
import com.imit.cosma.pkg.random.Randomizer;
import com.imit.cosma.util.Cloneable;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.PingPongList;
import com.imit.cosma.util.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
    private Point currentContentSpawnPoint;

    private Set<Point> availableForMove, availableForAttack;

    private Side playerSide, enemySide, neutralSide;
    private PingPongList<Side> sides;

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

        playerSide = new PlayerSide(getInstance().DEFAULT_SHIPS_NUMBER);
        enemySide = new EnemySide(getInstance().DEFAULT_SHIPS_NUMBER);
        neutralSide = new NeutralSide();

        sides = new PingPongList<>(3);
        sides.add(playerSide);
        sides.add(neutralSide);
        sides.add(enemySide);

        selectedPoint = new Point();
        /*
        for (int y = 0; y < getInstance().BOARD_SIZE; y++) {
            for (int x = 0; x < getInstance().BOARD_SIZE; x++) {
                cells[y][x] = new Cell();
                emptyCells.add(new Point(x, y));
            }
        }

        cells[0][0].setContent(spaceshipBuilder.setSide(playerSide)
                .addSkeleton(Skeleton.DREADNOUGHT)
                .addWeapon(4)
                .setMoveType(MoveType.QUEEN).build());

        cells[7][0].setContent(spaceshipBuilder.setSide(enemySide)
                .addSkeleton(Skeleton.CORVETTE)
                .addWeapon(ShipRandomizer.getRandomAmount())
                .setMoveType(MoveType.IDLE).build());

        emptyCells.remove(new Point(0, 0));
        emptyCells.remove(new Point(0, 7));

        contentCells.add(new Point(0, 0));
        contentCells.add(new Point(0, 7));
        */

        //initialise player ships
        for (int y = 0; y < getInstance().SPACESHIP_ROWS; y++) {
            for (int x = 0; x < getInstance().BOARD_SIZE; x++) {
                Spaceship spaceship = spaceshipBuilder.setSide(playerSide)
                        .addSkeleton()
                        .addWeapon(ShipRandomizer.getRandomAmount())
                        .setMoveType().build();
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
                        .setMoveType().build();
                cells[y][x] = new Cell(spaceship);
                contentCells.add(new Point(x, y));
            }
        }

        turn = playerSide;
        interactedCells = new HashSet<>();

        availableForMove = new HashSet<>();
        availableForAttack = new HashSet<>();
    }

    public void initAI(){
        enemy = new AI(this);
    }

    public BoardState getCurrentState(Point selected) {
        if(inBoard(selected)){
            if (turn.isPlayer()) {
                return calculateCurrentPlayerState(selected);
            } else if (turn instanceof EnemySide) {
                return calculateCurrentEnemyState();
            } else {
                return calculateCurrentOtherState();
            }
        }
        return new IdleBoardState();
    }

    public BoardState calculateCurrentPlayerState(Point selectedPoint){
        currentPath = new Path(this.selectedPoint.clone(), selectedPoint);

        if(selected.isShip() && selected.getStepMode() != StepMode.COMPLETED && selected.getSide() == turn) {
            if (selectedCanMoveTo(selectedPoint)) {
                setSelectedPosition(selectedPoint);

                turn.updateTurns();
                enemy.savePlayerTurn(currentPath, StepMode.MOVE);
                setSelected(selectedPoint);
                return new ShipMovingBoardState(turn.isPlayer() ? getCell(selectedPoint) : selected);
            } else if (selectedCanFireTo(selectedPoint)) {
                damageShip(selectedPoint, selected.getDamageAmount());

                turn.updateTurns();
                enemy.savePlayerTurn(currentPath, StepMode.ATTACK);
                return new ShipAttackingOneTargetBoardState(selected, interacted);
            }
        }
        setSelected(selectedPoint);
        return new IdleBoardState();
    }

    public Path getCurrentPath() {
        return currentPath;
    }

    public BoardState calculateCurrentEnemyState(){
        enemy.update(this);

        currentPath = enemy.getPath(); //TODO bug null path
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

    public boolean containsContent(int x, int y) {
        return !(cells[y][x].getContent() instanceof Space);
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
        interactedCells.add(selectedPoint.clone());

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
        return isPassable(target.x, target.y);
    }

    public boolean isPassable(int x, int y) {
        return inBoard(x, y) && cells[y][x].isPassable();
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
                && selected.getStepMode() == StepMode.ATTACK
                && availableForAttack.contains(target);
    }

    public Cell getCell(Point target){
        return getCell(target.x, target.y);
    }

    public Cell getCell(int x, int y) {
        return cells[y][x];
    }

    public Set<Point> getAvailableCellsForMove() {
        return selected.isShip() && selected.getStepMode() == StepMode.MOVE && selected.getSide() == turn
                ? availableForMove : emptySet;
    }

    public Set<Point> getAvailableCellsForMove(Point target) {
        return isShip(target) && cells[target.y][target.x].getStepMode() == StepMode.MOVE
                ? cells[target.y][target.x].getMoveType().getMove().getAvailable(this, target)
                : emptySet;
    }

    public Set<Point> getAvailableCellsForMove(int x, int y) {
        return isShip(x, y) && cells[y][x].getStepMode() == StepMode.MOVE
                ? cells[y][x].getMoveType().getMove().getAvailable(this, new Point(x, y))
                : emptySet;
    }

    public Set<Point> getAvailableCellsForFire(){
        return selected.isShip() && selected.getStepMode() == StepMode.ATTACK ? availableForAttack : emptySet;
    }

    public Set<Point> getAvailableCellsForFire(int x, int y) {
        return isShip(x, y) && cells[y][x].getStepMode() == StepMode.ATTACK
                ? Attack.getAvailable(this, x, y)
                : emptySet;
    }

    private void setSelectedPosition(Point destination) {
        interacted.setContent(cells[destination.y][destination.x].getContent().clone());

        contentCells.add(destination);
        contentCells.remove(selectedPoint);
        
        emptyCells.remove(destination);
        emptyCells.add(selectedPoint);

        swapCells(selectedPoint, destination);
        cells[destination.y][destination.x].setStepMode(StepMode.ATTACK);

        interactedCells.add(destination.clone());
    }

    public Point getAtlasCoords(int x, int y){
        return cells[y][x].getAtlasCoord();
    }

    public boolean inBoard(Point target){
        return inBoard(target.x, target.y);
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

    public void setSelectedEnemyTurn(Point target) {
        if(inBoard(target) && !turn.isPlayer()) {
            setSelected(target);
        }
    }

    private void setSelected(Point target) {
        selected = cells[target.y][target.x];
        selectedPoint.set(target);

        if(selected.isShip()) {
            availableForAttack = selected.getStepMode() == StepMode.ATTACK ? Attack.getAvailable(this, selectedPoint) : emptySet;
            availableForMove = selected.getStepMode() == StepMode.MOVE ? selected.getMoveType().getMove().getAvailable(this, selectedPoint) : emptySet;
        }
    }

    private void swapCells(Point a, Point b) {
        cells[a.y][a.x].swapContents(cells[b.y][b.x]);
    }

    public StepMode getStepMode(int x, int y) {
        return cells[y][x].getStepMode();
    }

    public int getMaxHealthPoints(int x, int y) {
        return cells[y][x].getMaxHealthPoints();
    }

    public int getHealthPoints(Point target){
        return getHealthPoints(target.x, target.y);
    }

    public int getHealthPoints(int x, int y) {
        return cells[y][x].getHealthPoints();
    }

    public int getDamagePoints(int x, int y) {
        return cells[y][x].getDamageAmount();
    }

    public boolean isGameObject(int x, int y) {
        return cells[y][x].isGameObject();
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

    //TODO refactor
    public BoardState calculateCurrentOtherState() {
        double spaceWeatherSpawnChance = 0.95; //TODO config
        double supplyKitSpawnChance = 0.7;
        double generatedValue = Math.random();

        if (generatedValue < spaceWeatherSpawnChance) {
            return calculateSpawnBlackHoleState();
        }

        return new IdleBoardState();
    }

    private BoardState calculateSpawnBlackHoleState() {
        currentContentSpawnPoint = new Point(0, 1);

        Point blackHoleSpawnPoint = currentContentSpawnPoint.clone();
        Cell cellWithBlackHole = Cell.initWithBlackHole();

        turn.updateTurns();
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
        /*
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

         */
        return new IdleBoardState();
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
        sides.next();
        turn = sides.get();
    }

    public boolean sideCompletedTurn() {
        return turn.getTurns() == 2 || turn.getTurns() == 1 && (turn.getShipsNumber() == 1 && availableForAttack.isEmpty() || turn instanceof NeutralSide);
    }

    public boolean isGameOver() {
        return playerSide.getShipsNumber() == 0 || enemySide.getShipsNumber() == 0;
    }

    public boolean isLoading() {
        return enemy.isLoading();
    }

    public Point getCurrentContentSpawnPoint() {
        return currentContentSpawnPoint;
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
        int result = Objects.hash(emptySet, selected, interacted,
                interactedCells, emptyCells, contentCells,
                turnCount, turn, selectedPoint, playerAdvantagePoints,
                enemyAdvantagePoints, enemy, currentPath, availableForMove,
                availableForAttack, playerSide, enemySide, neutralSide);
        result = 31 * result + Arrays.hashCode(cells);
        return result;
    }
}