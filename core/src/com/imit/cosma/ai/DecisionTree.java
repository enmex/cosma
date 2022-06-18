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
import java.util.Stack;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DecisionTree{
    private final MoveGenerator generator;

    private TreeNode root;
    private int depth;

    private Board board;
    private final Stack<Board> states;

    private boolean isCaching;

    public DecisionTree(Board board, int depth){
        generator = new MoveGenerator(board);
        this.board = board.clone();
        states = new Stack<>();

        root = new TreeNode();

        this.depth = depth;

        isCaching = true;
    }

    private int buildTree(int depth, int alpha, int beta, boolean playerTurn, TreeNode parent){
        int advantage;
        doTurn(parent.path);

        if(board.sideCompletedTurn()){
            playerTurn = !playerTurn;
            depth--;

            board.updateSide();
        }

        if(depth == 0 || board.isGameOver()){
            undoTurn();

            int nodeAdvantage = calculatePathAdvantage(parent.path, !playerTurn);
            parent.advantage = nodeAdvantage;

            return nodeAdvantage;
        }

        List<Path> paths = new ArrayList<>(playerTurn ? generator.getPlayerPaths() : generator.getEnemyPaths());
        int minMaxEval = playerTurn ? Integer.MAX_VALUE : -Integer.MAX_VALUE;

        for(Path path : paths){
            TreeNode currentNode = new TreeNode(parent, path, playerTurn);

            advantage = buildTree(depth, alpha, beta, playerTurn, currentNode);

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
        undoTurn();

        parent.advantage = minMaxEval;
        return minMaxEval;
    }

    public void update(Board board){
        this.board = board.clone();
        generator.update(board);
    }

    public Board getBoard() {
        return board;
    }

    private int calculatePathAdvantage(Path path, boolean playerTurn){
        int advantage = board.getMaxHealthPoints(path.getTarget())
                - board.getHealthPoints(path.getTarget())
                + board.getDamagePoints(path.getTarget());

        return playerTurn ? -advantage : advantage;
    }


    private void doTurn(Path path){
        states.push(board.clone());
        board.makeArtificialTurn(path);
        update(board);
    }

    private void undoTurn(){
        if(!states.isEmpty()) {
            board.set(states.pop());
            update(board);
        }
    }

    public void cacheTree() throws InterruptedException {
        List<Path> availablePaths = new ArrayList<>(generator.getEnemyPaths());

        ExecutorService threadPool = Executors.newCachedThreadPool();

        System.out.println("Подождите...");
        double donePercents = 0;
        double part = 1. / availablePaths.size();
        for(Path availablePath : availablePaths) {
            TreeNode child = new TreeNode(root, availablePath, false);
            root.addChild(child);
            System.out.printf("Загрузка...%.2f%%\n", (donePercents * 100));
            buildTree(depth, -Integer.MAX_VALUE, Integer.MAX_VALUE, false, child);
            donePercents += part;
        }
        threadPool.awaitTermination(1, TimeUnit.MINUTES);
        threadPool.shutdown();
        isCaching = false;
        System.out.println("Загрузка завершена");
    }

    public void climbDown(Path target) {
        TreeNode node = root.getChild(target);
        root.clear();
        root = node;

        climbDownAndAddLevel(root, depth);
    }

    public Map<Path, Integer> getRootChildren() {
        Map<Path, Integer> pathToAdvantageMap = new HashMap<>();

        for(TreeNode child : root.children) {
            pathToAdvantageMap.put(child.path, child.advantage);
        }

        return pathToAdvantageMap;
    }

    private void climbDownAndAddLevel(TreeNode currentNode, int depth) {
        if(depth == 0) {
            buildTree(1, -Integer.MAX_VALUE, Integer.MAX_VALUE, currentNode.playerTurn, currentNode);
        }

        for(TreeNode child : currentNode.children) {
            climbDownAndAddLevel(child, depth - 1);
        }
    }

    public boolean isCaching() {
        return isCaching;
    }
}

class TreeNode {
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

    public TreeNode(TreeNode parent, Path path, boolean playerTurn) {
        this.parent = parent;
        this.path = path;
        children = new HashSet<>();
        parent.addChild(this);
        this.playerTurn = playerTurn;
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