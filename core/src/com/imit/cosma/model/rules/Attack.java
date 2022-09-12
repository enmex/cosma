package com.imit.cosma.model.rules;

import com.imit.cosma.ai.ArtificialBoard;
import com.imit.cosma.util.Point;
import com.imit.cosma.model.board.Board;
import com.imit.cosma.model.spaceship.Spaceship;

import java.util.HashSet;
import java.util.Set;

public class Attack {

    public static Set<Point> getAvailable(Board board){
        Point selectedPoint = board.getSelectedPoint();

        int radius = ((Spaceship)board.getSelected().getContent()).getWeaponRange();

        Set<Point> availableCells = new HashSet<>();
        int offsetX;
        int offsetY;
        //TODO это не то

        for(Direction direction : Direction.getHorizontal()){
            for(int i = 1; i <= radius; i++){
                offsetX = selectedPoint.x + i * direction.getOffsetX();
                offsetY = selectedPoint.y + i * direction.getOffsetY();
                if(isAvailable(board, offsetX, offsetY)) {
                    availableCells.add(new Point(offsetX, offsetY));
                }
                if(isAvailable(board, offsetX, offsetY + 1)){
                    availableCells.add(new Point(offsetX, offsetY + 1));
                }
                if(isAvailable(board, offsetX, offsetY - 1)){
                    availableCells.add(new Point(offsetX, offsetY - 1));
                }
            }
        }
        for(Direction direction : Direction.getVertical()){
            for(int i = 1; i <= radius; i++){
                offsetX = selectedPoint.x + i * direction.getOffsetX();
                offsetY = selectedPoint.y + i * direction.getOffsetY();
                if(isAvailable(board, offsetX, offsetY)) {
                    availableCells.add(new Point(offsetX, offsetY));
                }
                if(isAvailable(board, offsetX - 1, offsetY)){
                    availableCells.add(new Point(offsetX - 1, offsetY));
                }
                if(isAvailable(board, offsetX + 1, offsetY)){
                    availableCells.add(new Point(offsetX + 1, offsetY));
                }
            }
        }

        if(radius == 3) { //TODO config
            for(Direction direction : Direction.getDiagonal()){
                for(int i = 2; i <= radius - 1; i++){
                    offsetX = selectedPoint.x + i * direction.getOffsetX();
                    offsetY = selectedPoint.y + i * direction.getOffsetY();
                    if(isAvailable(board, offsetX, offsetY)){
                        availableCells.add(new Point(offsetX, offsetY));
                    }
                }
            }
        }
        return availableCells;
    }

    public static Set<Point> getAvailable(Board board ,Point target) {
        return getAvailable(board, target.x, target.y);
    }

    public static Set<Point> getAvailable(ArtificialBoard board ,Point target) {
        return getAvailable(board, target.x, target.y);
    }

    public static Set<Point> getAvailable(ArtificialBoard board, int x, int y) {
        Point selectedPoint = board.getSelectedPoint();

        int radius = board.getWeaponRange(x, y);

        Set<Point> availableCells = new HashSet<>();
        int offsetX;
        int offsetY;
        //TODO это не то

        for(Direction direction : Direction.getHorizontal()){
            for(int i = 1; i <= radius; i++){
                offsetX = selectedPoint.x + i * direction.getOffsetX();
                offsetY = selectedPoint.y + i * direction.getOffsetY();
                if(isAvailable(board, offsetX, offsetY)) {
                    availableCells.add(new Point(offsetX, offsetY));
                }
                if(isAvailable(board, offsetX, offsetY + 1)){
                    availableCells.add(new Point(offsetX, offsetY + 1));
                }
                if(isAvailable(board, offsetX, offsetY - 1)){
                    availableCells.add(new Point(offsetX, offsetY - 1));
                }
            }
        }
        for(Direction direction : Direction.getVertical()){
            for(int i = 1; i <= radius; i++){
                offsetX = selectedPoint.x + i * direction.getOffsetX();
                offsetY = selectedPoint.y + i * direction.getOffsetY();
                if(isAvailable(board, offsetX, offsetY)) {
                    availableCells.add(new Point(offsetX, offsetY));
                }
                if(isAvailable(board, offsetX - 1, offsetY)){
                    availableCells.add(new Point(offsetX - 1, offsetY));
                }
                if(isAvailable(board, offsetX + 1, offsetY)){
                    availableCells.add(new Point(offsetX + 1, offsetY));
                }
            }
        }

        if(radius == 3) { //TODO config
            for(Direction direction : Direction.getDiagonal()){
                for(int i = 2; i <= radius - 1; i++){
                    offsetX = selectedPoint.x + i * direction.getOffsetX();
                    offsetY = selectedPoint.y + i * direction.getOffsetY();
                    if(isAvailable(board, offsetX, offsetY)){
                        availableCells.add(new Point(offsetX, offsetY));
                    }
                }
            }
        }
        return availableCells;
    }

    public static Set<Point> getAvailable(Board board, int selectedX, int selectedY){
        int radius = ((Spaceship)board.getCell(new Point(selectedX, selectedY)).getContent()).getWeaponRange();

        Set<Point> availableCells = new HashSet<>();
        int offsetX;
        int offsetY;

        for(Direction direction : Direction.getHorizontal()){
            for(int i = 1; i <= radius; i++){
                offsetX = selectedX + i * direction.getOffsetX();
                offsetY = selectedY + i * direction.getOffsetY();
                if(isAvailable(board, offsetX, offsetY)) {
                    availableCells.add(new Point(offsetX, offsetY));
                }
                if(isAvailable(board, offsetX, offsetY + 1)){
                    availableCells.add(new Point(offsetX, offsetY + 1));
                }
                if(isAvailable(board, offsetX, offsetY - 1)){
                    availableCells.add(new Point(offsetX, offsetY - 1));
                }
            }
        }

        for(Direction direction : Direction.getVertical()){
            for(int i = 1; i <= radius; i++){
                offsetX = selectedX + i * direction.getOffsetX();
                offsetY = selectedY + i * direction.getOffsetY();
                if(isAvailable(board, offsetX, offsetY)) {
                    availableCells.add(new Point(offsetX, offsetY));
                }
                if(isAvailable(board, offsetX - 1, offsetY)){
                    availableCells.add(new Point(offsetX - 1, offsetY));
                }
                if(isAvailable(board, offsetX + 1, offsetY)){
                    availableCells.add(new Point(offsetX + 1, offsetY));
                }
            }
        }

        if(radius == 3) { //TODO config
            for(Direction direction : Direction.getDiagonal()){
                for(int i = 2; i <= radius - 1; i++){
                    offsetX = selectedX + i * direction.getOffsetX();
                    offsetY = selectedY + i * direction.getOffsetY();
                    if(isAvailable(board, offsetX, offsetY)){
                        availableCells.add(new Point(offsetX, offsetY));
                    }
                }
            }
        }
        return availableCells;
    }

    private static boolean isAvailable(Board board, int offsetX, int offsetY){
        Point offset = new Point(offsetX, offsetY);
        return board.inBoard(offset) && board.isEnemyShip(offset);
    }

    private static boolean isAvailable(ArtificialBoard board, int offsetX, int offsetY) {
        Point offset = new Point(offsetX, offsetY);
        return board.inBoard(offset) && board.isEnemyShip(offset);
    }
}
