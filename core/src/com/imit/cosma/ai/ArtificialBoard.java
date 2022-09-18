package com.imit.cosma.ai;

import com.imit.cosma.config.Config;
import com.imit.cosma.model.board.Board;
import com.imit.cosma.model.rules.Attack;
import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.model.rules.move.MoveType;
import com.imit.cosma.model.rules.side.EnemySide;
import com.imit.cosma.model.rules.side.NeutralSide;
import com.imit.cosma.model.rules.side.PlayerSide;
import com.imit.cosma.model.rules.side.Side;
import com.imit.cosma.model.spaceship.Spaceship;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.IntegerPoint;

import java.util.HashSet;
import java.util.Set;

public class ArtificialBoard implements Cloneable {
    private Side[][] sidesField;
    private StepMode[][] stepModeField;
    private MoveType[][] moveTypeField;
    private int[][] healthField, damageField, weaponRangeField, maxHealthField;
    private boolean[][] obstaclesField;

    private final Set<IntegerPoint> emptySet = new HashSet<>();

    private Set<IntegerPoint> availableForMove, availableForAttack;

    private Side turn, playerSide, enemySide;

    private IntegerPoint selectedPoint;

    public ArtificialBoard() {
        int size = Config.getInstance().BOARD_SIZE;
        sidesField = new Side[size][size];
        healthField = new int[size][size];
        damageField = new int[size][size];
        stepModeField = new StepMode[size][size];
        weaponRangeField = new int[size][size];
        moveTypeField = new MoveType[size][size];
        obstaclesField = new boolean[size][size];
        maxHealthField = new int[size][size];

        availableForMove = new HashSet<>();
        availableForAttack = new HashSet<>();

        selectedPoint = new IntegerPoint();

        playerSide = new PlayerSide(Config.getInstance().DEFAULT_SHIPS_NUMBER);
        enemySide = new EnemySide(Config.getInstance().DEFAULT_SHIPS_NUMBER);
    }

    public ArtificialBoard(Board board) {
        this();
        turn = board.getTurn().clone();

        update(board);
    }

    public void update(Board board) {
        int size = Config.getInstance().BOARD_SIZE;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                sidesField[y][x] = board.getSide(x, y);
                healthField[y][x] = board.getHealthPoints(x, y);
                damageField[y][x] = board.getDamagePoints(x, y);
                stepModeField[y][x] = board.getStepMode(x, y);
                weaponRangeField[y][x] = board.isShip(x, y) ? ((Spaceship) (board.getCell(x, y).getContent())).getWeaponRange() : 0;
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

    public boolean isShip(IntegerPoint target) {
        return isShip(target.x, target.y);
    }

    public Side getSide(int x, int y) {
        return sidesField[y][x];
    }

    public void updateSide() {
        if (sideCompletedTurn()) {
            changeTurn();
        }
    }

    private boolean sideCompletedTurn() {
        return turn.getTurns() == 2 || turn.getTurns() == 1 && turn.getShipsNumber() == 1 && availableForAttack.isEmpty();
    }

    private void changeTurn() {
        turn.resetTurns();
        turn = turn.isPlayer() ? enemySide : playerSide;
    }

    public StepMode doTurn(Path path){
        StepMode mode = StepMode.COMPLETED;

        IntegerPoint source = path.getSource();
        IntegerPoint target = path.getTarget();

        setSelected(source);

        if(selectedCanMoveTo(target)){
            setSelectedPosition(target);

            setSelected(target);
            turn.updateTurns();

            mode = StepMode.MOVE;
        }
        else if(selectedCanFireTo(target)){
            damageShip(target, damageField[selectedPoint.y][selectedPoint.x]);
            turn.updateTurns();

            mode = StepMode.ATTACK;
        }

        return mode;
    }

    private void setSelected(IntegerPoint target) {
        selectedPoint.set(target);
        if(isShip(selectedPoint)) {
            availableForAttack = getStepMode(selectedPoint) == StepMode.ATTACK
                    ? Attack.getAvailable(this, selectedPoint)
                    : emptySet;
            availableForMove = getStepMode(selectedPoint) == StepMode.MOVE
                    ? moveTypeField[selectedPoint.y][selectedPoint.x].getMove().getAvailable(this, selectedPoint)
                    : emptySet;
        }
    }

    private void damageShip(IntegerPoint target, int damage) {
        healthField[target.y][target.x] -= damage;
        stepModeField[selectedPoint.y][selectedPoint.x] = StepMode.COMPLETED;

        if(healthField[target.y][target.x] <= 0){
            destroyShip(target);
        }
    }

    private void destroyShip(IntegerPoint target) {
        if(turn.isPlayer()) {
            enemySide.removeShip();
        }
        else {
            playerSide.removeShip();
        }

        healthField[target.y][target.x] = 0;
        stepModeField[target.y][target.x] = StepMode.COMPLETED;
        damageField[target.y][target.x] = 0;
        obstaclesField[target.y][target.x] = true;
        sidesField[target.y][target.x] = new NeutralSide();
        maxHealthField[target.y][target.x] = 0;
        weaponRangeField[target.y][target.x] = 0;
        moveTypeField[target.y][target.x] = MoveType.IDLE;
    }

    private void setSelectedPosition(IntegerPoint target) {
        Side sideTemp = sidesField[target.y][target.x];
        sidesField[target.y][target.x] = sidesField[selectedPoint.y][selectedPoint.x];
        sidesField[selectedPoint.y][selectedPoint.x] = sideTemp;

        int valueTemp = healthField[target.y][target.x];
        healthField[target.y][target.x] = healthField[selectedPoint.y][selectedPoint.x];
        healthField[selectedPoint.y][selectedPoint.x] = valueTemp;

        valueTemp = damageField[target.y][target.x];
        damageField[target.y][target.x] = damageField[selectedPoint.y][selectedPoint.x];
        damageField[selectedPoint.y][selectedPoint.x] = valueTemp;

        valueTemp = weaponRangeField[target.y][target.x];
        weaponRangeField[target.y][target.x] = weaponRangeField[selectedPoint.y][selectedPoint.x];
        weaponRangeField[selectedPoint.y][selectedPoint.x] = valueTemp;

        valueTemp = maxHealthField[target.y][target.x];
        maxHealthField[target.y][target.x] = maxHealthField[selectedPoint.y][selectedPoint.x];
        maxHealthField[selectedPoint.y][selectedPoint.x] = valueTemp;

        StepMode stepModeTemp = stepModeField[target.y][target.x];
        stepModeField[target.y][target.x] = stepModeField[selectedPoint.y][selectedPoint.x];
        stepModeField[selectedPoint.y][selectedPoint.x] = stepModeTemp;

        MoveType moveTypeTemp = moveTypeField[target.y][target.x];
        moveTypeField[target.y][target.x] = moveTypeField[selectedPoint.y][selectedPoint.x];
        moveTypeField[selectedPoint.y][selectedPoint.x] = moveTypeTemp;

        boolean obstacleTemp = obstaclesField[target.y][target.x];
        obstaclesField[target.y][target.x] = obstaclesField[selectedPoint.y][selectedPoint.x];
        obstaclesField[selectedPoint.y][selectedPoint.x] = obstacleTemp;

        stepModeField[target.y][target.x] = StepMode.ATTACK;
    }

    private StepMode getStepMode(IntegerPoint target) {
        return getStepMode(target.x, target.y);
    }

    private StepMode getStepMode(int x, int y) {
        return stepModeField[y][x];
    }

    public int getWeaponRange(int x, int y) {
        return weaponRangeField[y][x];
    }

    public boolean selectedCanMoveTo(IntegerPoint target){
        return isShip(selectedPoint)
                && !target.equals(selectedPoint)
                && moveTypeField[selectedPoint.y][selectedPoint.x].getMove().canMoveTo(selectedPoint.x, selectedPoint.y, target.x, target.y)
                && stepModeField[selectedPoint.y][selectedPoint.x] == StepMode.MOVE && isPassable(target);
    }

    public boolean selectedCanFireTo(IntegerPoint target){
        return isShip(selectedPoint) && isShip(target)
                && sidesField[selectedPoint.y][selectedPoint.x] != sidesField[target.y][target.x]
                && stepModeField[selectedPoint.y][selectedPoint.x] == StepMode.ATTACK
                && availableForAttack.contains(target);
    }

    public Set<IntegerPoint> getAvailableCellsForMove(int x, int y) {
        return isShip(x, y) && stepModeField[y][x] == StepMode.MOVE
                ? moveTypeField[y][x].getMove().getAvailable(this, new IntegerPoint(x, y))
                : emptySet;
    }

    public Set<IntegerPoint> getAvailableCellsForFire(int x, int y) {
        return isShip(x, y) && stepModeField[y][x] == StepMode.ATTACK
                ? Attack.getAvailable(this, x, y)
                : emptySet;
    }

    public IntegerPoint getSelectedPoint() {
        return selectedPoint;
    }

    public boolean inBoard(IntegerPoint target){
        return inBoard(target.x, target.y);
    }

    public boolean inBoard(int x, int y){
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    public boolean isEnemyShip(IntegerPoint target) {
        return isEnemyShip(target.x, target.y);
    }

    public boolean isEnemyShip(int x, int y) {
        return sidesField[selectedPoint.y][selectedPoint.x] != sidesField[y][x];
    }

    public boolean isPassable(IntegerPoint target) {
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
                board.sidesField[y][x] = sidesField[y][x];
                board.healthField[y][x] = healthField[y][x];
                board.damageField[y][x] = damageField[y][x];
                board.stepModeField[y][x] = stepModeField[y][x];
                board.moveTypeField[y][x] = moveTypeField[y][x];
                board.weaponRangeField[y][x] = weaponRangeField[y][x];
                board.obstaclesField[y][x] = obstaclesField[y][x];
                board.maxHealthField[y][x] = maxHealthField[y][x];
            }
        }

        board.turn = turn.clone();
        board.selectedPoint = selectedPoint.clone();
        board.availableForAttack = new HashSet<>(availableForAttack);
        board.availableForMove = new HashSet<>(availableForMove);

        return board;
    }

    public int getHealthPoints(IntegerPoint target) {
        return healthField[target.y][target.x];
    }

    public int getDamagePoints(IntegerPoint target) {
        return damageField[target.y][target.x];
    }

    public int getMaxHealthPoints(IntegerPoint target) {
        return maxHealthField[target.y][target.x];
    }
}
