package com.imit.cosma.ai;

import com.imit.cosma.config.Config;
import com.imit.cosma.model.board.Board;
import com.imit.cosma.model.rules.Direction;
import com.imit.cosma.model.rules.TurnType;
import com.imit.cosma.model.rules.move.MoveType;
import com.imit.cosma.model.rules.side.EnemySide;
import com.imit.cosma.model.rules.side.NeutralSide;
import com.imit.cosma.model.rules.side.PlayerSide;
import com.imit.cosma.model.rules.side.Side;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class ArtificialBoard implements Cloneable {
    private ArtificialSpaceshipController controller;

    private final Side[][] sidesField;
    private final TurnType[][] turnTypeField;
    private final MoveType[][] moveTypeField;
    private final int[][] healthField, damageField, firingRadiusField, maxHealthField;
    private final boolean[][] obstaclesField;

    private Side turn;
    private final Side playerSide;
    private final Side enemySide;

    public ArtificialBoard() {
        controller = new ArtificialSpaceshipController();
        int size = Config.getInstance().BOARD_SIZE;
        sidesField = new Side[size][size];
        healthField = new int[size][size];
        damageField = new int[size][size];
        turnTypeField = new TurnType[size][size];
        firingRadiusField = new int[size][size];
        moveTypeField = new MoveType[size][size];
        obstaclesField = new boolean[size][size];
        maxHealthField = new int[size][size];

        playerSide = new PlayerSide(Config.getInstance().DEFAULT_SHIPS_NUMBER);
        enemySide = new EnemySide(Config.getInstance().DEFAULT_SHIPS_NUMBER);
    }

    public ArtificialBoard(Board board) {
        this();
        turn = board.getTurn().clone();

        update(board);
    }

    public void update(Board board) {
        controller.clear();
        int size = Config.getInstance().BOARD_SIZE;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                sidesField[y][x] = board.getSide(x, y);
                if (sidesField[y][x].isPlayer()) {
                    controller.addPlayerShip(new Point<>(x, y));
                } else if (sidesField[y][x].isPlayingSide()) {
                    controller.addEnemyShip(new Point<>(x, y));
                }

                healthField[y][x] = board.getHealthPoints(x, y);
                damageField[y][x] = board.getDamagePoints(x, y);
                turnTypeField[y][x] = board.getStepMode(x, y);
                firingRadiusField[y][x] = board.isShip(x, y) ?
                        ((Spaceship) (board.getCell(x, y).getContent())).getFiringRadius() : 0;
                moveTypeField[y][x] = board.getCell(x, y).getContent().getMoveType();
                obstaclesField[y][x] = board.isPassable(x, y);
                maxHealthField[y][x] = board.getMaxHealthPoints(x, y);
            }
        }

        turn = board.getTurn().clone();
    }

    public boolean isShip(int x, int y) {
        return !(sidesField[y][x] instanceof NeutralSide);
    }

    public boolean isShip(Point<Integer> target) {
        return isShip(target.x, target.y);
    }

    public Side getSide(int x, int y) {
        return sidesField[y][x];
    }

    public void updateSide() {
        turn = turn.isPlayer() ? enemySide : playerSide;
    }

    public TurnType doTurn(Path<Integer> path){
        TurnType mode = TurnType.UNDEFINED;

        Point<Integer> source = path.getSource();
        Point<Integer> target = path.getTarget();

        if(spaceshipCanMoveTo(source, target)){
            if (turn.isPlayer()) {
                controller.changePlayerShipLocation(path);
            } else if (turn.isPlayingSide()) {
                controller.changeEnemyShipLocation(path);
            }

            setSpaceshipPosition(source, target);
            updateBlockedShips(target);
            updateSide();
            mode = TurnType.MOVE;
        }
        else if(spaceshipCanFireTo(source, target)){
            damageShip(target, damageField[target.y][target.x]);
            updateSide();
            mode = TurnType.ATTACK;
        }

        return mode;
    }

    private void updateBlockedShips(Point<Integer> target) {
        Point<Integer> offset = new Point<>(target);
        List<Point<Integer>> unblockedShipLocations = new ArrayList<>();
        for (Point<Integer> blockedShipLocation : controller.getBlockedShipLocations()) {
            if (!isShipBlocked(blockedShipLocation)) {
                unblockedShipLocations.add(blockedShipLocation);
            }
        }
        for (Point<Integer> unblockedShipLocation : unblockedShipLocations) {
            controller.removeBlockedShip(unblockedShipLocation);
        }
        for (Direction direction : Direction.getStraightAndDiagonal()) {
            offset.set(offset.x + direction.getOffsetX(), offset.y + direction.getOffsetY());
            if (inBoard(offset) && isShip(offset) && isShipBlocked(offset)) {
                controller.addBlockedShip(new Point<Integer>(offset));
            }
            offset.set(target);
        }
    }

    private boolean isShipBlocked(Point<Integer> shipLocation) {
        MoveType moveType = moveTypeField[shipLocation.y][shipLocation.x];
        Point<Integer> offset = new Point<>(shipLocation);
        for (Direction direction : moveType.getDirections()) {
            offset.set(offset.x + direction.getOffsetX(), offset.y + direction.getOffsetY());
            if (inBoard(offset) && (!isShip(offset) || !isSameSide(shipLocation, offset))) {
                return false;
            }
            offset.set(shipLocation);
        }
        return true;
    }

    private boolean isSameSide(Point<Integer> ship1, Point<Integer> ship2) {
        return sidesField[ship1.y][ship1.x].isPlayer() && sidesField[ship2.y][ship2.x].isPlayer() ||
                sidesField[ship1.y][ship1.x].isPlayingSide() && sidesField[ship2.y][ship2.x].isPlayingSide();
    }

    private void damageShip(Point<Integer> target, int damage) {
        healthField[target.y][target.x] -= damage;

        if(healthField[target.y][target.x] <= 0){
            destroyShip(target);
        }
    }

    private void destroyShip(Point<Integer> target) {
        if(!sidesField[target.y][target.x].isPlayer()) {
            controller.removeEnemyShip(target);
            enemySide.removeShip();
        }
        else {
            controller.removePlayerShip(target);
            playerSide.removeShip();
        }

        healthField[target.y][target.x] = 0;
        damageField[target.y][target.x] = 0;
        obstaclesField[target.y][target.x] = true;
        sidesField[target.y][target.x] = new NeutralSide();
        maxHealthField[target.y][target.x] = 0;
        firingRadiusField[target.y][target.x] = 0;
        moveTypeField[target.y][target.x] = MoveType.IDLE;
    }

    private void setSpaceshipPosition(Point<Integer> source, Point<Integer> target) {
        Side sideTemp = sidesField[target.y][target.x];
        sidesField[target.y][target.x] = sidesField[source.y][source.x];
        sidesField[source.y][source.x] = sideTemp;

        int valueTemp = healthField[target.y][target.x];
        healthField[target.y][target.x] = healthField[source.y][source.x];
        healthField[source.y][source.x] = valueTemp;

        valueTemp = damageField[target.y][target.x];
        damageField[target.y][target.x] = damageField[source.y][source.x];
        damageField[source.y][source.x] = valueTemp;

        valueTemp = firingRadiusField[target.y][target.x];
        firingRadiusField[target.y][target.x] = firingRadiusField[source.y][source.x];
        firingRadiusField[source.y][source.x] = valueTemp;

        valueTemp = maxHealthField[target.y][target.x];
        maxHealthField[target.y][target.x] = maxHealthField[source.y][source.x];
        maxHealthField[source.y][source.x] = valueTemp;

        TurnType turnTypeTemp = turnTypeField[target.y][target.x];
        turnTypeField[target.y][target.x] = turnTypeField[source.y][source.x];
        turnTypeField[source.y][source.x] = turnTypeTemp;

        MoveType moveTypeTemp = moveTypeField[target.y][target.x];
        moveTypeField[target.y][target.x] = moveTypeField[source.y][source.x];
        moveTypeField[source.y][source.x] = moveTypeTemp;

        boolean obstacleTemp = obstaclesField[target.y][target.x];
        obstaclesField[target.y][target.x] = obstaclesField[source.y][source.x];
        obstaclesField[source.y][source.x] = obstacleTemp;

        turnTypeField[target.y][target.x] = TurnType.ATTACK;
    }

    public boolean spaceshipCanMoveTo(Point<Integer> source, Point<Integer> target){
        return isShip(source)
                && !target.equals(source)
                && isPassable(target);
    }

    public boolean spaceshipCanFireTo(Point<Integer> source, Point<Integer> target){
        return isShip(source) && isShip(target)
                && sidesField[source.y][source.x] != sidesField[target.y][target.x];
    }

    public Set<Point<Integer>> getAvailableCells(Point<Integer> target) {
        Set<Point<Integer>> availableForMove = getAvailableForMove(target);
        Set<Point<Integer>> availableForAttack = getAvailableForAttack(target);
        availableForAttack.addAll(availableForMove);
        return availableForAttack;
    }

    public Set<Point<Integer>> getAvailableCells(int x, int y) {
        return getAvailableCells(new Point<>(x, y));
    }

    public boolean inBoard(Point<Integer> target){
        return inBoard(target.x, target.y);
    }

    public boolean inBoard(int x, int y){
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    public boolean isEnemyShip(Point<Integer> source, Point<Integer> target) {
        return isShip(target.x, target.y)
                && (sidesField[source.y][source.x].isPlayer() && !sidesField[target.y][target.x].isPlayer()
                || !sidesField[source.y][source.x].isPlayer() && sidesField[target.y][target.x].isPlayer());
    }

    public boolean isPassable(Point<Integer> target) {
        return isPassable(target.x, target.y);
    }

    public boolean isPassable(int x, int y) {
        return inBoard(x, y) && obstaclesField[y][x];
    }

    public ArtificialBoard clone() {
        int size = Config.getInstance().BOARD_SIZE;
        ArtificialBoard board = new ArtificialBoard();

        for (int y = 0; y < size; y++) {
            for(int x = 0; x < size; x++) {
                board.sidesField[y][x] = sidesField[y][x].clone();
                board.healthField[y][x] = healthField[y][x];
                board.damageField[y][x] = damageField[y][x];
                board.turnTypeField[y][x] = turnTypeField[y][x];
                board.moveTypeField[y][x] = moveTypeField[y][x];
                board.firingRadiusField[y][x] = firingRadiusField[y][x];
                board.obstaclesField[y][x] = obstaclesField[y][x];
                board.maxHealthField[y][x] = maxHealthField[y][x];
            }
        }

        board.turn = turn.clone();
        board.controller = new ArtificialSpaceshipController(controller);

        return board;
    }

    public int getHealthPoints(Point<Integer> target) {
        return healthField[target.y][target.x];
    }

    public int getDamagePoints(Point<Integer> target) {
        return damageField[target.y][target.x];
    }

    public List<Point<Integer>> getAvailablePlayerShipLocations() {
        List<Point<Integer>> availableShips = new ArrayList<>(controller.getPlayerShipLocations());
        availableShips.removeAll(controller.getBlockedShipLocations());
        return availableShips;
    }

    public List<Point<Integer>> getAvailableEnemyShipLocations() {
        List<Point<Integer>> availableShips = new ArrayList<>(controller.getEnemyShipLocations());
        availableShips.removeAll(controller.getBlockedShipLocations());
        return availableShips;
    }

    public Side getTurn() {
        return turn;
    }

    public Set<Point<Integer>> getAvailableForMove(Point<Integer> target) {
        Set<Point<Integer>> availableForMove = new HashSet<>();
        MoveType moveType = moveTypeField[target.y][target.x];

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

    public Set<Point<Integer>> getAvailableForAttack(Point<Integer> target) {
        Set<Point<Integer>> availableForAttack = new HashSet<>();
        int firingRadius = firingRadiusField[target.y][target.x];

        Point<Integer> offset = new Point<>(target);
        List<Direction> unpassableContentDirections = new ArrayList<>();

        for (Direction direction : Direction.getStraight()) {
            for (int i = 0; i < firingRadius; i++) {
                offset.set(offset.x + direction.getOffsetX(), offset.y + direction.getOffsetY());
                if (inBoard(offset) && isEnemyShip(target, offset)) {
                    unpassableContentDirections.add(direction);
                    availableForAttack.add(new Point<>(offset));
                }
            }
            offset.set(target);
        }

        if (firingRadius > 1) {
            for (Direction direction : Direction.getDiagonal()) {
                offset.set(offset.x + direction.getOffsetX(), offset.y + direction.getOffsetY());
                if (inBoard(offset) && isEnemyShip(target, offset) && !unpassableContentDirections.contains(direction)) {
                    unpassableContentDirections.add(direction);
                    availableForAttack.add(new Point<>(offset));
                }
                offset.set(target);
            }
        }

        if (firingRadius > 2) {
            for (Direction direction : Direction.getHorseDirections()) {
                offset.set(offset.x + direction.getOffsetX(), offset.y + direction.getOffsetY());
                if (inBoard(offset) && isEnemyShip(target, offset) && !unpassableContentDirections.contains(direction)) {
                    unpassableContentDirections.add(direction);
                    availableForAttack.add(new Point<>(offset));
                }
                offset.set(target);
            }
        }
        return availableForAttack;
    }

    public TurnType getTurnTypeByPath(Path<Integer> path) {
        Point<Integer> target = path.getTarget();
        return sidesField[target.y][target.x].isPlayer() ? TurnType.ATTACK : TurnType.MOVE;
    }

    public boolean isGameOver() {
        return controller.getEnemyShipLocations().isEmpty() || controller.getPlayerShipLocations().isEmpty();
    }

    public double calculateReward(Path<Integer> spaceshipPath) {
        double reward = 0;
        Point<Integer> targetSpaceship = spaceshipPath.getSource();

        List<Point<Integer>> playerShipsAroundTarget = getSpaceshipsAround(spaceshipPath.getTarget(), true);
        List<Point<Integer>> botShipsAroundTarget = getSpaceshipsAround(spaceshipPath.getTarget(), false);
        int playerCoef = turn.isPlayer() ? 1 : -1;
        int enemyCoef = -playerCoef;
        for (Point<Integer> playerShip : playerShipsAroundTarget) {
            double ratio = (double) damageField[playerShip.y][playerShip.x] / healthField[targetSpaceship.y][targetSpaceship.x];
            reward += playerCoef * ratio;
        }
        for (Point<Integer> botShip : botShipsAroundTarget) {
            double ratio = (double) healthField[botShip.y][botShip.x] / damageField[botShip.y][botShip.x];
            if (canAttackTarget(botShip, targetSpaceship)) {
                reward += enemyCoef * ratio;
            }
        }
        if (isShip(spaceshipPath.getTarget())) {
            Point<Integer> spaceship = spaceshipPath.getTarget();
            reward += reward * ((double) damageField[spaceship.y][spaceship.x] - healthField[spaceship.y][spaceship.x]);
        }

        return reward;
    }

    private boolean canAttackTarget(Point<Integer> source, Point<Integer> target) {
         return Math.sqrt((source.y - target.y) * (source.y - target.y) + (source.x - target.x) * (source.x - target.x)) <= firingRadiusField[source.y][source.x];
    }

    private List<Point<Integer>> getSpaceshipsAround(Point<Integer> target, boolean isPlayer) {
        List<Point<Integer>> locations = new ArrayList<>();
        List<Point<Integer>> spaceships = isPlayer ? controller.getPlayerShipLocations() : controller.getEnemyShipLocations();
        for (Point<Integer> playerSpaceshipLocation : spaceships) {
            double distance = Math.sqrt((target.y - playerSpaceshipLocation.y)*(target.y - playerSpaceshipLocation.y)
                    + (target.x - playerSpaceshipLocation.x)*(target.x - playerSpaceshipLocation.x));
            if (distance <= 3) {
                locations.add(playerSpaceshipLocation);
            }
        }
        return locations;
    }
}

class ArtificialSpaceshipController {
    private final List<Point<Integer>> playerShipLocations, enemyShipLocations;
    private final List<Point<Integer>> blockedShipLocations;

    public ArtificialSpaceshipController() {
        playerShipLocations = new ArrayList<>();
        enemyShipLocations = new ArrayList<>();
        blockedShipLocations = new ArrayList<>();
    }

    public ArtificialSpaceshipController(ArtificialSpaceshipController controller) {
        playerShipLocations = new ArrayList<>(controller.playerShipLocations);
        enemyShipLocations = new ArrayList<>(controller.enemyShipLocations);
        blockedShipLocations = new ArrayList<>(controller.blockedShipLocations);
    }

    public void addPlayerShip(Point<Integer> location) {
        playerShipLocations.add(location);
    }

    public void addEnemyShip(Point<Integer> location) {
        enemyShipLocations.add(location);
    }

    public void removePlayerShip(Point<Integer> location) {
        playerShipLocations.remove(location);
    }

    public void removeEnemyShip(Point<Integer> location) {
        enemyShipLocations.remove(location);
    }

    public void addBlockedShip(Point<Integer> location) {
        blockedShipLocations.add(location);
    }

    public void removeBlockedShip(Point<Integer> location) {
        blockedShipLocations.remove(location);
    }

    public void changePlayerShipLocation(Path<Integer> path) {
        playerShipLocations.remove(path.getSource());
        playerShipLocations.add(path.getTarget());
    }

    public void changeEnemyShipLocation(Path<Integer> path) {
        enemyShipLocations.remove(path.getSource());
        enemyShipLocations.add(path.getTarget());
    }

    public List<Point<Integer>> getEnemyShipLocations() {
        return enemyShipLocations;
    }

    public List<Point<Integer>> getPlayerShipLocations() {
        return playerShipLocations;
    }

    public List<Point<Integer>> getBlockedShipLocations() {
        return blockedShipLocations;
    }

    public void clear() {
        playerShipLocations.clear();
        enemyShipLocations.clear();
    }
}