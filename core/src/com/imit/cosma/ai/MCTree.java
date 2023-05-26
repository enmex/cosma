package com.imit.cosma.ai;

import com.imit.cosma.model.rules.TurnType;
import com.imit.cosma.pkg.random.Randomizer;
import com.imit.cosma.util.Path;

import java.util.ArrayList;
import java.util.List;

public class MCTree extends DecisionTree {
    private MCTreeNode root;

    public MCTree(){
        root = new MCTreeNode();
    }

    @Override
    public void treeSearch(ArtificialBoard board) {
        if(root.getChildren().isEmpty()) {
            expand(board, root, !board.getTurn().isPlayer());
        }
        for (int i = 0; i < root.getChildren().size(); i++) {
            ArtificialBoard clonedBoard = board.clone();
            MCTreeNode bestNode = select(root);
            clonedBoard.doTurn(bestNode.getPath());
            if (bestNode.getChildren().isEmpty()) {
                expand(clonedBoard, bestNode, !clonedBoard.getTurn().isPlayer());
            }
            MCTreeNode randomChildNode = Randomizer.getRandom(bestNode.getChildren());
            clonedBoard.doTurn(randomChildNode.getPath());
            simulate(clonedBoard, randomChildNode);
            backpropogate(randomChildNode, randomChildNode.getReward());
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
                node.setReward(board);
                if (board.isGameOver()) {
                    node.setTerminal(true);
                }
                current.addChild(node);
            }
        }
    }

    public void simulate(ArtificialBoard board, MCTreeNode parentNode) {
        ArtificialBoard clonedBoard = board.clone();
        int totalReward = 0;
        for (int i = 0; i < 5 && !clonedBoard.isGameOver(); i++) {
            Path<Integer> path = PathGenerator.getRandomShipPath(clonedBoard);
            totalReward += clonedBoard.calculateReward(path);
            clonedBoard.doTurn(path);
        }
        parentNode.addReward(totalReward);
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
            if(child.getUCB() > current.getUCB()) {
                current = child;
                isNullNode = false;
            }
        }

        return isNullNode ? Randomizer.getRandom(parentNode.getChildren()) : current;
    }
}

class MCTreeNode {
    private Path<Integer> path;
    private TurnType turnType;

    private boolean terminal;
    private double reward;
    private int visits;

    private MCTreeNode parent;
    private final List<MCTreeNode> children;

    public MCTreeNode() {
        children = new ArrayList<>();
    }

    public MCTreeNode(Path<Integer> path, TurnType turnType) {
        children = new ArrayList<>();
        this.path = path;
        this.turnType = turnType;
    }

    public MCTreeNode(MCTreeNode parent, Path<Integer> path, TurnType turnType) {
        children = new ArrayList<>();
        this.path = path;
        this.turnType = turnType;
        this.parent = parent;
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

    public double getUCB() {
        if(parent == null || parent.visits == 0 || visits == 0) {
            return Double.NEGATIVE_INFINITY;
        }

        return reward / visits + Math.sqrt(Math.log(parent.visits) / visits);
    }

    public void setReward(ArtificialBoard board) {
        reward = board.calculateReward(path);
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

    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    public boolean isTerminal() {
        return terminal;
    }
}
