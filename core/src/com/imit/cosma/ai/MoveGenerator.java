package com.imit.cosma.ai;

import com.imit.cosma.config.Config;
import com.imit.cosma.util.Point;
import com.imit.cosma.model.board.Board;
import com.imit.cosma.model.rules.Side;
import com.imit.cosma.util.Path;

import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {
    private List<Path> paths;

    public MoveGenerator(Board board){
        paths = new ArrayList<>();
        update(board);
    }

    public Path getRandomPath(){
        return paths.get((int) (Math.random() * paths.size()));
    }

    public int getPathsSize(){
        return paths.size();
    }

    public List<Path> getPaths(){
        return paths;
    }

    public void update(Board board){
        paths.clear();
        for(int y = 0; y < Config.getInstance().BOARD_SIZE; y++){
            for(int x = 0; x < Config.getInstance().BOARD_SIZE; x++){
                if(board.isShip(x, y) && board.getSide(x, y) == Side.ENEMY){
                    for(Point point : board.getAvailableCellsForMove(x, y)){
                        paths.add(new Path(x, y, point.x, point.y));
                    }
                    for(Point point : board.getAvailableCellsForFire(x, y)){
                        paths.add(new Path(x, y, point.x, point.y));
                    }
                }
            }
        }
    }
}
