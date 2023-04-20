package com.imit.cosma.ai;

import com.imit.cosma.model.rules.TurnType;
import com.imit.cosma.pkg.random.Randomizer;
import com.imit.cosma.util.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MCTree extends DecisionTree {
    private MCTreeNode root;
    private ThreadPoolExecutor threadPool;

    public MCTree(){
        root = new MCTreeNode();
        threadPool = new ThreadPoolExecutor(6, 50, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10));
    }

    @Override
    public void treeSearch(ArtificialBoard board) {
        if(root.getChildren().isEmpty()) {
            expand(board, root, !board.getTurn().isPlayer());
        }
        for (int i = 0; i < 10; i++) {
            MCTreeNode bestNode = select(root);
            board.doTurn(bestNode.getPath());
            expand(board, bestNode, !board.getTurn().isPlayer());
            MCTreeNode randomChildNode = Randomizer.getRandom(bestNode.getChildren());
            board.doTurn(randomChildNode.getPath());
            simulate(board, randomChildNode);
            backpropogate(randomChildNode, randomChildNode.getReward());
            bestNode.setLocked(false);
        }
    }

    @Override
    public Path<Integer> getPath() {
        MCTreeNode current = new MCTreeNode();
        for(MCTreeNode child : root.getChildren()) {
            if(child.getUCB() > current.getUCB()) {
                current = child;
            }
        }

        return current.getPath();
    }

    public void expand(ArtificialBoard board, MCTreeNode current, boolean playerTurn) {
        PathGenerator generator = new PathGenerator(board);
        List<Path<Integer>> paths = playerTurn
                ? new ArrayList<>(generator.getEnemyPaths())
                : new ArrayList<>(generator.getPlayerPaths());

        for(Path<Integer> path : paths) {
            ArtificialBoard artificialBoard = board.clone();
            TurnType turnType = artificialBoard.doTurn(path);
            generator.update(artificialBoard);
            MCTreeNode node = current.getChild(path);

            if(node == null) {
                node = new MCTreeNode(current, path, turnType);
                node.setAdvantage(board);
                if (board.isGameOver()) {
                    node.setTerminal(true);
                }
                current.addChild(node);
            }
        }
    }

    public void simulate(ArtificialBoard board, MCTreeNode parentNode) {
        ArtificialBoard clonedBoard = board.clone();
        MCTreeNode node = parentNode.clone();

        int totalReward = 0;



        parentNode.setReward(totalReward);
    }

    @Override
    public void climbDown(Path<Integer> path, TurnType turnType) {
        MCTreeNode node = root.getChild(path);

        if(node == null) {
            node = new MCTreeNode(path, turnType);
        }
        root = node;
    }

    public void backpropogate(MCTreeNode current, double reward) {
        while(current != null) {
            current.addVisit();
            current.addReward(reward);
            current = current.getParent();
        }
    }

    public MCTreeNode select(MCTreeNode parentNode) {
        if(parentNode.getChildren().isEmpty()) {
            return parentNode;
        }

        MCTreeNode current = new MCTreeNode();
        boolean isNullNode = true;
        for(MCTreeNode child : parentNode.getChildren()) {
            if(!child.isLocked() && child.getUCB() > current.getUCB()) {
                current = child;
                isNullNode = false;
            }
        }

        return isNullNode ? Randomizer.getRandom(parentNode.getChildren()) : current;
    }
}

class MCTreeNode {
    private boolean locked;
    private Path<Integer> path;
    private TurnType turnType;

    private double reward;
    private int visits;

    private boolean isTerminal;

    private MCTreeNode parent;
    private final List<MCTreeNode> children;

    public MCTreeNode() {
        children = new ArrayList<>();
    }

    public MCTreeNode(Path<Integer> path, TurnType turnType) {
        children = new ArrayList<>();
        this.path = path;
        this.turnType = turnType;
        isTerminal = false;
    }

    public MCTreeNode(MCTreeNode parent, Path<Integer> path, TurnType turnType) {
        children = new ArrayList<>();
        this.path = path;
        this.turnType = turnType;
        this.parent = parent;
        isTerminal = false;
    }

    public void addChild(MCTreeNode child) {
        children.add(child);
    }

    public MCTreeNode getChild(Path<Integer> path) {
        for(MCTreeNode child : children) {
            if(child.path.equals(path)) {
                return child;
            }
        }
        return null;
    }

    public void setTerminal(boolean terminal) {
        isTerminal = terminal;
    }

    public double getUCB() {
        if(parent == null) {
            return 0;
        }

        if(parent.visits == 0 || visits == 0) {
            return Double.MAX_VALUE;
        }

        return reward / visits + Math.sqrt(Math.log(parent.visits) / visits);
    }

    public boolean isTerminal() {
        return isTerminal;
    }

    public void setAdvantage(ArtificialBoard board) {
        int hp = board.getHealthPoints(path.getTarget());
        reward = hp != 0 ? (double) board.getDamagePoints(path.getTarget()) / board.getHealthPoints(path.getTarget()) : 0;
    }

    public void setReward(double reward) {
        this.reward = reward;
    }

    public double getReward() {
        return reward;
    }

    public List<MCTreeNode> getChildren() {
        return children;
    }

    public MCTreeNode getParent() {
        return parent;
    }

    public Path<Integer> getPath() {
        return path;
    }

    public TurnType getTurnType() {
        return turnType;
    }

    public void addVisit() {
        visits++;
    }

    public void addReward(double reward) {
        this.reward += reward;
    }

    public void setTurnType(TurnType turnType) {
        this.turnType = turnType;
    }

    public void setPath(Path<Integer> path) {
        this.path = path;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public MCTreeNode clone() {
        MCTreeNode node = new MCTreeNode();
        node.reward = reward;
        node.path = path.clone();
        node.turnType = turnType;
        for(MCTreeNode child : children) {
            node.addChild(child.clone());
        }
        return node;
    }

    @SuppressWarnings("DefaultLocale")
    @Override
    public String toString() {
        return String.format("(%f/%d) path = %s, attack = %b", reward, visits, path, turnType == TurnType.ATTACK);
    }
}
