package com.imit.cosma.model.rules;

import com.imit.cosma.util.Point;
import com.imit.cosma.model.board.Board;
import com.imit.cosma.model.spaceship.Spaceship;

import java.util.HashSet;
import java.util.Set;

public class Attack {

    public static Set<Point> getAvailableCells(Board board){
        int selectedX = board.getSelectedX();
        int selectedY = board.getSelectedY();
        int radius = ((Spaceship)board.getSelected().getContent()).getWeaponRange();

        Set<Point> availableCells = new HashSet<>();
        int offsetX;
        int offsetY;
        for(Direction direction : Direction.getStraight()){
            for(int i = 1; i <= radius; i++){
                offsetX = selectedX + i*direction.getOffsetX();
                offsetY = selectedY + i*direction.getOffsetY();
                if(board.inBoard(offsetX, offsetY) && board.isEnemyShip(offsetX, offsetY)){
                    availableCells.add(new Point(offsetX, offsetY));
                }
            }
        }
        for(Direction direction : Direction.getDiagonal()){
            for(int i = 1; i <= Math.sqrt(radius); i++){
                offsetX = selectedX + i*direction.getOffsetX();
                offsetY = selectedY + i*direction.getOffsetY();
                if(board.inBoard(offsetX, offsetY) && board.isEnemyShip(offsetX, offsetY)){
                    availableCells.add(new Point(offsetX, offsetY));
                }
            }
        }
        return availableCells;
    }

    public static Set<Point> getAvailableCells(Board board, Point selected){
        return getAvailableCells(board, selected.x, selected.y);
    }

    public static Set<Point> getAvailableCells(Board board, int selectedX, int selectedY){
        int radius = ((Spaceship)board.getContent(selectedX, selectedY)).getWeaponRange();

        Set<Point> availableCells = new HashSet<>();
        int offsetX;
        int offsetY;
        for(Direction direction : Direction.getStraight()){
            for(int i = 1; i <= radius; i++){
                offsetX = selectedX + i*direction.getOffsetX();
                offsetY = selectedY + i*direction.getOffsetY();
                if(board.inBoard(offsetX, offsetY) && board.isEnemyShip(offsetX, offsetY)){
                    availableCells.add(new Point(offsetX, offsetY));
                }
            }
        }
        for(Direction direction : Direction.getDiagonal()){
            for(int i = 1; i <= Math.sqrt(radius); i++){
                offsetX = selectedX + i*direction.getOffsetX();
                offsetY = selectedY + i*direction.getOffsetY();
                if(board.inBoard(offsetX, offsetY) && board.isEnemyShip(offsetX, offsetY)){
                    availableCells.add(new Point(offsetX, offsetY));
                }
            }
        }
        return availableCells;
    }


}
