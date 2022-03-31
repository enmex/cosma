package com.imit.cosma.ai;

import com.imit.cosma.model.board.Board;
import com.imit.cosma.util.Path;

public class AI {
    private MoveGenerator generator;
    private Board board;

    public AI(Board board){
        generator = new MoveGenerator(board);
        this.board = board;
    }

    //TODO minimax
    public Path getPath(){
        Path bestPath = null;
        int bestAdvantage = -Integer.MAX_VALUE;

        for(Path path : generator.getPaths()) {
            int realAdvantage = Math.min(Math.min(board.getHealthPoints(path.getDestination()),
                                                  board.getDamagePoints(path.getDeparture())),
                                         Math.max(board.getDamagePoints(path.getDestination()),
                                                  board.getHealthPoints(path.getDeparture())));
            if(bestAdvantage < realAdvantage){
                bestAdvantage = realAdvantage;
                bestPath = path;
            }
        }
        return bestPath;
    }

    public void update(Board board){
        generator.update(board);
        this.board = board;
    }


}