package com.imit.cosma.ai;

import com.imit.cosma.model.board.Board;
import com.imit.cosma.util.Path;

import java.util.ArrayList;
import java.util.List;

public class AI {
    private final MoveGenerator generator;
    private final int depth = 3; //TODO config

    private DecisionTree cachedTree;

    public AI(Board board){
        cachedTree = new DecisionTree(board);
        generator = new MoveGenerator(board);
    }

    public Path getPath(){
        int maxAdvantage = -1;
        Path bestPath = null;

        for(Path rootPath : generator.getEnemyPaths()) {
            cachedTree.setRootPath(rootPath);

            int advantage = cachedTree.calculateBestPath(depth, 1, -Integer.MAX_VALUE, Integer.MAX_VALUE);

            if(maxAdvantage < advantage) {
                maxAdvantage = advantage;
                bestPath = rootPath;
            }
        }
        System.out.println(maxAdvantage);
        return bestPath;
    }

    public void update(Board board){
        cachedTree.update(board);
        generator.update(board);
    }
}