package com.imit.cosma.model.board;

import static com.imit.cosma.config.Config.getInstance;

import com.imit.cosma.ai.BotPlayer;
import com.imit.cosma.config.Config;
import com.imit.cosma.model.board.content.DamageKit;
import com.imit.cosma.model.board.content.GameObject;
import com.imit.cosma.model.board.content.HealthKit;
import com.imit.cosma.model.board.content.Loot;
import com.imit.cosma.model.board.event.GameObjectSpawnEvent;
import com.imit.cosma.model.board.event.BoardEvent;
import com.imit.cosma.model.board.event.GameObjectsDespawnEvent;
import com.imit.cosma.model.board.event.IdleBoardEvent;
import com.imit.cosma.model.board.event.SpaceshipAttackBoardEvent;
import com.imit.cosma.model.board.event.SpaceshipMovementBoardEvent;
import com.imit.cosma.model.board.weather.SpaceDebris;
import com.imit.cosma.model.board.weather.SpaceWeather;
import com.imit.cosma.model.rules.Direction;
import com.imit.cosma.model.rules.move.MoveType;
import com.imit.cosma.model.rules.side.ClearingSide;
import com.imit.cosma.model.rules.side.EnemySide;
import com.imit.cosma.model.rules.side.NeutralSide;
import com.imit.cosma.model.rules.side.PlayerSide;
import com.imit.cosma.model.rules.side.Side;
import com.imit.cosma.model.rules.TurnType;
import com.imit.cosma.model.spaceship.ShipRandomizer;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.model.spaceship.SpaceshipBuilder;
import com.imit.cosma.pkg.random.Randomizer;
import com.imit.cosma.util.MutualLinkedMap;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.CycledList;
import com.imit.cosma.util.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Board {
    private final Set<Point<Integer>> emptySet;
    private final ObjectController objectController;
    private final Cell[][] cells;
    private Cell selected;
    private final Cell interacted;
    private final Set<Point<Integer>> interactedCells;
    private Side turn;
    private final Point<Integer> selectedPoint;
    private BotPlayer enemyBotPlayer;
    private Path<Integer> currentPath;
    private Point<Integer> currentContentSpawnPoint;
    private Set<Point<Integer>> availableForMove;
    private Map<Point<Integer>, Boolean> availableForAttack;
    private final Side playerSide;
    private final Side enemySide;
    private final CycledList<Side> sides;

    public Board() {
        cells = new Cell[getInstance().BOARD_SIZE][getInstance().BOARD_SIZE];
        emptySet = new HashSet<>();
        selected = new Cell(getInstance().SPACE);
        interacted = new Cell(getInstance().SPACE);

        objectController = new ObjectController();

        SpaceshipBuilder spaceshipBuilder = new SpaceshipBuilder();

        playerSide = new PlayerSide();
        enemySide = new EnemySide();

        sides = new CycledList<>();
        sides.add(enemySide);
        sides.add(new NeutralSide());
        sides.add(new ClearingSide());
        sides.add(playerSide);

        selectedPoint = new Point<>();
/*
        //initialise space cells
        for (int y = 0; y < Config.getInstance().BOARD_SIZE; y++) {
            for (int x = 0; x < Config.getInstance().BOARD_SIZE; x++) {
                cells[y][x] = new Cell();
                Point<Integer> currentLocation = new Point<>(x, y);
                if (playerShipLocation.equals(currentLocation) || enemyShipLocation.equals(currentLocation)) {
                    Spaceship spaceship = spaceshipBuilder.setSide(playerShipLocation.equals(currentLocation) ? playerSide : enemySide)
                            .addSkeleton(Skeleton.DREADNOUGHT)
                            .addWeapon(1)
                            .setMoveType(MoveType.WEAK_ROOK).build();
                    cells[y][x].setContent(spaceship);
                    objectController.addSpaceship(currentLocation);
                } else {
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
                objectController.addSpaceship(spaceship, x, y);
            }
        }

        //initialise space cells
        for (int y = 1; y < Config.getInstance().BOARD_SIZE - 1; y++) {
            for (int x = 0; x < Config.getInstance().BOARD_SIZE; x++) {
                cells[y][x] = new Cell();
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
                objectController.addSpaceship(spaceship, x, y);
            }
        }

        turn = playerSide;
        interactedCells = new HashSet<>();

        availableForMove = new HashSet<>();
        availableForAttack = new HashMap<>();
    }

    public void initAI(){
        enemyBotPlayer = new BotPlayer(this);
    }

    public BoardEvent getCurrentEvent(Point<Integer> selected) {
        if(inBoard(selected)){
            if (turn.isPlayer()) {
                return getCurrentPlayerEvent(selected);
            } else if (turn.isPlayingSide()) {
                return getCurrentEnemyEvent();
            } else if (turn instanceof NeutralSide){
                return getCurrentBoardEvent();
            } else {
                return getBoardClearingEvent();
            }
        }
        return new IdleBoardEvent();
    }

    public BoardEvent getCurrentPlayerEvent(Point<Integer> targetPoint){
        currentPath = new Path<>(this.selectedPoint, targetPoint);

        if(selected.containsShip() && selected.getSide().equals(turn)) {
            if (selectedCanMoveTo(targetPoint)) {
                enemyBotPlayer.savePlayerTurn(currentPath, TurnType.MOVE);

                if (isSupplyKit(targetPoint)) {
                    return getCurrentSupplyKitEvent(targetPoint);
                }

                setSelectedShipPosition(targetPoint);
                setSelected(targetPoint);
                return new SpaceshipMovementBoardEvent(turn.isPlayer() ? getCell(targetPoint) : selected, getCell(targetPoint), currentPath);
            } else if (selectedCanFireTo(targetPoint)) {
                damageShip(targetPoint, selected.getDamagePoints());

                enemyBotPlayer.savePlayerTurn(currentPath, TurnType.ATTACK);
                return new SpaceshipAttackBoardEvent(selected, interacted, currentPath);
            }
        }
        setSelected(targetPoint);
        return new IdleBoardEvent();
    }

    private boolean isSupplyKit(Point<Integer> target) {
        return inBoard(target) && getCell(target).containsPickableContent();
    }

    private BoardEvent getCurrentSupplyKitEvent(Point<Integer> targetPoint) {
        Cell lootCell = new Cell(getCell(targetPoint).getContent().clone());

        if (getCell(targetPoint).getDamagePoints() < 0) {
            damageShip(selectedPoint, getCell(targetPoint).getDamagePoints());
        } else {
            double damageBonus = ((DamageKit)getCell(targetPoint).getContent()).getDamageBonus();
            ((Spaceship)getCell(selectedPoint).getContent()).setDamageBonus(damageBonus);
        }

        setSelectedShipPosition(targetPoint);
        setSelected(targetPoint);

        return new SpaceshipMovementBoardEvent(getCell(targetPoint), lootCell, currentPath);
    }

    public BoardEvent getCurrentEnemyEvent(){
        currentPath = enemyBotPlayer.getPath(this);
        Point<Integer> source = currentPath.getSource();
        Point<Integer> targetPoint = currentPath.getTarget();

        setSelectedEnemyTurn(source);

        if (isShip(targetPoint)) {
            selected.setStepMode(TurnType.ATTACK);
        }

        if (selected.containsShip() && selected.getSide().equals(turn)) {
            if (selectedCanMoveTo(targetPoint)) {
                if (isSupplyKit(targetPoint)) {
                    return getCurrentSupplyKitEvent(targetPoint);
                }

                setSelectedShipPosition(targetPoint);
                setSelectedEnemyTurn(targetPoint);
                return new SpaceshipMovementBoardEvent(selected, getCell(targetPoint), currentPath);
            } else if (selectedCanFireTo(targetPoint)) {
                damageShip(targetPoint, selected.getDamagePoints());
                return new SpaceshipAttackBoardEvent(selected, interacted, currentPath);
            }
        }
        return new IdleBoardEvent();
    }

    public void updateSide(){
        turn.addScore(0);
        changeTurn();

        for(Point<Integer> point : interactedCells){
            cells[point.y][point.x].setStepMode(TurnType.MOVE);
        }
        interactedCells.clear();
    }

    public List<Point<Integer>> getExpiredObjects() {
        return new ArrayList<>(objectController.getExpiredGameObjectLocations());
    }

    public boolean isShip(Point<Integer> target) {
        return isShip(target.x, target.y);
    }

    public boolean isShip(int x, int y) {
        return cells[y][x].containsShip();
    }

    public boolean isShipSelected() {
        return selected.containsShip();
    }

    public boolean isObjectSelected() {
        return selected.isGameObject();
    }

    public boolean isEnemyShip(Point<Integer> target){
        return isShip(target)
                && (selected.getSide().isPlayer() && !cells[target.y][target.x].getSide().isPlayer()
                || !selected.getSide().isPlayer() && cells[target.y][target.x].getSide().isPlayer());
    }

    public void damageShip(Point<Integer> target, int damage){
        interacted.setContent(cells[target.y][target.x].getContent().clone());

        cells[target.y][target.x].setDamage(damage);
        interactedCells.add(selectedPoint.clone());
        if(cells[target.y][target.x].getContent().getHealthPoints() <= 0){
            turn.addScore(cells[target.y][target.x].getContent().getMaxHealthPoints());
            destroyShip(target, Cell.initWithSpace());
            objectController.setEmpty(new Point<>(target));
        }
    }

    private void destroyShip(Point<Integer> target, Cell replacement) {
        if (cells[target.y][target.x].getSide().isPlayer()) {
            playerSide.removeShip();
        } else {
            enemySide.removeShip();
        }

        cells[target.y][target.x] = replacement;
    }

    public boolean isPassable(Point<Integer> target) {
        return isPassable(target.x, target.y);
    }

    public boolean isPassable(int x, int y) {
        return inBoard(x, y) && cells[y][x].isPassable();
    }

    public Cell getSelected() {
        return selected;
    }

    public boolean selectedCanMoveTo(Point<Integer> target){
        return selected.containsShip()
                && cells[target.y][target.x] != selected
                && availableForMove.contains(target)
                && selected.getTurnType() == TurnType.MOVE && isPassable(target);
    }

    public boolean selectedCanFireTo(Point<Integer> target){
        return selected != null && isShipSelected() && isShip(target)
                && selected.getSide() != cells[target.y][target.x].getSide()
                && selected.getTurnType() == TurnType.ATTACK
                && availableForAttack.get(target) != null;
    }

    public Cell getCell(Point<Integer> target){
        return getCell(target.x, target.y);
    }

    public Cell getCell(int x, int y) {
        return cells[y][x];
    }

    public Set<Point<Integer>> getAvailableCellsForMove() {
        return selected.containsShip() && selected.getTurnType() == TurnType.MOVE && selected.getSide() == turn
                ? availableForMove : emptySet;
    }

    public Set<Point<Integer>> getAvailableCellsForMove(int x, int y) {
        return isShip(x, y) && cells[y][x].getTurnType() == TurnType.MOVE
                ? getAvailableForMove(new Point<>(x, y))
                : emptySet;
    }

    public Map<Point<Integer>, Boolean> getAvailableCellsForFire(){
        return selected.containsShip() && selected.getTurnType() == TurnType.ATTACK
                ? availableForAttack
                : new HashMap<Point<Integer>, Boolean>();
    }

    public Map<Point<Integer>, Boolean> getAvailableCellsForFire(int x, int y) {
        return isShip(x, y) && cells[y][x].getTurnType() == TurnType.ATTACK
                ? getAvailableForAttack(new Point<>(x, y))
                : new HashMap<Point<Integer>, Boolean>();
    }

    public List<Point<Integer>> getNonEmptyLocations() {
        return objectController.getNonEmptyLocations();
    }

    private void setSelectedShipPosition(Point<Integer> destination) {
        interacted.setContent(cells[destination.y][destination.x].getContent().clone());
        Spaceship spaceship = (Spaceship) getCell(selectedPoint).getContent();
        swapCells(selectedPoint, destination);

        objectController.setSpaceship(spaceship, new Point<>(destination));
        objectController.setEmpty(new Point<>(selectedPoint));

        interactedCells.add(destination.clone());
    }

    public String getIdleAnimationPath(int x, int y){
        return cells[y][x].getIdleAnimationPath();
    }

    public String getIdleAnimationPath(Point<Integer> target) {
        return getIdleAnimationPath(target.x, target.y);
    }

    public boolean inBoard(Point<Integer> target){
        return inBoard(target.x, target.y);
    }

    public boolean inBoard(int x, int y){
        return x >=0 && x < 8 && y >= 0 && y < 8;
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

    public float getDefaultRotation(Point<Integer> target) {
        return getDefaultRotation(target.x, target.y);
    }

    public void setSelectedEnemyTurn(Point<Integer> target) {
        if(inBoard(target) && !turn.isPlayer()) {
            setSelected(target);
        }
    }

    private void setSelected(Point<Integer> target) {
        selected = cells[target.y][target.x];
        selectedPoint.set(target);

        if(selected.containsShip()) {
            availableForAttack = getAvailableForAttack(selectedPoint);
            availableForMove = getAvailableForMove(selectedPoint);
        }
    }

    private void swapCells(Point<Integer> a, Point<Integer> b) {
        cells[a.y][a.x].swapContents(cells[b.y][b.x]);
    }

    public TurnType getStepMode(int x, int y) {
        return cells[y][x].getTurnType();
    }

    public int getMaxHealthPoints(int x, int y) {
        return cells[y][x].getMaxHealthPoints();
    }

    public int getHealthPoints(Point<Integer> target){
        return getHealthPoints(target.x, target.y);
    }

    public int getHealthPoints(int x, int y) {
        return cells[y][x].getHealthPoints();
    }

    public int getDamagePoints(int x, int y) {
        return cells[y][x].getDamagePoints();
    }

    public BoardEvent getCurrentBoardEvent() {
        //if (Math.random() < getInstance().SPACE_DEBRIS_SPAWN_CHANCE) {
          //  return getSpaceDebrisSpawnEvent();
        //}
        if (0 < getInstance().BLACK_HOLE_SPAWN_CHANCE) {
            return getSpawnBlackHoleEvent();
        }

        //if (Math.random() < getInstance().LOOT_SPAWN_CHANCE) {
          //  return getLootSpawnEvent();
        //}
        updateSide();
        return new IdleBoardEvent();
    }

    public BoardEvent getBoardClearingEvent() {
        objectController.updateLiveTime();
        MutualLinkedMap<GameObject, Point<Integer>> objectToLocationMap = new MutualLinkedMap<>();
        for (Point<Integer> objectLocation : getExpiredObjects()) {
            objectToLocationMap.put((GameObject) getCell(objectLocation).getContent(), objectLocation);
            setCell(objectLocation, Cell.initWithSpace());
        }
        objectController.clearExpiredGameObjects();
        return new GameObjectsDespawnEvent(objectToLocationMap);
    }

    private BoardEvent getSpawnBlackHoleEvent() {
        currentContentSpawnPoint = Randomizer.generatePoint(0, getInstance().BOARD_SIZE - 1);

        Point<Integer> blackHoleSpawnPoint = currentContentSpawnPoint.clone();
        Cell cellWithBlackHole = Cell.initWithBlackHole();
        objectController.addGameObject((GameObject) cellWithBlackHole.getContent(), currentContentSpawnPoint);

        currentPath = new Path<>(blackHoleSpawnPoint, blackHoleSpawnPoint);
        if (isShip(blackHoleSpawnPoint)) {
            Spaceship victimSpaceship = (Spaceship) cells[blackHoleSpawnPoint.y][blackHoleSpawnPoint.x].getContent();
            if (victimSpaceship.getSide().isPlayer()) {
                enemySide.addScore(victimSpaceship.getDamagePoints());
            } else {
                playerSide.addScore(victimSpaceship.getDamagePoints());
            }
            destroyShip(blackHoleSpawnPoint, cellWithBlackHole);
            setCell(blackHoleSpawnPoint, cellWithBlackHole);

            return new GameObjectSpawnEvent((GameObject) cellWithBlackHole.getContent(), blackHoleSpawnPoint, victimSpaceship);
        } else {
            setCell(blackHoleSpawnPoint, cellWithBlackHole);

            return new GameObjectSpawnEvent((GameObject) cellWithBlackHole.getContent(), blackHoleSpawnPoint);
        }
    }

    private BoardEvent getSpaceDebrisSpawnEvent() {
        List<Point<Integer>> targets = new ArrayList<>();
        List<Integer> damages = new ArrayList<>();
        List<Spaceship> spaceships = new ArrayList<>();

        SpaceWeather debris = new SpaceDebris();

        List<Point<Integer>> spaceshipLocations = new ArrayList<>(objectController.getSpaceshipsLocations());

        int piecesNumber = Math.min(spaceshipLocations.size(), debris.getPiecesNumber());

        for (int i = 0; i < piecesNumber; i++) {
            Point<Integer> target = Randomizer.getRandom(spaceshipLocations);
            int damage = debris.generateDamage();

            targets.add(target);
            damages.add(damage);
            spaceships.add((Spaceship) getCell(target).getContent().clone());

            damageShip(target, damage);

            spaceshipLocations.remove(target);
        }

        return null;
    }

    private BoardEvent getLootSpawnEvent() {
        currentContentSpawnPoint = Randomizer.getRandom(objectController.getSpaceLocations());

        int randomValue = (int) (Math.random() * 2);
        
        //Loot loot = randomValue == 0 ? new HealthKit() : new DamageKit();
        Loot loot = new HealthKit();
        Cell lootCell = new Cell(loot);
        setCell(currentContentSpawnPoint, lootCell);
        objectController.setGameObject(loot, currentContentSpawnPoint);

        return new GameObjectSpawnEvent(loot, currentContentSpawnPoint);
    }

    private void setCell(Point<Integer> target, Cell newCell) {
        cells[target.y][target.x] = newCell;
    }

    private void changeTurn() {
        turn = sides.next();
    }

    public boolean isGameOver() {
        return playerSide.getShipsNumber() == 0 || enemySide.getShipsNumber() == 0;
    }

    private Set<Point<Integer>> getAvailableForMove(Point<Integer> target) {
        Set<Point<Integer>> availableForMove = new HashSet<>();
        MoveType moveType = getCell(target).getMoveType();

        Point<Integer> offset = new Point<>(target);
        for (Direction direction : moveType.getDirections()) {
            do {
                offset.set(offset.x + direction.getOffsetX(), offset.y + direction.getOffsetY());

                if (inBoard(offset) && isPassable(offset)) {
                    availableForMove.add(new Point<>(offset));
                }

            } while (moveType.isEndless() && inBoard(offset) && isPassable(offset));

            offset.set(target);
        }

        return availableForMove;
    }

    private Map<Point<Integer>, Boolean> getAvailableForAttack(Point<Integer> target) {
        Map<Point<Integer>, Boolean> availableForAttack = new HashMap<>();
        int firingRadius = isShip(target) ? ((Spaceship) getCell(target).getContent()).getFiringRadius() : 0;

        Point<Integer> offset = new Point<>(target);

        for (Direction direction : Direction.getStraight()) {
            for (int i = 0; i < firingRadius; i++) {
                offset.set(offset.x + direction.getOffsetX(), offset.y + direction.getOffsetY());
                if (inBoard(offset)) {
                    availableForAttack.put(new Point<>(offset), isEnemyShip(offset));
                }
            }
            offset.set(target);
        }

        if (firingRadius > 1) {
            for (Direction direction : Direction.getDiagonal()) {
                offset.set(offset.x + direction.getOffsetX(), offset.y + direction.getOffsetY());
                if (inBoard(offset)) {
                    availableForAttack.put(new Point<>(offset), isEnemyShip(offset));
                }
                offset.set(target);
            }
        }

        if (firingRadius > 2) {
            for (Direction direction : Direction.getHorseDirections()) {
                offset.set(offset.x + direction.getOffsetX(), offset.y + direction.getOffsetY());
                if (inBoard(offset)) {
                    availableForAttack.put(new Point<>(offset), isEnemyShip(offset));
                }
                offset.set(target);
            }
        }

        return availableForAttack;
    }

    public boolean selectedInAttackMode() {
        return selected.getContent().getTurnType() == TurnType.ATTACK;
    }

    public int getCurrentPlayerScore() {
        return turn.getScore();
    }

    public int getEnemySideScore() {
        return enemySide.getScore();
    }

    public int getPlayerSideScore() {
        return playerSide.getScore();
    }
}