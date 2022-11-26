package com.imit.cosma.ai;

import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.util.MutualLinkedMap;
import com.imit.cosma.util.Path;

public abstract class DecisionTree {
    protected ArtificialBoard board;

    protected DecisionTree(ArtificialBoard board) {
        this.board = board;
    }

    public abstract void climbDown(MutualLinkedMap<Path, StepMode> paths);

    public abstract MutualLinkedMap<Path, StepMode> treeSearch(ArtificialBoard board);
}
