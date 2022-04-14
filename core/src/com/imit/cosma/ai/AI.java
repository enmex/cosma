package com.imit.cosma.ai;

import com.imit.cosma.model.board.Board;
import com.imit.cosma.util.Path;

public class AI {
    private final MoveGenerator generator;
    private final int depth = 5; //TODO config
    private final DecisionTree tree;

    public AI(Board board){
        tree = new DecisionTree(board);
        generator = new MoveGenerator(board);
    }

    public Path getPath(){
        int maxAdvantage = -1;
        Path bestPath = null;

        for(Path rootPath : generator.getEnemyPaths()){
            tree.setRootPath(rootPath);

            int advantage = tree.calculateBestPath(depth, 1, -Integer.MAX_VALUE, Integer.MAX_VALUE);

            if(maxAdvantage < advantage) {
                maxAdvantage = advantage;
                bestPath = rootPath;
            }
        }
        return bestPath;
    }

    public void update(Board board){
        tree.update(board);
        generator.update(board);
    }
}