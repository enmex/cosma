package com.imit.cosma.ai;

import com.imit.cosma.config.Config;
import com.imit.cosma.model.board.Board;
import com.imit.cosma.model.rules.Side;
import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

public class ArtificialBoard {
    private Point selected;

    private CellData[][] board;

    public ArtificialBoard(Board board) {
        this.board = new CellData[Config.getInstance().BOARD_SIZE][Config.getInstance().BOARD_SIZE];

        update(board);
    }

    public void update(Board board) {
        for(int y = 0; y < Config.getInstance().BOARD_SIZE; y++) {
            for(int x = 0; x < Config.getInstance().BOARD_SIZE; x++) {
                this.board[y][x].update(board.getStepMode(x, y),
                        board.getSide(x, y),
                        calculateAdvantage(board, x, y));
            }
        }
    }

    private int calculateAdvantage(Board board, int x, int y) {
        return board.getMaxHealthPoints(x, y)
                - board.getHealthPoints(x, y)
                + board.getDamagePoints(x, y);
    }

    public int getAdvantage(Point target) {
        return board[target.y][target.x].advantage;
    }
/*
    public void makeArtificialTurn(Path path){
        Point source = path.getSource();
        Point target = path.getTarget();

        selected = source;

        if(selectedCanMoveTo(target.x, target.y)){
            setSelectedPosition(target.x, target.y);

            selected = cells[target.y][target.x];
            selectedX = target.x;
            selectedY = target.y;
        }
        else if(selectedCanFireTo(target.x, target.y)){
            damageShip(target.x, target.y, selected.getDamageAmount());
        }
    }

    public boolean selectedCanMoveTo(int x, int y){
        return isShipSelected() && !isSelected(x, y) && selected.canMoveTo(selectedX, selectedY, x, y)
                && stepModeMap[selected.y][selected.x] == StepMode.MOVE && isPassable(x, y);
    }
*/
    private boolean isShipSelected() {
        return board[selected.y][selected.x].side != Side.NONE;
    }

    private boolean isSelected(int x, int y) {
        return selected.x == x && selected.y == y;
    }

    private boolean isPassable(int x, int y) {
        return inBoard(x, y) && board[y][x].side == Side.NONE;
    }

    private boolean inBoard(int x, int y){
        return x >=0 && x < 8 && y >= 0 && y < 8;
    }
}

class CellData {
    protected StepMode stepMode;
    protected Side side;
    protected int advantage;

    public void update(StepMode stepMode, Side side, int advantage) {
        this.stepMode = stepMode;
        this.side = side;
        this.advantage = advantage;
    }
}
