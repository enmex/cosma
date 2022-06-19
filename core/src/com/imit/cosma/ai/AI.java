package com.imit.cosma.ai;

import com.imit.cosma.model.board.Board;
import com.imit.cosma.util.Path;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AI {
    private final MoveGenerator generator;
    private final int depth = 3; //TODO config

    private DecisionTree cachedTree;

    private Set<Path> playerTurns;

    private Board board;

    public AI(final Board board){
        this.board = board.clone();
        playerTurns = new HashSet<>();
        cachedTree = new DecisionTree(depth);
        generator = new MoveGenerator(board);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cachedTree.cacheTree(board.clone());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public Path getPath(){
        int maxAdvantage = -Integer.MAX_VALUE;
        Path bestPath = null;

        for(Path playerTurn : playerTurns) {
            cachedTree.climbDown(board, generator, playerTurn);
        }
        System.out.println("Ходы игрока учтены");

        for(Map.Entry<Path, Integer> entry : cachedTree.getRootChildren().entrySet()) {
            if(entry.getValue() >= maxAdvantage) {
                maxAdvantage = entry.getValue();
                bestPath = entry.getKey();
            }
        }
        System.out.println("Просчитан лучший ход");

        cachedTree.climbDown(board, generator, bestPath);
        System.out.println("Достроено дерево");
        return bestPath;
    }

    public void update(Board board){
        generator.update(board);
    }

    public boolean isLoading() {
        return cachedTree.isCaching();
    }

    public void rememberPlayerTurn(Path playerTurn) {
        playerTurns.add(playerTurn);
    }
}