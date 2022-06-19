package com.imit.cosma.ai;

import com.imit.cosma.model.board.Board;
import com.imit.cosma.util.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DecisionTree{
    private TreeNode root;
    private int depth;

    private boolean isCaching;

    public DecisionTree(int depth){
        root = new TreeNode();
        this.depth = depth;
        isCaching = true;
    }

    private int buildTree(Board board, MoveGenerator generator, int depth, int alpha, int beta, boolean playerTurn, TreeNode parent){
        int advantage;

        board.makeArtificialTurn(parent.path);

        if(board.sideCompletedTurn()){
            playerTurn = !playerTurn;
            depth--;

            board.updateSide();
        }

        if(depth == 0 || board.isGameOver()){
            int nodeAdvantage = calculatePathAdvantage(board, parent.path, !playerTurn);
            parent.advantage = nodeAdvantage;

            return nodeAdvantage;
        }

        List<Path> paths = new ArrayList<>(playerTurn ? generator.getPlayerPaths() : generator.getEnemyPaths());
        int minMaxEval = playerTurn ? Integer.MAX_VALUE : -Integer.MAX_VALUE;

        for(Path path : paths){
            TreeNode currentNode = new TreeNode(board, parent, path, playerTurn);

            advantage = buildTree(board.clone(), generator.clone(), depth, alpha, beta, playerTurn, currentNode);

            if(playerTurn){
                minMaxEval = Math.min(minMaxEval, advantage);
                beta = Math.min(advantage, beta);
            }
            else{
                minMaxEval = Math.max(minMaxEval, advantage);
                alpha = Math.max(advantage, alpha);
            }
            if(beta <= alpha){
                break;
            }
        }

        parent.advantage = minMaxEval;
        return minMaxEval;
    }

    private int calculatePathAdvantage(Board board, Path path, boolean playerTurn){
        int advantage = board.getMaxHealthPoints(path.getTarget())
                - board.getHealthPoints(path.getTarget())
                + board.getDamagePoints(path.getTarget());

        return playerTurn ? -advantage : advantage;
    }

    public void cacheTree(final Board board) throws InterruptedException {
        final MoveGenerator generator = new MoveGenerator(board);
        List<Path> availablePaths = new ArrayList<>(generator.getEnemyPaths());

        for(Path path : availablePaths) {
            TreeNode child = new TreeNode(board, root, path, false);
            root.addChild(child);
            buildTree(board.clone(), generator.clone(), depth, -Integer.MAX_VALUE, Integer.MAX_VALUE, false, child);
        }

        isCaching = false;
    }

    public void climbDown(Board board, MoveGenerator generator, Path target) {
        TreeNode node = root.getChild(target);
        root.clear();
        root = node;

        climbDownAndAddLevel(board.clone(), generator.clone(), root, depth);
    }

    public Map<Path, Integer> getRootChildren() {
        Map<Path, Integer> pathToAdvantageMap = new HashMap<>();

        for(TreeNode child : root.children) {
            pathToAdvantageMap.put(child.path, child.advantage);
        }

        return pathToAdvantageMap;
    }

    private void climbDownAndAddLevel(Board board, MoveGenerator generator, TreeNode currentNode, int depth) {
        if(depth == 0) {
            buildTree(board.clone(), generator.clone(),1, -Integer.MAX_VALUE, Integer.MAX_VALUE, currentNode.playerTurn, currentNode);
        }

        for(TreeNode child : currentNode.children) {
            climbDownAndAddLevel(board.clone(), generator.clone(), child, depth - 1);
        }
    }

    public boolean isCaching() {
        return isCaching;
    }
}

class TreeNode {
    protected Board board;
    protected Path path;
    protected int advantage;
    protected boolean playerTurn;
    protected TreeNode parent;
    protected Set<TreeNode> children;

    public TreeNode() {
        this.parent = null;
        this.path = new Path();
        children = new HashSet<>();
        playerTurn = false;
    }

    public TreeNode(Board board, TreeNode parent, Path path, boolean playerTurn) {
        this.parent = parent;
        this.path = path;
        children = new HashSet<>();
        parent.addChild(this);
        this.playerTurn = playerTurn;
        this.board = board;
    }

    public void addChild(TreeNode child) {
        children.add(child);
    }

    public TreeNode getChild(Path path) {
        for(TreeNode child : children) {
            if(child.path.equals(path)) {
                return child.clone();
            }
        }
        return null;
    }

    public void clear() {
        children.clear();
    }

    public TreeNode clone() {
        TreeNode node = new TreeNode();
        node.path = path;
        node.advantage = advantage;
        node.playerTurn = playerTurn;
        for(TreeNode child : children) {
            node.addChild(child.clone());
        }

        return node;
    }

    public int size() {
        int size = 1;

        for(TreeNode child : children) {
            size += child.size();
        }

        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TreeNode treeNode = (TreeNode) o;
        return advantage == treeNode.advantage && path.equals(treeNode.path) && children.equals(treeNode.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, advantage, children);
    }
}