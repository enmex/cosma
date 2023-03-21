package com.imit.cosma.ai;

import com.imit.cosma.model.rules.TurnType;
import com.imit.cosma.util.Path;

public abstract class DecisionTree {

    public abstract void climbDown(Path<Integer> path, TurnType turnType);

    public abstract void treeSearch(ArtificialBoard board);

    public abstract Path<Integer> getPath();
}