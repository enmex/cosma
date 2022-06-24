package com.imit.cosma.ai;

import com.imit.cosma.model.board.Board;
import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.util.MutualLinkedMap;
import com.imit.cosma.util.Path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sun.security.util.Cache;

public class DecisionTree{
    private TreeNode root;
    private int depth;

    private Cache<Integer, TreeNode> cache;
    private int treeSize;

    private boolean isCaching;

    public DecisionTree(int depth){
        root = new TreeNode();
        this.depth = depth;
        isCaching = true;
        treeSize = 0;
    }
/*
    private int buildTree(Board board, int depth, int alpha, int beta, TreeNode parent) {
        if(depth == 0) {
            return calculateAdvantage(board, parent);
        }

        treeSize++;

        MoveGenerator generator = new MoveGenerator(board);

        List<Path> pathsForFirstTurn = parent.playerTurn
                ? new ArrayList<>(generator.getEnemyPaths())
                : new ArrayList<>(generator.getPlayerPaths());

        int minMaxEval = parent.playerTurn ? Integer.MAX_VALUE : -Integer.MAX_VALUE;

        for(Path firstPath : pathsForFirstTurn) {
            Board firstTurnBoard = board.clone();

            StepMode firstTurnMode = firstTurnBoard.doArtificialTurn(firstPath);
            generator.update(firstTurnBoard);
            firstTurnBoard.updateSide();

            List<Path> pathsForSecondTurn = parent.playerTurn
                    ? new ArrayList<>(generator.getEnemyPaths())
                    : new ArrayList<>(generator.getPlayerPaths());

            TreeNode node;

            if(pathsForSecondTurn.isEmpty()) {
                MutualLinkedMap<Path, StepMode> pathToTypeMap = new MutualLinkedMap<>();
                pathToTypeMap.put(firstPath, firstTurnMode);

                node = initNode(parent, pathToTypeMap);

                node.advantage = buildTree(firstTurnBoard.clone(), depth - 1, alpha, beta, node);

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

                    Board secondTurnBoard = firstTurnBoard.clone();

                    StepMode secondMode = secondTurnBoard.doArtificialTurn(secondPath);

                    MutualLinkedMap<Path, StepMode> pathToTypeMap = new MutualLinkedMap<>();
                    pathToTypeMap.put(secondPath, secondMode);
                    pathToTypeMap.put(firstPath, firstTurnMode);

                    node = initNode(parent, pathToTypeMap);

                    int advantage = buildTree(firstTurnBoard.clone(), depth - 1, alpha, beta, node);

                    if(parent.playerTurn){
                        minMaxEval = Math.min(minMaxEval, advantage);
                        beta = Math.min(node.advantage, beta);
                    }
                    else{
                        minMaxEval = Math.max(minMaxEval, advantage);
                        alpha = Math.max(node.advantage, alpha);
                    }

                    if(beta <= alpha){
                        break;
                    }
                }
            }

            if(beta <= alpha){
                break;
            }
        }

        parent.updateAdvantage(minMaxEval);

        cache.put();

        return minMaxEval;
    }
*/

    private void selection() {
        TreeNode current = root;
        for(TreeNode child : current.children) {
            if(child.wins > current.wins) {
                current = child;
            }
        }
    }

    private void expansion(TreeNode bestNode) {
        TreeNode newNode = new TreeNode();
        newNode.parent = bestNode;
        bestNode.children.add(newNode);
    }

    private void simulation() {

    }

    private int calculateAdvantage(Board board, TreeNode node){
        Path path = node.getByStepMode(StepMode.ATTACK);

        if(path == null) {
            return 0;
        }

        int advantage = board.getMaxHealthPoints(path.getTarget())
                - board.getHealthPoints(path.getTarget())
                + board.getDamagePoints(path.getTarget());

        return node.playerTurn ? -advantage : advantage;
    }

    public void cacheTree(final Board board) {
        buildTree(board, depth, -Integer.MAX_VALUE, Integer.MAX_VALUE, root);
        isCaching = false;
    }

    public void climbDown(Board board, MutualLinkedMap<Path, StepMode> targets) {
        root = cache.get(1);

        TreeNode node = root.getChild(targets.keySet());

        if(node == null) {
            node = new TreeNode(targets, !root.playerTurn);
        }

        root.clear();
        root = node;

        buildTree(board.clone(), depth, -Integer.MAX_VALUE, Integer.MAX_VALUE, root);
    }

    public Map<MutualLinkedMap<Path, StepMode>, Integer> getRootChildren() {
        Map<MutualLinkedMap<Path, StepMode>, Integer> pathToAdvantageMap = new HashMap<>();

        for(TreeNode child : root.children) {
            pathToAdvantageMap.put(child.pathToTypeMap, child.advantage);
        }

        return pathToAdvantageMap;
    }

    private TreeNode initNode(TreeNode parent, MutualLinkedMap<Path, StepMode> pathToTypeMap) {
        TreeNode node = parent.getChild(pathToTypeMap.keySet());

        if(node == null) {
            node = new TreeNode(pathToTypeMap, !parent.playerTurn);
        }

        parent.addChild(node);
        return node;
    }

    public boolean isCaching() {
        return isCaching;
    }

}

//Cached TreeNode: 1|0011|2123|MA|P|NNNN

class TreeNode {
    protected MutualLinkedMap<Path, StepMode> pathToTypeMap;
    protected int advantage;
    protected boolean playerTurn;
    protected int wins;
    protected TreeNode parent;
    protected Set<TreeNode> children;

    public TreeNode() {
        pathToTypeMap = new MutualLinkedMap<>();
        children = new HashSet<>();
        playerTurn = false;
    }

    public TreeNode(MutualLinkedMap<Path, StepMode> pathToTypeMap, boolean playerTurn) {
        this.pathToTypeMap = pathToTypeMap;
        children = new HashSet<>();
        this.playerTurn = playerTurn;
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

    public void updateAdvantage(int minMaxValue) {
        if(children.isEmpty()) {
            advantage = playerTurn ? Math.min(advantage, minMaxValue) : Math.max(advantage, minMaxValue);
        }
        else {
            for(TreeNode child : children) {
                advantage = playerTurn ? Math.min(advantage, child.advantage) : Math.max(advantage, child.advantage);
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

    public TreeNode clone() {
        TreeNode node = new TreeNode();
        node.advantage = advantage;
        node.playerTurn = playerTurn;

        node.pathToTypeMap.putAll(pathToTypeMap);

        for(TreeNode child : children) {
            node.addChild(child.clone());
        }

        return node;
    }
}
