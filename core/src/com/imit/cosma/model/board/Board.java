package com.imit.cosma.model.board;

import com.imit.cosma.ai.AI;
import com.imit.cosma.config.Config;
import com.imit.cosma.model.board.content.DamageKit;
import com.imit.cosma.model.board.content.HealthKit;
import com.imit.cosma.model.board.content.Loot;
import com.imit.cosma.model.board.event.BlackHoleSpawnEvent;
import com.imit.cosma.model.board.event.BoardEvent;
import com.imit.cosma.model.board.event.IdleBoardEvent;
import com.imit.cosma.model.board.event.LootSpawnEvent;
import com.imit.cosma.model.board.event.SpaceshipAttackBoardEvent;
import com.imit.cosma.model.board.event.SpaceshipMovementBoardEvent;
import com.imit.cosma.model.board.event.SpaceDebrisAttackEvent;
import com.imit.cosma.model.board.event.SpaceshipPicksLootBoardEvent;
import com.imit.cosma.model.board.weather.SpaceDebris;
import com.imit.cosma.model.board.weather.SpaceWeather;
import com.imit.cosma.model.rules.Attack;
import com.imit.cosma.model.rules.Direction;
import com.imit.cosma.model.rules.move.MoveType;
import com.imit.cosma.model.rules.side.EnemySide;
import com.imit.cosma.model.rules.side.NeutralSide;
import com.imit.cosma.model.rules.side.PlayerSide;
import com.imit.cosma.model.rules.side.Side;
import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.model.spaceship.ShipRandomizer;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.model.spaceship.SpaceshipBuilder;
import com.imit.cosma.pkg.random.Randomizer;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.PingPongList;
import com.imit.cosma.util.IntegerPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

    private Set<IntegerPoint> availableForMove;
    private Map<IntegerPoint, Boolean> availableForAttack;

    private final Side playerSide;
    private final Side enemySide;
    private final PingPongList<Side> sides;

    public Board() {
        cells = new Cell[Config.getInstance().BOARD_SIZE][Config.getInstance().BOARD_SIZE];
        emptySet = new HashSet<>();
        selected = new Cell(Config.getInstance().SPACE);
        interacted = new Cell(Config.getInstance().SPACE);

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
            for (int x = 0; x < Config.getInstance().BOARD_SIZE; x++) {
                Spaceship spaceship = spaceshipBuilder.setSide(playerSide)
                        .addSkeleton()
                        .addWeapon(ShipRandomizer.getRandomAmount())
                        .setMoveType(MoveType.QUEEN).build();
                cells[y][x] = new Cell(spaceship);
                objectController.addSpaceship(x, y);
            }
        }

        //initialise space cells
        for (int y = 1; y < Config.getInstance().BOARD_SIZE - 1; y++) {
            for (int x = 0; x < Config.getInstance().BOARD_SIZE; x++) {
                cells[y][x] = new Cell();
                objectController.addSpace(x, y);
            }
        }

        //initialise enemy ships
        for (int y = Config.getInstance().BOARD_SIZE - 1; y < Config.getInstance().BOARD_SIZE; y++) {
            for (int x = 0; x < Config.getInstance().BOARD_SIZE; x++) {
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
        availableForAttack = new HashMap<>();
    }

    public void initAI(){
        enemy = new AI(this);
    }

    public BoardEvent getCurrentEvent(IntegerPoint selected) {
        if(inBoard(selected)){
            if (turn.isPlayer()) {
                return getCurrentPlayerEvent(selected);
            } else if (turn instanceof EnemySide) {
                return getCurrentEnemyEvent();
            } else {
                return getCurrentBoardEvent();
            }
        }
        return new IdleBoardEvent();
    }

    public BoardEvent getCurrentPlayerEvent(IntegerPoint targetPoint){
        currentPath = new Path(this.selectedPoint, targetPoint);

        if(selected.containsShip() && selected.getStepMode() != StepMode.COMPLETED && selected.getSide() == turn) {
            if (selectedCanMoveTo(targetPoint)) {
                turn.scoreMove();
                enemy.savePlayerTurn(currentPath, StepMode.MOVE);

                if (isSupplyKit(targetPoint)) {
                    return getCurrentSupplyKitEvent(targetPoint);
                }

                setSelectedShipPosition(targetPoint);
                setSelected(targetPoint);
                return new SpaceshipMovementBoardEvent(turn.isPlayer() ? getCell(targetPoint) : selected, currentPath);
            } else if (selectedCanFireTo(targetPoint)) {
                damageShip(targetPoint, selected.getDamagePoints());

                turn.scoreMove();
                enemy.savePlayerTurn(currentPath, StepMode.ATTACK);
                return new SpaceshipAttackBoardEvent(selected, interacted, currentPath);
            }
        }
        setSelected(targetPoint);
        return new IdleBoardEvent();
    }

    private boolean isSupplyKit(IntegerPoint target) {
        return inBoard(target) && getCell(target).containsPickableContent();
    }

    private BoardEvent getCurrentSupplyKitEvent(IntegerPoint targetPoint) {
        Cell lootCell = new Cell(getCell(targetPoint).getContent().clone());

        if (getCell(targetPoint).getDamagePoints() < 0) {
            damageShip(selectedPoint, getCell(targetPoint).getDamagePoints());
        } else {
            double damageBonus = ((DamageKit)getCell(targetPoint).getContent()).getDamageBonus();
            ((Spaceship)getCell(selectedPoint).getContent()).setDamageBonus(damageBonus);
        }

        setSelectedShipPosition(targetPoint);
        setSelected(targetPoint);

        return new SpaceshipPicksLootBoardEvent(getCell(targetPoint), lootCell, currentPath);
    }

    public Path getCurrentPath() {
        return currentPath;
    }

    public BoardEvent getCurrentEnemyEvent(){
        enemy.update(this);
        currentPath = enemy.getPath();
        IntegerPoint source = currentPath.getSource();
        IntegerPoint target = currentPath.getTarget();

        setSelectedEnemyTurn(source);

        if (isShip(target)) {
            selected.setStepMode(StepMode.ATTACK);
        }

        if (selected.containsShip() && selected.getStepMode() != StepMode.COMPLETED && selected.getSide().equals(turn)) {
            if (selectedCanMoveTo(target)) {
                setSelectedShipPosition(target);
                turn.scoreMove();

                setSelectedEnemyTurn(target);

                return new SpaceshipMovementBoardEvent(selected, currentPath);
            } else if (selectedCanFireTo(target)) {
                damageShip(target, selected.getDamagePoints());
                turn.scoreMove();

                return new SpaceshipAttackBoardEvent(selected, interacted, currentPath);
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

    private boolean sideCompletedTurn() {//TODO bug
        return turn.completedTurn();
    }

    public boolean isShip(IntegerPoint target) {
        return isShip(target.x, target.y);
    }

    public boolean isShip(int x, int y) {
        return cells[y][x].containsShip();
    }

    public boolean isShipSelected() {
        return selected.containsShip();
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

    public Cell getSelected() {
        return selected;
    }

    public boolean selectedCanMoveTo(IntegerPoint target){
        return selected.containsShip()
                && cells[target.y][target.x] != selected
                && availableForMove.contains(target)
                && selected.getStepMode() == StepMode.MOVE && isPassable(target);
    }

    public boolean selectedCanFireTo(IntegerPoint target){
        return selected != null && isShipSelected() && isShip(target)
                && selected.getSide() != cells[target.y][target.x].getSide()
                && selected.getStepMode() == StepMode.ATTACK
                && availableForAttack.get(target) != null;
    }

    public Cell getCell(IntegerPoint target){
        return getCell(target.x, target.y);
    }

    public Cell getCell(int x, int y) {
        return cells[y][x];
    }

    public Set<IntegerPoint> getAvailableCellsForMove() {
        return selected.containsShip() && selected.getStepMode() == StepMode.MOVE && selected.getSide() == turn
                ? availableForMove : emptySet;
    }

    public Set<IntegerPoint> getAvailableCellsForMove(int x, int y) {
        return isShip(x, y) && cells[y][x].getStepMode() == StepMode.MOVE
                ? getAvailableForMove(new IntegerPoint(x, y))
                : emptySet;
    }

    public Map<IntegerPoint, Boolean> getAvailableCellsForFire(){
        return selected.containsShip() && selected.getStepMode() == StepMode.ATTACK
                ? availableForAttack
                : new HashMap<IntegerPoint, Boolean>();
    }

    public Map<IntegerPoint, Boolean> getAvailableCellsForFire(int x, int y) {
        return isShip(x, y) && cells[y][x].getStepMode() == StepMode.ATTACK
                ? getAvailableForAttack(new IntegerPoint(x, y))
                : new HashMap<IntegerPoint, Boolean>();
    }

    public List<IntegerPoint> getNonEmptyLocations() {
        return objectController.getNonEmptyLocations();
    }

    private void setSelectedShipPosition(IntegerPoint destination) {
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

        if(selected.containsShip()) {
            availableForAttack = selected.getStepMode() == StepMode.ATTACK
                    ? getAvailableForAttack(selectedPoint)
                    : new HashMap<IntegerPoint, Boolean>();
            availableForMove = selected.getStepMode() == StepMode.MOVE
                    ? getAvailableForMove(selectedPoint)
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

    public BoardEvent getCurrentBoardEvent() {
        if (Math.random() < Config.getInstance().SPACE_DEBRIS_SPAWN_CHANCE) {
            return getSpaceDebrisSpawnEvent();
        }

        if (Math.random() < Config.getInstance().BLACK_HOLE_SPAWN_CHANCE) {
            return getSpawnBlackHoleEvent();
        }

        if (Math.random() < Config.getInstance().LOOT_SPAWN_CHANCE) {
            return getLootSpawnEvent();
        }

        turn.scoreMove();
        return new IdleBoardEvent();
    }

    private BoardEvent getSpawnBlackHoleEvent() {
        turn.scoreMove();

        currentContentSpawnPoint = Randomizer.generatePoint(0, Config.getInstance().BOARD_SIZE - 1);
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

    private BoardEvent getSpaceDebrisSpawnEvent() {
        turn.scoreMove();

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

    private BoardEvent getLootSpawnEvent() {
        turn.scoreMove();

        currentContentSpawnPoint = Randomizer.getRandom(objectController.getSpaceLocations());

        int randomValue = (int) (Math.random() * 2);
        
        Loot loot = randomValue == 0 ? new HealthKit() : new DamageKit();
        Cell lootCell = new Cell(loot);
        setCell(currentContentSpawnPoint, lootCell);

        return new LootSpawnEvent(lootCell, currentContentSpawnPoint);
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

    private Set<IntegerPoint> getAvailableForMove(IntegerPoint target) {
        Set<IntegerPoint> availableForMove = new HashSet<>();
        MoveType moveType = getCell(target).getMoveType();

        IntegerPoint offset = new IntegerPoint(target);
        for (Direction direction : moveType.getDirections()) {
            do {
                offset.move(direction);

                if (inBoard(offset) && isPassable(offset)) {
                    availableForMove.add(new IntegerPoint(offset));
                }

            } while (moveType.isEndless() && inBoard(offset) && isPassable(offset));

            offset.set(target);
        }

        return availableForMove;
    }

    private Map<IntegerPoint, Boolean> getAvailableForAttack(IntegerPoint target) {
        Map<IntegerPoint, Boolean> availableForAttack = new HashMap<>();
        int firingRadius = isShip(target) ? ((Spaceship) getCell(target).getContent()).getFiringRadius() : 0;

        IntegerPoint offset = new IntegerPoint(target);

        for (Direction direction : Direction.getStraight()) {
            for (int i = 0; i < firingRadius; i++) {
                offset.move(direction);
                if (inBoard(offset)) {
                    availableForAttack.put(new IntegerPoint(offset), isEnemyShip(offset));
                }
            }
            offset.set(target);
        }

        if (firingRadius > 1) {
            for (Direction direction : Direction.getDiagonal()) {
                offset.move(direction);
                if (inBoard(offset)) {
                    availableForAttack.put(new IntegerPoint(offset), isEnemyShip(offset));
                }
                offset.set(target);
            }
        }

        if (firingRadius > 2) {
            for (Direction direction : Direction.getHorseDirections()) {
                offset.move(direction);
                if (inBoard(offset)) {
                    availableForAttack.put(new IntegerPoint(offset), isEnemyShip(offset));
                }
                offset.set(target);
            }
        }

        return availableForAttack;
    }
}