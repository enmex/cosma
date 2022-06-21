package com.imit.cosma.ai;

import static com.imit.cosma.model.rules.StepMode.ATTACK;
import static com.imit.cosma.model.rules.StepMode.MOVE;

import com.imit.cosma.model.board.Board;
import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.util.MutualLinkedMap;
import com.imit.cosma.util.Path;

import java.util.Map;

public class AI {
    private final MoveGenerator generator;
    private final int depth = 3; //TODO config

    private DecisionTree cachedTree;

    private MutualLinkedMap<Path, StepMode> playerTurns;

    private Board board;

    private MutualLinkedMap<Path, StepMode> currentPaths;

    public AI(final Board board){
        this.board = board.clone();
        playerTurns = new MutualLinkedMap<>();
        cachedTree = new DecisionTree(depth);
        generator = new MoveGenerator(board);
        currentPaths = new MutualLinkedMap<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                cachedTree.cacheTree(board.clone());
            }
        }).start();
    }

    private void updatePaths(){
        int maxAdvantage = -Integer.MAX_VALUE;

        cachedTree.climbDown(board, playerTurns); //корень = ход игрока

        //получение лучшего дочернего узла
        for(Map.Entry<MutualLinkedMap<Path, StepMode>, Integer> entry : cachedTree.getRootChildren().entrySet()) {
            if(entry.getValue() >= maxAdvantage) {
                maxAdvantage = entry.getValue();
                currentPaths.clear();
                currentPaths.putAll(entry.getKey());
            }
        }
        System.out.println(maxAdvantage);
        cachedTree.climbDown(board, currentPaths); //корень = ход ИИ

        playerTurns.clear();
    }

    //сначала MOVE затем ATTACK
    public Path getPath() {
        if(currentPaths.size() == 0) {
            updatePaths();
        }

        Path path = currentPaths.getKey(MOVE) == null ? null : currentPaths.getKey(MOVE).clone();

        if(path == null) {
            path = currentPaths.getKey(ATTACK) == null ? null : currentPaths.getKey(ATTACK).clone();
        }

        currentPaths.removeKey(path);
        return path;
    }

    public void update(Board board){
        this.board = board;
        generator.update(board);
    }

    public boolean isLoading() {
        return cachedTree.isCaching();
    }

    public void savePlayerTurn(Path playerTurn, StepMode turnType) {
        playerTurns.put(playerTurn.clone(), turnType);
    }
}