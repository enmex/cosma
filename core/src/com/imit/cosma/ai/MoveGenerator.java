package com.imit.cosma.ai;

import com.imit.cosma.config.Config;
import com.imit.cosma.util.Point;
import com.imit.cosma.model.board.Board;
import com.imit.cosma.model.rules.Side;
import com.imit.cosma.util.Path;

import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {
    private List<Path> pathsAI, pathsPlayer;

    public MoveGenerator(Board board){
        pathsAI = new ArrayList<>();
        pathsPlayer = new ArrayList<>();
        update(board);
    }

    public List<Path> getEnemyPaths(){
        return pathsAI;
    }

    public List<Path> getPlayerPaths() {
        return pathsPlayer;
    }

    public void update(Board board){
        pathsAI.clear();
        for(int y = 0; y < Config.getInstance().BOARD_SIZE; y++){
            for(int x = 0; x < Config.getInstance().BOARD_SIZE; x++){
                if(board.isShip(x, y) && board.getSide(x, y) == Side.ENEMY){
                    for(Point point : board.getAvailableCellsForMove(x, y)){
                        pathsAI.add(new Path(x, y, point.x, point.y));
                    }
                    for(Point point : board.getAvailableCellsForFire(x, y)){
                        pathsAI.add(new Path(x, y, point.x, point.y));
                    }
                }
            }
        }

        pathsPlayer.clear();
        for(int y = 0; y < Config.getInstance().BOARD_SIZE; y++){
            for(int x = 0; x < Config.getInstance().BOARD_SIZE; x++){
                if(board.isShip(x, y) && board.getSide(x, y) == Side.PLAYER){
                    for(Point point : board.getAvailableCellsForMove(x, y)){
                        pathsPlayer.add(new Path(x, y, point.x, point.y));
                    }
                    for(Point point : board.getAvailableCellsForFire(x, y)){
                        pathsPlayer.add(new Path(x, y, point.x, point.y));
                    }
                }
            }
        }
    }
}
