package com.imit.cosma.ai;

import com.imit.cosma.model.board.Board;
import com.imit.cosma.util.Path;

public class AI {
    private MoveGenerator generator;

    public AI(Board board){
        generator = new MoveGenerator(board);
    }

    public Path getPath(){
        return generator.getRandomPath();
    }

    public void update(Board board){
        generator.update(board);
    }
}