package com.imit.cosma.ai;

import static com.imit.cosma.model.rules.StepMode.ATTACK;
import static com.imit.cosma.model.rules.StepMode.MOVE;

import com.imit.cosma.model.board.Board;
import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.util.MutualLinkedMap;
import com.imit.cosma.util.Path;

public class AI {
    private final MoveGenerator generator;

    private final DecisionTree cachedTree;

    private final MutualLinkedMap<Path, StepMode> playerTurns;

    private final ArtificialBoard board;

    private MutualLinkedMap<Path, StepMode> currentPaths;

    public AI(final Board board){
        this.board = new ArtificialBoard(board);
        playerTurns = new MutualLinkedMap<>();
        cachedTree = new DecisionTree();
        generator = new MoveGenerator(board);
        currentPaths = new MutualLinkedMap<>();
    }

    private void updatePaths(){
        cachedTree.climbDown(playerTurns); //корень = ход игрока

        //получение лучшего дочернего узла
        currentPaths = cachedTree.treeSearch(board);

        cachedTree.climbDown(currentPaths); //корень = ход ИИ
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
        this.board.update(board);
        generator.update(board);
    }

    public boolean isLoading() {
        return cachedTree.isCaching();
    }

    public void savePlayerTurn(Path playerTurn, StepMode turnType) {
        playerTurns.put(playerTurn.clone(), turnType);
    }
}