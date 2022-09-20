package com.imit.cosma.ai;

import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.util.MutualLinkedMap;
import com.imit.cosma.util.Path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DecisionTree{
    private TreeNode root;

    private final int SEARCH_LIMIT = 3;

    private final boolean isCaching;

    public DecisionTree(){
        root = new TreeNode();
        isCaching = false;
    }

    public MutualLinkedMap<Path, StepMode> treeSearch(ArtificialBoard board) {
        for(int i = 0; i < SEARCH_LIMIT; i++) {
            TreeNode node = selectNode(board, root);
            int reward = node.stateReward;
            backpropogate(node, reward);
        }

        TreeNode bestChild = selectBestNode(root);

        return bestChild.pathToTypeMap;
    }

    private void expandNode(ArtificialBoard board, TreeNode current) {
        int childrenNumber = 0;
        MoveGenerator generator = new MoveGenerator(board);

        List<Path> firstPaths = current.playerTurn
                ? new ArrayList<>(generator.getEnemyPaths())
                : new ArrayList<>(generator.getPlayerPaths());

        for(Path firstPath : firstPaths) {
            ArtificialBoard firstTurnBoard = board.clone();

            StepMode firstTurnMode = firstTurnBoard.doTurn(firstPath);
            generator.update(firstTurnBoard);
            firstTurnBoard.updateSide();

            List<Path> pathsForSecondTurn = current.playerTurn
                    ? new ArrayList<>(generator.getEnemyPaths())
                    : new ArrayList<>(generator.getPlayerPaths());

            if(pathsForSecondTurn.isEmpty()) {
                MutualLinkedMap<Path, StepMode> pathToTypeMap = new MutualLinkedMap<>();
                pathToTypeMap.put(firstPath, firstTurnMode);

                initNode(firstTurnBoard, current, pathToTypeMap);
                childrenNumber++;
            } else {
                for(Path secondPath : pathsForSecondTurn) {

                    ArtificialBoard secondTurnBoard = firstTurnBoard.clone();

                    StepMode secondMode = secondTurnBoard.doTurn(secondPath);

                    MutualLinkedMap<Path, StepMode> pathToTypeMap = new MutualLinkedMap<>();
                    pathToTypeMap.put(secondPath, secondMode);
                    pathToTypeMap.put(firstPath, firstTurnMode);

                    initNode(secondTurnBoard, current, pathToTypeMap);
                    childrenNumber++;

                    if(childrenNumber >= SEARCH_LIMIT) {
                        break;
                    }
                }
            }

            if(childrenNumber >= SEARCH_LIMIT) {
                break;
            }
        }
    }

    public void climbDown(MutualLinkedMap<Path, StepMode> paths) {
        TreeNode node = root.getChild(paths.keySet());

        if(node == null) {
            node = new TreeNode(paths, !root.playerTurn);
        }

        root.clear();
        root = node;
    }

    private void backpropogate(TreeNode current, int reward) {
        while(current != null) {
            current.visits++;
            current.totalReward += reward;
            current = current.parent;
        }
    }

    private TreeNode selectNode(ArtificialBoard board, TreeNode current) {
        if(current.children.isEmpty()) {
            expandNode(board, current);
        }

        return selectBestNode(current);
    }

    private TreeNode selectBestNode(TreeNode parentNode) {
        if(parentNode.children.isEmpty()) {
            return parentNode;
        }

        TreeNode current = parentNode;
        for(TreeNode child : parentNode.children) {
            if(child.visits == 0 && child.totalReward == 0) {
                return child;
            }
            if(child.getUCB() > current.getUCB()) {
                current = child;
            }
        }

        return selectBestNode(current);
    }

    private void initNode(ArtificialBoard board, TreeNode parent, MutualLinkedMap<Path, StepMode> pathToTypeMap) {
        TreeNode node = parent.getChild(pathToTypeMap.keySet());

        if(node == null) {
            node = new TreeNode(parent, pathToTypeMap);
            node.setAdvantage(board);
            parent.addChild(node);
        }
    }

    public boolean isCaching() {
        return isCaching;
    }

}

class TreeNode {
    protected MutualLinkedMap<Path, StepMode> pathToTypeMap;
    protected boolean playerTurn;
    protected int totalReward;
    protected int stateReward;
    protected int visits;
    protected TreeNode parent;
    protected boolean isInitiated;
    protected Set<TreeNode> children;

    public TreeNode() {
        pathToTypeMap = new MutualLinkedMap<>();
        children = new HashSet<>();
        playerTurn = false;
        isInitiated = false;
    }

    public TreeNode(TreeNode parent, MutualLinkedMap<Path, StepMode> pathToTypeMap) {
        this.pathToTypeMap = pathToTypeMap;
        children = new HashSet<>();
        this.playerTurn = !parent.playerTurn;
        this.parent = parent;
    }

    public TreeNode(MutualLinkedMap<Path, StepMode> pathToTypeMap, boolean playerTurn) {
        this.pathToTypeMap = pathToTypeMap;
        this.playerTurn = playerTurn;
        children = new HashSet<>();

    }

    public void addChild(TreeNode child) {
        children.add(child);
    }

    public TreeNode getChild(Collection<Path> paths) {
        for(TreeNode child : children) {
            if(child.pathToTypeMap.keySet().containsAll(paths)) {
                return child;
            }
        }
        return null;
    }

    public double getUCB() {
        if(parent == null) {
            return 0;
        }

        if(parent.visits == 0 || visits == 0) {
            return Double.MAX_VALUE;
        }

        return totalReward + Math.sqrt(Math.log(parent.visits) / visits);
    }

    public void setAdvantage(ArtificialBoard board) {
        Path path = getByStepMode(StepMode.ATTACK);

        if(path == null) {
            totalReward = 0;
        } else {

            this.stateReward = board.getMaxHealthPoints(path.getTarget())
                    - board.getHealthPoints(path.getTarget())
                    + board.getDamagePoints(path.getTarget());
        }
    }

    public Path getByStepMode(StepMode stepMode) {
        return pathToTypeMap.getKey(stepMode);
    }

    public void clear() {
        parent = null;
        for(TreeNode child : children) {
            child.parent = null;
        }

        children.clear();
        pathToTypeMap.clear();
    }

    public TreeNode clone() {
        TreeNode node = new TreeNode();
        node.totalReward = totalReward;
        node.playerTurn = playerTurn;

        node.pathToTypeMap.putAll(pathToTypeMap);

        for(TreeNode child : children) {
            node.addChild(child.clone());
        }

        return node;
    }

    @Override
    public String toString() {
        return totalReward + "/" + visits;
    }
}
