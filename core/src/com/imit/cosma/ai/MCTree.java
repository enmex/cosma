package com.imit.cosma.ai;

import com.imit.cosma.model.rules.TurnType;
import com.imit.cosma.pkg.random.Randomizer;
import com.imit.cosma.util.Path;

import java.util.ArrayList;
import java.util.List;

public class MCTree extends DecisionTree {
    private final int SIMULATION_DEPTH = 5;
    private MCTreeNode root;
    private ArtificialBoard board;

    public MCTree(ArtificialBoard board){
        super(board);
        root = new MCTreeNode();
    }

    @Override
    public Path<Integer> treeSearch(ArtificialBoard board) {
        this.board = board;
        for (int i = 0; i < 10; i++) {
            if(root.children.isEmpty()) {
                expand(root);
            }
            MCTreeNode bestNode = select(root);
            expand(bestNode);
            MCTreeNode randomChildNode = Randomizer.getRandom(bestNode.children);
            simulate(randomChildNode);
            backpropogate(randomChildNode, randomChildNode.reward);
        }

        MCTreeNode current = new MCTreeNode();
        for(MCTreeNode child : root.children) {
            if(child.getUCB() > current.getUCB()) {
                current = child;
            }
        }

        return current.path;
    }

    private void expand(MCTreeNode current) {
        PathGenerator generator = new PathGenerator(board);
        List<Path<Integer>> paths = current.playerTurn
                ? new ArrayList<>(generator.getEnemyPaths())
                : new ArrayList<>(generator.getPlayerPaths());

        for(Path<Integer> path : paths) {
            ArtificialBoard artificialBoard = board.clone();
            TurnType turnType = artificialBoard.doTurn(path);
            generator.update(artificialBoard);
            artificialBoard.updateSide();
            initNode(current, path, turnType);
        }
    }

    private void simulate(MCTreeNode parentNode) {
        ArtificialBoard clonedBoard = board.clone();
        MCTreeNode node = parentNode.clone();

        int totalReward = 0;

        for (int i = 0; i < SIMULATION_DEPTH; i++) {
            Path<Integer> path = PathGenerator.getRandomShipPath(clonedBoard);
            TurnType turnType = clonedBoard.getTurnTypeByPath(path);
            MCTreeNode childNode = new MCTreeNode(node, path, turnType);
            childNode.setAdvantage(clonedBoard, path);
            totalReward += childNode.reward;
            node.addChild(childNode);
            node = childNode;
            clonedBoard.doTurn(path);
        }

        parentNode.reward = totalReward;
    }

    @Override
    public void climbDown(Path<Integer> path, TurnType turnType) {
        MCTreeNode node = root.getChild(path);

        if(node == null) {
            node = new MCTreeNode(path, turnType, !root.playerTurn);
        }
        root = node;
    }

    private void backpropogate(MCTreeNode current, int reward) {
        while(current != null) {
            current.visits++;
            current.reward = reward;
            current = current.parent;
        }
    }

    private MCTreeNode select(MCTreeNode parentNode) {
        if(parentNode.children.isEmpty()) {
            return parentNode;
        }

        MCTreeNode current = new MCTreeNode();
        for(MCTreeNode child : parentNode.children) {
            if(child.getUCB() > current.getUCB()) {
                current = child;
            }
        }

        return select(current);
    }

    private void initNode(MCTreeNode parent, Path<Integer> path, TurnType turnType) {
        MCTreeNode node = parent.getChild(path);

        if(node == null) {
            node = new MCTreeNode(parent, path, turnType);
            parent.addChild(node);
        }
    }

}

class MCTreeNode {
    protected Path<Integer> path;
    protected TurnType turnType;
    protected boolean playerTurn;
    protected int reward;
    protected int visits;
    protected MCTreeNode parent;
    protected boolean isInitiated;
    protected List<MCTreeNode> children;

    public MCTreeNode() {
        children = new ArrayList<>();
        playerTurn = false;
        isInitiated = false;
    }

    public MCTreeNode(MCTreeNode parent, Path<Integer> path, TurnType turnType) {
        children = new ArrayList<>();
        this.path = path;
        this.turnType = turnType;
        this.playerTurn = !parent.playerTurn;
        this.parent = parent;
    }

    public MCTreeNode(Path<Integer> path, TurnType turnType, boolean playerTurn) {
        children = new ArrayList<>();
        this.path = path;
        this.turnType = turnType;
        this.playerTurn = playerTurn;
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
        if(parent == null) {
            return 0;
        }

        if(parent.visits == 0 || visits == 0) {
            return Double.MAX_VALUE;
        }

        return (double) reward / visits + Math.sqrt(Math.log(parent.visits) / visits);
    }

    public void setAdvantage(ArtificialBoard board, Path<Integer> path) {
        int hp = board.getHealthPoints(path.getTarget());
        reward = hp != 0 ? board.getDamagePoints(path.getTarget()) / board.getHealthPoints(path.getTarget()) : 0;
    }

    public MCTreeNode clone() {
        MCTreeNode node = new MCTreeNode();
        node.reward = reward;
        node.playerTurn = playerTurn;
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
        return String.format("(%d/%d), hasAttack=%b", reward, visits, turnType);
    }
}
