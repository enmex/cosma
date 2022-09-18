package com.imit.cosma.model.rules;

import com.imit.cosma.ai.ArtificialBoard;
import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.model.board.Board;
import com.imit.cosma.model.spaceship.Spaceship;

import java.util.HashSet;
import java.util.Set;

public class Attack {

    public static Set<IntegerPoint> getAvailable(Board board){
        IntegerPoint selectedPoint = board.getSelectedPoint();

        int radius = ((Spaceship)board.getSelected().getContent()).getWeaponRange();

        Set<IntegerPoint> availableCells = new HashSet<>();
        int offsetX;
        int offsetY;
        //TODO это не то

        for(Direction direction : Direction.getHorizontal()){
            for(int i = 1; i <= radius; i++){
                offsetX = selectedPoint.x + i * direction.getOffsetX();
                offsetY = selectedPoint.y + i * direction.getOffsetY();
                if(isAvailable(board, offsetX, offsetY)) {
                    availableCells.add(new IntegerPoint(offsetX, offsetY));
                }
                if(isAvailable(board, offsetX, offsetY + 1)){
                    availableCells.add(new IntegerPoint(offsetX, offsetY + 1));
                }
                if(isAvailable(board, offsetX, offsetY - 1)){
                    availableCells.add(new IntegerPoint(offsetX, offsetY - 1));
                }
            }
        }
        for(Direction direction : Direction.getVertical()){
            for(int i = 1; i <= radius; i++){
                offsetX = selectedPoint.x + i * direction.getOffsetX();
                offsetY = selectedPoint.y + i * direction.getOffsetY();
                if(isAvailable(board, offsetX, offsetY)) {
                    availableCells.add(new IntegerPoint(offsetX, offsetY));
                }
                if(isAvailable(board, offsetX - 1, offsetY)){
                    availableCells.add(new IntegerPoint(offsetX - 1, offsetY));
                }
                if(isAvailable(board, offsetX + 1, offsetY)){
                    availableCells.add(new IntegerPoint(offsetX + 1, offsetY));
                }
            }
        }

        if(radius == 3) { //TODO config
            for(Direction direction : Direction.getDiagonal()){
                for(int i = 2; i <= radius - 1; i++){
                    offsetX = selectedPoint.x + i * direction.getOffsetX();
                    offsetY = selectedPoint.y + i * direction.getOffsetY();
                    if(isAvailable(board, offsetX, offsetY)){
                        availableCells.add(new IntegerPoint(offsetX, offsetY));
                    }
                }
            }
        }
        return availableCells;
    }

    public static Set<IntegerPoint> getAvailable(Board board , IntegerPoint target) {
        return getAvailable(board, target.x, target.y);
    }

    public static Set<IntegerPoint> getAvailable(ArtificialBoard board , IntegerPoint target) {
        return getAvailable(board, target.x, target.y);
    }

    public static Set<IntegerPoint> getAvailable(ArtificialBoard board, int x, int y) {
        IntegerPoint selectedPoint = board.getSelectedPoint();

        int radius = board.getWeaponRange(x, y);

        Set<IntegerPoint> availableCells = new HashSet<>();
        int offsetX;
        int offsetY;
        //TODO это не то

        for(Direction direction : Direction.getHorizontal()){
            for(int i = 1; i <= radius; i++){
                offsetX = selectedPoint.x + i * direction.getOffsetX();
                offsetY = selectedPoint.y + i * direction.getOffsetY();
                if(isAvailable(board, offsetX, offsetY)) {
                    availableCells.add(new IntegerPoint(offsetX, offsetY));
                }
                if(isAvailable(board, offsetX, offsetY + 1)){
                    availableCells.add(new IntegerPoint(offsetX, offsetY + 1));
                }
                if(isAvailable(board, offsetX, offsetY - 1)){
                    availableCells.add(new IntegerPoint(offsetX, offsetY - 1));
                }
            }
        }
        for(Direction direction : Direction.getVertical()){
            for(int i = 1; i <= radius; i++){
                offsetX = selectedPoint.x + i * direction.getOffsetX();
                offsetY = selectedPoint.y + i * direction.getOffsetY();
                if(isAvailable(board, offsetX, offsetY)) {
                    availableCells.add(new IntegerPoint(offsetX, offsetY));
                }
                if(isAvailable(board, offsetX - 1, offsetY)){
                    availableCells.add(new IntegerPoint(offsetX - 1, offsetY));
                }
                if(isAvailable(board, offsetX + 1, offsetY)){
                    availableCells.add(new IntegerPoint(offsetX + 1, offsetY));
                }
            }
        }

        if(radius == 3) { //TODO config
            for(Direction direction : Direction.getDiagonal()){
                for(int i = 2; i <= radius - 1; i++){
                    offsetX = selectedPoint.x + i * direction.getOffsetX();
                    offsetY = selectedPoint.y + i * direction.getOffsetY();
                    if(isAvailable(board, offsetX, offsetY)){
                        availableCells.add(new IntegerPoint(offsetX, offsetY));
                    }
                }
            }
        }
        return availableCells;
    }

    public static Set<IntegerPoint> getAvailable(Board board, int selectedX, int selectedY){
        int radius = ((Spaceship)board.getCell(new IntegerPoint(selectedX, selectedY)).getContent()).getWeaponRange();

        Set<IntegerPoint> availableCells = new HashSet<>();
        int offsetX;
        int offsetY;

        for(Direction direction : Direction.getHorizontal()){
            for(int i = 1; i <= radius; i++){
                offsetX = selectedX + i * direction.getOffsetX();
                offsetY = selectedY + i * direction.getOffsetY();
                if(isAvailable(board, offsetX, offsetY)) {
                    availableCells.add(new IntegerPoint(offsetX, offsetY));
                }
                if(isAvailable(board, offsetX, offsetY + 1)){
                    availableCells.add(new IntegerPoint(offsetX, offsetY + 1));
                }
                if(isAvailable(board, offsetX, offsetY - 1)){
                    availableCells.add(new IntegerPoint(offsetX, offsetY - 1));
                }
            }
        }

        for(Direction direction : Direction.getVertical()){
            for(int i = 1; i <= radius; i++){
                offsetX = selectedX + i * direction.getOffsetX();
                offsetY = selectedY + i * direction.getOffsetY();
                if(isAvailable(board, offsetX, offsetY)) {
                    availableCells.add(new IntegerPoint(offsetX, offsetY));
                }
                if(isAvailable(board, offsetX - 1, offsetY)){
                    availableCells.add(new IntegerPoint(offsetX - 1, offsetY));
                }
                if(isAvailable(board, offsetX + 1, offsetY)){
                    availableCells.add(new IntegerPoint(offsetX + 1, offsetY));
                }
            }
        }

        if(radius == 3) { //TODO config
            for(Direction direction : Direction.getDiagonal()){
                for(int i = 2; i <= radius - 1; i++){
                    offsetX = selectedX + i * direction.getOffsetX();
                    offsetY = selectedY + i * direction.getOffsetY();
                    if(isAvailable(board, offsetX, offsetY)){
                        availableCells.add(new IntegerPoint(offsetX, offsetY));
                    }
                }
            }
        }
        return availableCells;
    }

    private static boolean isAvailable(Board board, int offsetX, int offsetY){
        IntegerPoint offset = new IntegerPoint(offsetX, offsetY);
        return board.inBoard(offset) && board.isEnemyShip(offset);
    }

    private static boolean isAvailable(ArtificialBoard board, int offsetX, int offsetY) {
        IntegerPoint offset = new IntegerPoint(offsetX, offsetY);
        return board.inBoard(offset) && board.isEnemyShip(offset);
    }
}
