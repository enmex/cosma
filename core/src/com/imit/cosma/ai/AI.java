package com.imit.cosma.ai;

import com.imit.cosma.model.board.Board;
import com.imit.cosma.util.Path;

public class AI {
    private final int depth = 2; //TODO config
    private final DecisionTree tree;

    public AI(Board board){
        tree = new DecisionTree(board);
    }

    public Path getPath(){
        System.out.println("start");
        tree.calculateBestMove(depth, false);
        System.out.println("\nend");
        return tree.getCurrentPath();
    }

    public void update(Board board){
        tree.update(board);
    }


}