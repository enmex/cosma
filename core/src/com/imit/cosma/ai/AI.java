package com.imit.cosma.ai;

import com.imit.cosma.model.board.Board;
import com.imit.cosma.model.rules.TurnType;
import com.imit.cosma.util.Path;

public class AI {
    private final PathGenerator generator;

    private final DecisionTree tree;

    private Path<Integer> playerPath;
    private TurnType playerTurnType;

    private final ArtificialBoard board;

    public AI(final Board board){
        this.board = new ArtificialBoard(board);
        tree = new MCTree(this.board);
        generator = new PathGenerator();
    }

    public Path<Integer> getPath(Board board){
        this.board.update(board);
        generator.update(board);
        tree.climbDown(playerPath, playerTurnType);
        Path<Integer> currentPath = tree.treeSearch(this.board);
        TurnType turnType = this.board.getTurnTypeByPath(currentPath);
        tree.climbDown(currentPath, turnType);
        return currentPath;
    }

    public boolean isLoading() {
        return false;
    }

    public void savePlayerTurn(Path<Integer> playerPath, TurnType turnType) {
        this.playerPath = playerPath;
        playerTurnType = turnType;
    }
}