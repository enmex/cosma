package com.imit.cosma.model.board;

import static com.imit.cosma.config.Config.getInstance;

import com.imit.cosma.ai.AI;
import com.imit.cosma.model.board.content.DamageKit;
import com.imit.cosma.model.board.content.HealthKit;
import com.imit.cosma.model.board.content.SupplyKit;
import com.imit.cosma.model.board.state.BlackHoleSpawnEvent;
import com.imit.cosma.model.board.state.BoardEvent;
import com.imit.cosma.model.board.state.IdleBoardEvent;
import com.imit.cosma.model.board.state.ShipAttackBoardEvent;
import com.imit.cosma.model.board.state.ShipMovementBoardEvent;
import com.imit.cosma.model.board.state.SpaceDebrisAttackEvent;
import com.imit.cosma.model.board.weather.SpaceDebris;
import com.imit.cosma.model.board.weather.SpaceWeather;
import com.imit.cosma.model.rules.Attack;
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
import com.imit.cosma.util.Path;
import com.imit.cosma.util.PingPongList;
import com.imit.cosma.util.IntegerPoint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Board {
    private final Set<IntegerPoint> emptySet;

    private final ObjectController objectController;

    private final Cell[][] cells;
    private Cell selected;
    private final Cell interacted;
    private final Set<IntegerPoint> interactedCells;

    private Side turn;

    private final IntegerPoint selectedPoint;

    private AI enemy;
    private Path currentPath;
    private IntegerPoint currentContentSpawnPoint;

    private Set<IntegerPoint> availableForMove, availableForAttack;

    private final Side playerSide;
    private final Side enemySide;
    private final PingPongList<Side> sides;

    public Board() {
        cells = new Cell[getInstance().BOARD_SIZE][getInstance().BOARD_SIZE];
        emptySet = new HashSet<>();
        selected = new Cell(getInstance().SPACE);
        interacted = new Cell(getInstance().SPACE);

        objectController = new ObjectController();

        SpaceshipBuilder spaceshipBuilder = new SpaceshipBuilder();

        playerSide = new PlayerSide();
        enemySide = new EnemySide();

        sides = new PingPongList<>(3);
        sides.add(playerSide);
        sides.add(new NeutralSide());
        sides.add(enemySide);

        selectedPoint = new IntegerPoint();

        /*
        IntegerPoint pShip = new IntegerPoint(4, 4);
        IntegerPoint eShip1 = new IntegerPoint(1, 5);
        IntegerPoint eShip2 = new IntegerPoint(2, 5);

        for (int y = 0; y < getInstance().BOARD_SIZE; y++) {
            for (int x = 0; x < getInstance().BOARD_SIZE; x++) {
                IntegerPoint point = new IntegerPoint(x, y);
                if (point.equals(pShip)) {
                    cells[y][x] = new Cell(spaceshipBuilder.setSide(playerSide)
                            .addSkeleton(Skeleton.DREADNOUGHT)
                            .addWeapon(8)
                            .setMoveType(MoveType.QUEEN).build());
                    objectController.addSpaceship(x, y);
                } else if (point.equals(eShip1)) {
                    cells[y][x] = new Cell(spaceshipBuilder.setSide(enemySide)
                            .addSkeleton(Skeleton.DREADNOUGHT)
                            .addWeapon(2)
                            .setMoveType(MoveType.OFFICER).build());
                    objectController.addSpaceship(x, y);
                } else if (point.equals(eShip2)) {
                    cells[y][x] = new Cell(spaceshipBuilder.setSide(enemySide)
                            .addSkeleton(Skeleton.DREADNOUGHT)
                            .addWeapon(2)
                            .setMoveType(MoveType.OFFICER).build());
                    objectController.addSpaceship(x, y);
                } else {
                    cells[y][x] = Cell.initWithSpace();
                    objectController.addSpace(x, y);
                }
            }
        }*/


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

    public BoardEvent getCurrentState(IntegerPoint selected) {
        if(inBoard(selected)){
            if (turn.isPlayer()) {
                return calculateCurrentPlayerState(selected);
            } else if (turn instanceof EnemySide) {
                return calculateCurrentEnemyState();
            } else {
                return calculateCurrentOtherState();
            }
        }
        return new IdleBoardEvent();
    }

    public BoardEvent calculateCurrentPlayerState(IntegerPoint targetPoint){
        currentPath = new Path(this.selectedPoint, targetPoint);

        if(selected.isShip() && selected.getStepMode() != StepMode.COMPLETED && selected.getSide() == turn) {
            if (selectedCanMoveTo(targetPoint)) {
                turn.updateTurns();
                enemy.savePlayerTurn(currentPath, StepMode.MOVE);
                setSelectedPosition(targetPoint);
                setSelected(targetPoint);
                return new ShipMovementBoardEvent(turn.isPlayer() ? getCell(targetPoint) : selected, currentPath);
            } else if (selectedCanFireTo(targetPoint)) {
                damageShip(targetPoint, selected.getDamagePoints());

                turn.updateTurns();
                enemy.savePlayerTurn(currentPath, StepMode.ATTACK);
                return new ShipAttackBoardEvent(selected, interacted, currentPath);
            }
        }
        setSelected(targetPoint);
        return new IdleBoardEvent();
    }

    public Path getCurrentPath() {
        return currentPath;
    }

    public BoardEvent calculateCurrentEnemyState(){
        enemy.update(this);
        System.out.println("Enemy`s turn");
        currentPath = enemy.getPath();
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

                return new ShipMovementBoardEvent(selected, currentPath);
            } else if (selectedCanFireTo(target)) {
                damageShip(target, selected.getDamagePoints());
                turn.updateTurns();

                return new ShipAttackBoardEvent(selected, interacted, currentPath);
            }
        }
        return new IdleBoardEvent();
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

    private boolean sideCompletedTurn() {
        return turn.completedTurn() && (!turn.isPlayingSide() || turn.isPlayingSide() && selected.getStepMode() == StepMode.COMPLETED);
    }

    public boolean isShip(IntegerPoint target) {
        return isShip(target.x, target.y);
    }

    public boolean isShip(int x, int y) {
        return cells[y][x].isShip();
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

        if(cells[target.y][target.x].getContent().getHealthPoints() <= 0){
            destroyShip(target, Cell.initWithSpace());
            objectController.setEmpty(new IntegerPoint(target));
        }
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

    private boolean isSupplyKit(IntegerPoint target) {
        return cells[target.y][target.x].getContent() instanceof SupplyKit;
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

        objectController.setSpaceship(new IntegerPoint(destination));
        objectController.setEmpty(new IntegerPoint(selectedPoint));

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
            availableForAttack = selected.getStepMode() == StepMode.ATTACK
                    ? Attack.getAvailable(this, selectedPoint)
                    : emptySet;
            availableForMove = selected.getStepMode() == StepMode.MOVE
                    ? selected.getMoveType().getMove().getAvailable(this, selectedPoint)
                    : emptySet;

            if (selected.getStepMode() == StepMode.ATTACK && availableForAttack.isEmpty()) {
                selected.setStepMode(StepMode.COMPLETED);
            }
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
        return cells[y][x].getDamagePoints();
    }

    public BoardEvent calculateCurrentOtherState() {
        if (Math.random() < getInstance().SPACE_DEBRIS_SPAWN_CHANCE) {
            return calculateSpaceDebrisSpawnState();
        }

        if (Math.random() < getInstance().BLACK_HOLE_SPAWN_CHANCE) {
            return calculateSpawnBlackHoleState();
        }

        turn.updateTurns();
        return new IdleBoardEvent();
    }

    private BoardEvent calculateSpawnBlackHoleState() {
        turn.updateTurns();

        currentContentSpawnPoint = Randomizer.generatePoint(0, getInstance().BOARD_SIZE - 1);
        objectController.addGameObject(currentContentSpawnPoint);

        IntegerPoint blackHoleSpawnPoint = currentContentSpawnPoint.clone();
        Cell cellWithBlackHole = Cell.initWithBlackHole();

        currentPath = new Path(blackHoleSpawnPoint, blackHoleSpawnPoint);
        if (isShip(blackHoleSpawnPoint)) {
            Spaceship victimSpaceship = (Spaceship) cells[blackHoleSpawnPoint.y][blackHoleSpawnPoint.x].getContent();

            destroyShip(blackHoleSpawnPoint, cellWithBlackHole);
            setCell(blackHoleSpawnPoint, cellWithBlackHole);

            return new BlackHoleSpawnEvent(blackHoleSpawnPoint, victimSpaceship);
        } else {
            setCell(blackHoleSpawnPoint, cellWithBlackHole);
            objectController.setGameObject(blackHoleSpawnPoint);

            return new BlackHoleSpawnEvent(blackHoleSpawnPoint);
        }
    }

    private BoardEvent calculateSpaceDebrisSpawnState() {
        turn.updateTurns();

        List<IntegerPoint> targets = new ArrayList<>();
        List<Integer> damages = new ArrayList<>();
        List<Spaceship> spaceships = new ArrayList<>();

        SpaceWeather debris = new SpaceDebris();

        List<IntegerPoint> spaceshipLocations = new ArrayList<>(objectController.getSpaceshipsLocations());

        int piecesNumber = Math.min(spaceshipLocations.size(), debris.getPiecesNumber());

        for (int i = 0; i < piecesNumber; i++) {
            IntegerPoint target = Randomizer.getRandom(spaceshipLocations);
            int damage = debris.generateDamage();

            targets.add(target);
            damages.add(damage);
            spaceships.add((Spaceship) getCell(target).getContent().clone());

            damageShip(target, damage);

            spaceshipLocations.remove(target);
        }

        return new SpaceDebrisAttackEvent(targets, damages, spaceships);
    }

    private BoardEvent calculateSupplyKitSpawnState() {
        IntegerPoint spawnPoint = Randomizer.getRandom(objectController.getSpaceLocations());
        SupplyKit supplyKit;

        int randomValue = (int) (Math.random() * 2);
        
        supplyKit = randomValue == 0 ? new HealthKit() : new DamageKit();
        Cell cell = new Cell(supplyKit);
        setCell(spawnPoint, cell);

        return new IdleBoardEvent();
    }

    private void setCell(IntegerPoint target, Cell newCell) {
        cells[target.y][target.x] = newCell;
    }

    private void changeTurn() {
        turn.resetTurns();
        sides.next();
        turn = sides.get();
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
}