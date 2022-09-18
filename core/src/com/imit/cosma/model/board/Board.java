package com.imit.cosma.model.board;

import static com.imit.cosma.config.Config.getInstance;

import com.imit.cosma.ai.AI;
import com.imit.cosma.model.board.content.DamageKit;
import com.imit.cosma.model.board.content.HealthKit;
import com.imit.cosma.model.board.content.SupplyKit;
import com.imit.cosma.model.board.state.BlackHoleSpawnState;
import com.imit.cosma.model.board.state.BoardState;
import com.imit.cosma.model.board.state.IdleBoardState;
import com.imit.cosma.model.board.state.ShipAttackingOneTargetBoardState;
import com.imit.cosma.model.board.state.ShipMovingBoardState;
import com.imit.cosma.model.rules.Attack;
import com.imit.cosma.model.rules.side.EnemySide;
import com.imit.cosma.model.rules.side.NeutralSide;
import com.imit.cosma.model.rules.side.PlayerSide;
import com.imit.cosma.model.rules.side.Side;
import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.model.spaceship.ShipRandomizer;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.model.spaceship.SpaceshipBuilder;
import com.imit.cosma.pkg.random.Randomizer;
import com.imit.cosma.util.Cloneable;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.PingPongList;
import com.imit.cosma.util.IntegerPoint;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Board implements Cloneable {
    private Set<IntegerPoint> emptySet;

    private ObjectController objectController;

    private Cell[][] cells;
    private Cell selected;
    private final Cell interacted;
    private Set<IntegerPoint> interactedCells;

    private int turnCount;

    private Side turn;

    private IntegerPoint selectedPoint;

    private int playerAdvantagePoints, enemyAdvantagePoints;

    private AI enemy;
    private Path currentPath;
    private IntegerPoint currentContentSpawnPoint;

    private Set<IntegerPoint> availableForMove, availableForAttack;

    private Side playerSide;
    private Side enemySide;
    private final Side neutralSide;
    private final PingPongList<Side> sides;

    public Board() {
        cells = new Cell[getInstance().BOARD_SIZE][getInstance().BOARD_SIZE];
        emptySet = new HashSet<>();
        selected = new Cell(getInstance().SPACE);
        interacted = new Cell(getInstance().SPACE);
        playerAdvantagePoints = 0;
        enemyAdvantagePoints = 0;

        objectController = new ObjectController();

        SpaceshipBuilder spaceshipBuilder = new SpaceshipBuilder();

        playerSide = new PlayerSide(getInstance().DEFAULT_SHIPS_NUMBER);
        enemySide = new EnemySide(getInstance().DEFAULT_SHIPS_NUMBER);
        neutralSide = new NeutralSide();

        sides = new PingPongList<>(3);
        sides.add(playerSide);
        sides.add(neutralSide);
        sides.add(enemySide);

        selectedPoint = new IntegerPoint();
        /*
        Point pShip = new Point(4, 4);
        Point eShip = new Point(0, 7);

        for (int y = 0; y < getInstance().BOARD_SIZE; y++) {
            for (int x = 0; x < getInstance().BOARD_SIZE; x++) {
                Point point = new Point(x, y);
                if (point.equals(pShip)) {
                    cells[y][x] = new Cell(spaceshipBuilder.setSide(playerSide)
                            .addSkeleton(Skeleton.DREADNOUGHT)
                            .addWeapon(ShipRandomizer.getRandomAmount())
                            .setMoveType(MoveType.QUEEN).build());
                    objectController.addSpaceship(x, y);
                } else if (point.equals(eShip)) {
                    cells[y][x] = new Cell(spaceshipBuilder.setSide(enemySide)
                            .addSkeleton()
                            .addWeapon(ShipRandomizer.getRandomAmount())
                            .setMoveType(MoveType.OFFICER).build());
                    objectController.addSpaceship(x, y);
                } else {
                    cells[y][x] = Cell.initWithSpace();
                    objectController.addSpace(x, y);
                }
            }
        }
         */

        //initialise player ships
        for (int y = 0; y < 1; y++) {
            for (int x = 0; x < getInstance().BOARD_SIZE; x++) {
                Spaceship spaceship = spaceshipBuilder.setSide(playerSide)
                        .addSkeleton()
                        .addWeapon(ShipRandomizer.getRandomAmount())
                        .setMoveType().build();
                cells[y][x] = new Cell(spaceship);
                objectController.addSpaceship(x, y);
            }
        }

        //initialise space cells
        for (int y = 1; y < getInstance().BOARD_SIZE - 1; y++) {
            for (int x = 0; x < getInstance().BOARD_SIZE; x++) {
                cells[y][x] = new Cell();
                objectController.addSpace(x, y);
            }
        }

        //initialise enemy ships
        for (int y = getInstance().BOARD_SIZE - 1; y < getInstance().BOARD_SIZE; y++) {
            for (int x = 0; x < getInstance().BOARD_SIZE; x++) {
                Spaceship spaceship = spaceshipBuilder.setSide(enemySide)
                        .addSkeleton()
                        .addWeapon(ShipRandomizer.getRandomAmount())
                        .setMoveType().build();
                cells[y][x] = new Cell(spaceship);
                objectController.addSpaceship(x, y);
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

    public BoardState getCurrentState(IntegerPoint selected) {
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

    public BoardState calculateCurrentPlayerState(IntegerPoint selectedPoint){
        currentPath = new Path(this.selectedPoint.clone(), selectedPoint);

        if(selected.isShip() && selected.getStepMode() != StepMode.COMPLETED && selected.getSide() == turn) {
            if (selectedCanMoveTo(selectedPoint)) {
                setSelectedPosition(selectedPoint);

                turn.updateTurns();
                enemy.savePlayerTurn(currentPath, StepMode.MOVE);
                setSelected(selectedPoint);
                return new ShipMovingBoardState(turn.isPlayer() ? getCell(selectedPoint) : selected, currentPath);
            } else if (selectedCanFireTo(selectedPoint)) {
                damageShip(selectedPoint, selected.getDamageAmount());

                turn.updateTurns();
                enemy.savePlayerTurn(currentPath, StepMode.ATTACK);
                return new ShipAttackingOneTargetBoardState(selected, interacted, currentPath);
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
        IntegerPoint source = currentPath.getSource();
        IntegerPoint target = currentPath.getTarget();

        setSelectedEnemyTurn(source);

        if (isShip(target)) {
            selected.setStepMode(StepMode.ATTACK);
        }

        if (selected.isShip() && selected.getStepMode() != StepMode.COMPLETED && selected.getSide().equals(turn)) {
            if (selectedCanMoveTo(target)) {
                setSelectedPosition(target);
                turn.updateTurns();

                setSelectedEnemyTurn(target);

                return new ShipMovingBoardState(selected, currentPath);
            } else if (selectedCanFireTo(target)) {
                damageShip(target, selected.getDamageAmount());
                turn.updateTurns();

                return new ShipAttackingOneTargetBoardState(selected, interacted, currentPath);
            }
        }
        return new IdleBoardState();
    }

    public void updateSide(){
        if(sideCompletedTurn()){
            changeTurn();

            for(IntegerPoint point : interactedCells){
                cells[point.y][point.x].setStepMode(StepMode.MOVE);
            }
            interactedCells.clear();
        }
    }

    public boolean isShip(IntegerPoint target) {
        return isShip(target.x, target.y);
    }

    public boolean isShip(int x, int y) {
        return cells[y][x].isShip();
    }

    public boolean containsContent(int x, int y) {
        return !objectController.isEmpty(x, y);
    }

    public boolean isShipSelected() {
        return selected.isShip();
    }

    public boolean isEnemyShip(IntegerPoint target){
        return isShip(target)
                && (selected.getSide().isPlayer() && !cells[target.y][target.x].getSide().isPlayer()
                || !selected.getSide().isPlayer() && cells[target.y][target.x].getSide().isPlayer());
    }

    public void damageShip(IntegerPoint target, int damage){
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
            objectController.setEmpty(target);
        }
    }

    public void healShip(IntegerPoint target, int healthPoints) {
        interacted.setContent(cells[target.y][target.x].getContent().clone());

        cells[target.y][target.x].addHealthPoints(healthPoints);
        selected.setStepMode(StepMode.COMPLETED);
        interactedCells.add(selectedPoint);
    }

    private void destroyShip(IntegerPoint target, Cell replacement) {
        if (cells[target.y][target.x].getSide().isPlayer()) {
            playerSide.removeShip();
        } else {
            enemySide.removeShip();
        }

        cells[target.y][target.x] = replacement;
    }

    public boolean isPassable(IntegerPoint target) {
        return isPassable(target.x, target.y);
    }

    public boolean isPassable(int x, int y) {
        return inBoard(x, y) && cells[y][x].isPassable();
    }

    public Cell getSelected() {
        return selected;
    }

    public boolean selectedCanMoveTo(IntegerPoint target){
        return selected.isShip()
                && cells[target.y][target.x] != selected
                && selected.canMoveTo(selectedPoint.x, selectedPoint.y, target.x, target.y)
                && selected.getStepMode() == StepMode.MOVE && isPassable(target);
    }

    public boolean selectedCanFireTo(IntegerPoint target){
        return selected != null && isShipSelected() && isShip(target)
                && selected.getSide() != cells[target.y][target.x].getSide()
                && selected.getStepMode() == StepMode.ATTACK
                && availableForAttack.contains(target);
    }

    public Cell getCell(IntegerPoint target){
        return getCell(target.x, target.y);
    }

    public Cell getCell(int x, int y) {
        return cells[y][x];
    }

    public Set<IntegerPoint> getAvailableCellsForMove() {
        return selected.isShip() && selected.getStepMode() == StepMode.MOVE && selected.getSide() == turn
                ? availableForMove : emptySet;
    }

    public Set<IntegerPoint> getAvailableCellsForMove(IntegerPoint target) {
        return isShip(target) && cells[target.y][target.x].getStepMode() == StepMode.MOVE
                ? cells[target.y][target.x].getMoveType().getMove().getAvailable(this, target)
                : emptySet;
    }

    public Set<IntegerPoint> getAvailableCellsForMove(int x, int y) {
        return isShip(x, y) && cells[y][x].getStepMode() == StepMode.MOVE
                ? cells[y][x].getMoveType().getMove().getAvailable(this, new IntegerPoint(x, y))
                : emptySet;
    }

    public Set<IntegerPoint> getAvailableCellsForFire(){
        return selected.isShip() && selected.getStepMode() == StepMode.ATTACK ? availableForAttack : emptySet;
    }

    public Set<IntegerPoint> getAvailableCellsForFire(int x, int y) {
        return isShip(x, y) && cells[y][x].getStepMode() == StepMode.ATTACK
                ? Attack.getAvailable(this, x, y)
                : emptySet;
    }

    public List<IntegerPoint> getNonEmptyLocations() {
        return objectController.getNonEmptyLocations();
    }

    private void setSelectedPosition(IntegerPoint destination) {
        interacted.setContent(cells[destination.y][destination.x].getContent().clone());

        swapCells(selectedPoint, destination);
        cells[destination.y][destination.x].setStepMode(StepMode.ATTACK);

        objectController.setSpaceship(destination);
        objectController.setEmpty(selectedPoint);

        interactedCells.add(destination.clone());
    }

    public String getIdleAnimationPath(int x, int y){
        return cells[y][x].getIdleAnimationPath();
    }

    public String getIdleAnimationPath(IntegerPoint target) {
        return getIdleAnimationPath(target.x, target.y);
    }

    public boolean inBoard(IntegerPoint target){
        return inBoard(target.x, target.y);
    }

    public boolean inBoard(int x, int y){
        return x >=0 && x < 8 && y >= 0 && y < 8;
    }

    public IntegerPoint getSelectedPoint() {
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

    public float getDefaultRotation(IntegerPoint target) {
        return getDefaultRotation(target.x, target.y);
    }

    public void setSelectedEnemyTurn(IntegerPoint target) {
        if(inBoard(target) && !turn.isPlayer()) {
            setSelected(target);
        }
    }

    private void setSelected(IntegerPoint target) {
        selected = cells[target.y][target.x];
        selectedPoint.set(target);

        if(selected.isShip()) {
            availableForAttack = selected.getStepMode() == StepMode.ATTACK ? Attack.getAvailable(this, selectedPoint) : emptySet;
            availableForMove = selected.getStepMode() == StepMode.MOVE ? selected.getMoveType().getMove().getAvailable(this, selectedPoint) : emptySet;
        }
    }

    private void swapCells(IntegerPoint a, IntegerPoint b) {
        cells[a.y][a.x].swapContents(cells[b.y][b.x]);
    }

    public StepMode getStepMode(int x, int y) {
        return cells[y][x].getStepMode();
    }

    public int getMaxHealthPoints(int x, int y) {
        return cells[y][x].getMaxHealthPoints();
    }

    public int getHealthPoints(IntegerPoint target){
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
        double spaceWeatherSpawnChance = 0.2; //TODO config
        double supplyKitSpawnChance = 0.7;
        double generatedValue = Math.random();
        if (Math.random() < 0.5) {
            if (generatedValue < spaceWeatherSpawnChance) {
                return calculateSpawnBlackHoleState();
            } else {
                objectController.clearGameObjects();
            }
        } else {
            if (generatedValue < supplyKitSpawnChance) {
                return calculateSupplyKitSpawnState();
            } else {
                objectController.clearGameObjects();
            }
        }

        return new IdleBoardState();
    }

    private void clearCell(IntegerPoint target) {
        cells[target.y][target.x].setContent(getInstance().SPACE);
    }

    private BoardState calculateSpawnBlackHoleState() {
        currentContentSpawnPoint = Randomizer.generatePoint(0, getInstance().BOARD_SIZE - 1);
        objectController.addGameObject(currentContentSpawnPoint);

        IntegerPoint blackHoleSpawnPoint = currentContentSpawnPoint.clone();
        Cell cellWithBlackHole = Cell.initWithBlackHole();

        turn.updateTurns();
        if (isShip(blackHoleSpawnPoint)) {
            Spaceship victimSpaceship = (Spaceship) cells[blackHoleSpawnPoint.y][blackHoleSpawnPoint.x].getContent();

            destroyShip(blackHoleSpawnPoint, cellWithBlackHole);
            setCell(blackHoleSpawnPoint, cellWithBlackHole);

            return new BlackHoleSpawnState(blackHoleSpawnPoint, victimSpaceship);
        } else {
            setCell(blackHoleSpawnPoint, cellWithBlackHole);
            objectController.setGameObject(blackHoleSpawnPoint);

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
        IntegerPoint spawnPoint = Randomizer.getRandom(objectController.getSpaceLocations());
        SupplyKit supplyKit;

        int randomValue = (int) (Math.random() * 2);
        
        supplyKit = randomValue == 0 ? new HealthKit() : new DamageKit();
        Cell cell = new Cell(supplyKit);
        setCell(spawnPoint, cell);
        
        //return new SupplyKitSpawnState(spawnPoint, supplyKit);
        return new IdleBoardState();
    }

    private void setCell(IntegerPoint target, Cell newCell) {
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

    public IntegerPoint getCurrentContentSpawnPoint() {
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
                interactedCells,
                turnCount, turn, selectedPoint, playerAdvantagePoints,
                enemyAdvantagePoints, enemy, currentPath, availableForMove,
                availableForAttack, playerSide, enemySide, neutralSide);
        result = 31 * result + Arrays.hashCode(cells);
        return result;
    }
}