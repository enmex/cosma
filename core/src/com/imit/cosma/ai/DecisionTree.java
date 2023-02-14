package com.imit.cosma.ai;

import com.imit.cosma.model.rules.TurnType;
import com.imit.cosma.util.Path;

public abstract class DecisionTree {
    protected ArtificialBoard board;

    protected DecisionTree(ArtificialBoard board) {
        this.board = board;
    }

    public abstract void climbDown(Path<Integer> path, TurnType turnType);

    public abstract Path<Integer> treeSearch(ArtificialBoard board);
}