package com.imit.cosma.ai;

import com.imit.cosma.config.Config;
import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.util.MutualLinkedMap;
import com.imit.cosma.util.Path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MinMaxTree extends DecisionTree {
    private MinMaxTreeNode root;
    private int bestAdvantage;

    public MinMaxTree(ArtificialBoard board) {
        super(board);

        root = new MinMaxTreeNode();
    }

    @Override
    public MutualLinkedMap<Path, StepMode> treeSearch(ArtificialBoard board) {
        this.board = board;
        this.bestAdvantage = buildTree(
                board,
                Config.getInstance().TREE_MAX_DEPTH,
                -Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                root
        );

        MinMaxTreeNode bestNode = new MinMaxTreeNode();
        for (MinMaxTreeNode child : root.children) {
            if (child.advantage == bestAdvantage) {
                bestNode = child;
                break;
            }
        }

        return bestNode.pathToTypeMap;
    }

    private int buildTree(ArtificialBoard board, int depth, int alpha, int beta, MinMaxTreeNode parent) {
        if(depth == 0) {
            return calculateAdvantage(board, parent);
        }

        int childrenNumber = 0;

        PathGenerator generator = new PathGenerator(board);

        List<Path> pathsForFirstTurn = parent.playerTurn
                ? new ArrayList<>(generator.getEnemyPaths())
                : new ArrayList<>(generator.getPlayerPaths());

        int minMaxEval = parent.playerTurn ? Integer.MAX_VALUE : -Integer.MAX_VALUE;

        for(Path firstPath : pathsForFirstTurn) {
            ArtificialBoard firstTurnBoard = board.clone();

            StepMode firstTurnMode = firstTurnBoard.doTurn(firstPath);
            generator.update(firstTurnBoard);
            firstTurnBoard.updateSide();

            List<Path> pathsForSecondTurn = parent.playerTurn
                    ? new ArrayList<>(generator.getEnemyPaths())
                    : new ArrayList<>(generator.getPlayerPaths());

            MinMaxTreeNode node;

            if(pathsForSecondTurn.isEmpty()) {
                MutualLinkedMap<Path, StepMode> pathToTypeMap = new MutualLinkedMap<>();
                pathToTypeMap.put(firstPath, firstTurnMode);

                node = initNode(parent, pathToTypeMap);

                node.advantage = buildTree(firstTurnBoard.clone(), depth - 1, alpha, beta, node);

                childrenNumber++;

                if (childrenNumber >= Config.getInstance().MAX_CHILDREN_NODES) {
                    break;
                }

                if(parent.playerTurn){
                    minMaxEval = Math.min(minMaxEval, node.advantage);
                    beta = Math.min(node.advantage, beta);
                }
                else{
                    minMaxEval = Math.max(minMaxEval, node.advantage);
                    alpha = Math.max(node.advantage, alpha);
                }

            } else {
                for(Path secondPath : pathsForSecondTurn) {

                    ArtificialBoard secondTurnBoard = firstTurnBoard.clone();

                    StepMode secondMode = secondTurnBoard.doTurn(secondPath);

                    MutualLinkedMap<Path, StepMode> pathToTypeMap = new MutualLinkedMap<>();
                    pathToTypeMap.put(secondPath, secondMode);
                    pathToTypeMap.put(firstPath, firstTurnMode);

                    node = initNode(parent, pathToTypeMap);

                    node.advantage = buildTree(firstTurnBoard.clone(), depth - 1, alpha, beta, node);

                    childrenNumber++;

                    if (childrenNumber >= Config.getInstance().MAX_CHILDREN_NODES) {
                        break;
                    }

                    if(parent.playerTurn){
                        minMaxEval = Math.min(minMaxEval, node.advantage);
                        beta = Math.min(node.advantage, beta);
                    }
                    else{
                        minMaxEval = Math.max(minMaxEval, node.advantage);
                        alpha = Math.max(node.advantage, alpha);
                    }

                    if(beta <= alpha){
                        break;
                    }
                }
            }
            if (childrenNumber >= Config.getInstance().MAX_CHILDREN_NODES) {
                break;
            }

            if(beta <= alpha){
                break;
            }
        }

        parent.updateAdvantage(minMaxEval);

        return minMaxEval;
    }

    private MinMaxTreeNode initNode(MinMaxTreeNode parent, MutualLinkedMap<Path, StepMode> pathToTypeMap) {
        MinMaxTreeNode node = parent.getChildByPaths(pathToTypeMap.keySet());

        if(node == null) {
            node = new MinMaxTreeNode(pathToTypeMap, !parent.playerTurn);
        }

        parent.addChild(node);
        return node;
    }


    private int calculateAdvantage(ArtificialBoard board, MinMaxTreeNode node) {
        Path path = node.getByStepMode(StepMode.ATTACK);

        if (path == null) {
            return 0;
        }

        int advantage = board.getMaxHealthPoints(path.getTarget())
                - board.getHealthPoints(path.getTarget())
                + board.getDamagePoints(path.getTarget());

        return node.playerTurn ? -advantage : advantage;
    }

    @Override
    public void climbDown(MutualLinkedMap<Path, StepMode> targets) {
        MinMaxTreeNode node = root.getChildByPaths(targets.keySet());

        if (node == null) {
            node = new MinMaxTreeNode(targets, !root.playerTurn);
        }

        root.clear();
        root = node;
    }

}

class MinMaxTreeNode {
    protected MutualLinkedMap<Path, StepMode> pathToTypeMap;
    protected int advantage;
    protected boolean playerTurn;
    protected Set<MinMaxTreeNode> children;

    public MinMaxTreeNode() {
        pathToTypeMap = new MutualLinkedMap<>();
        children = new HashSet<>();
        playerTurn = false;
    }

    public MinMaxTreeNode(MutualLinkedMap<Path, StepMode> pathToTypeMap, boolean playerTurn) {
        this.pathToTypeMap = pathToTypeMap;
        children = new HashSet<>();
        this.playerTurn = playerTurn;
    }

    public void addChild(MinMaxTreeNode child) {
        children.add(child);
    }

    public MinMaxTreeNode getChildByPaths(Collection<Path> paths) {
        for (MinMaxTreeNode child : children) {
            if (child.pathToTypeMap.keySet().containsAll(paths)) {
                return child;
            }
        }

        return null;
    }

    public void updateAdvantage(int minMaxValue) {
        if(children.isEmpty()) {
            advantage = playerTurn ? Math.min(advantage, minMaxValue) : Math.max(advantage, minMaxValue);
        }
        else {
            for(MinMaxTreeNode child : children) {
                advantage = playerTurn
                        ? Math.min(advantage, child.advantage)
                        : Math.max(advantage, child.advantage);
            }
        }
    }

    public Path getByStepMode(StepMode stepMode) {
        return pathToTypeMap.getKey(stepMode);
    }

    public void clear() {
        children.clear();
        pathToTypeMap.clear();
    }

    public MinMaxTreeNode clone() {
        MinMaxTreeNode node = new MinMaxTreeNode();
        node.advantage = advantage;
        node.playerTurn = playerTurn;

        node.pathToTypeMap.putAll(pathToTypeMap);

        for(MinMaxTreeNode child : children) {
            node.addChild(child.clone());
        }

        return node;
    }

    @Override
    public String toString() {
        return pathToTypeMap.toString() + " " + advantage;
    }
}
