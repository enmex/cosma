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

public class MonteCarloTree extends DecisionTree {
    private MonteCarloTreeNode root;
    private ArtificialBoard board;

    public MonteCarloTree(ArtificialBoard board){
        super(board);
        root = new MonteCarloTreeNode();
        expand(root);
    }

    @Override
    public MutualLinkedMap<Path, StepMode> treeSearch(ArtificialBoard board) {
        this.board = board;
        if(root.children.isEmpty()) {
            expand(root);
        }

        MonteCarloTreeNode bestNode = selectBestNode(root);

        expand(bestNode);

        simulate(bestNode);

        backpropogate(bestNode, bestNode.totalReward);

        MonteCarloTreeNode bestChild = selectBestNode(root);

        if (bestChild.pathToTypeMap == null) {
            System.out.println();
        }
        return bestChild.pathToTypeMap;
    }

    private void expand(MonteCarloTreeNode current) {
        MoveGenerator generator = new MoveGenerator(board);
        int childrenNumber = 0;

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

                    if (childrenNumber >= Config.getInstance().MAX_CHILDREN_NODES) {
                        break;
                    }
                }
            }
            if (childrenNumber >= Config.getInstance().MAX_CHILDREN_NODES) {
                break;
            }
        }
    }

    private void simulate(MonteCarloTreeNode parentNode) {
        MonteCarloTreeNode node = parentNode.clone();
        expand(node);

        double maxUCB = -Double.MAX_VALUE;
        MonteCarloTreeNode bestNode = null;
        for (MonteCarloTreeNode child : node.children) {
            if (maxUCB < child.getUCB()) {
                bestNode = child;
                maxUCB = child.getUCB();
            }
        }

        if (bestNode != null) {
            parentNode.totalReward += bestNode.stateReward;
        }
    }

    @Override
    public void climbDown(MutualLinkedMap<Path, StepMode> paths) {
        MonteCarloTreeNode node = root.getChild(paths.keySet());

        if(node == null) {
            node = new MonteCarloTreeNode(paths, !root.playerTurn);
        }
        //root.clear();
        root = node;
    }

    private void backpropogate(MonteCarloTreeNode current, int reward) {
        while(current != null) {
            current.visits++;
            current.totalReward += reward;
            current = current.parent;
        }
    }

    private MonteCarloTreeNode selectBestNode(MonteCarloTreeNode parentNode) {
        if(parentNode.children.isEmpty()) {
            return parentNode;
        }

        MonteCarloTreeNode current = new MonteCarloTreeNode();
        for(MonteCarloTreeNode child : parentNode.children) {
            if(child.getUCB() > current.getUCB()) {
                current = child;
            }
        }

        return selectBestNode(current);
    }

    private void initNode(ArtificialBoard board, MonteCarloTreeNode parent, MutualLinkedMap<Path, StepMode> pathToTypeMap) {
        MonteCarloTreeNode node = parent.getChild(pathToTypeMap.keySet());

        if(node == null) {
            node = new MonteCarloTreeNode(parent, pathToTypeMap);
            node.setAdvantage(board);
            parent.addChild(node);
        }
    }

    public boolean isCaching() {
        return false;
    }

}

class MonteCarloTreeNode {
    protected MutualLinkedMap<Path, StepMode> pathToTypeMap;
    protected boolean playerTurn;
    protected int totalReward;
    protected int stateReward;
    protected int visits;
    protected MonteCarloTreeNode parent;
    protected boolean isInitiated;
    protected Set<MonteCarloTreeNode> children;

    public MonteCarloTreeNode() {
        pathToTypeMap = new MutualLinkedMap<>();
        children = new HashSet<>();
        playerTurn = false;
        isInitiated = false;
    }

    public MonteCarloTreeNode(MonteCarloTreeNode parent, MutualLinkedMap<Path, StepMode> pathToTypeMap) {
        this.pathToTypeMap = pathToTypeMap;
        children = new HashSet<>();
        this.playerTurn = !parent.playerTurn;
        this.parent = parent;
        this.visits = 1;
    }

    public MonteCarloTreeNode(MutualLinkedMap<Path, StepMode> pathToTypeMap, boolean playerTurn) {
        this.pathToTypeMap = pathToTypeMap;
        this.playerTurn = playerTurn;
        children = new HashSet<>();

    }

    public void addChild(MonteCarloTreeNode child) {
        children.add(child);
    }

    public MonteCarloTreeNode getChild(Collection<Path> paths) {
        for(MonteCarloTreeNode child : children) {
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

        return (double) stateReward / visits + Math.sqrt(Math.log(parent.visits) / visits);
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
        for(MonteCarloTreeNode child : children) {
            child.parent = null;
        }

        children.clear();
        pathToTypeMap.clear();
    }

    public MonteCarloTreeNode clone() {
        MonteCarloTreeNode node = new MonteCarloTreeNode();
        node.totalReward = totalReward;
        node.playerTurn = playerTurn;

        node.pathToTypeMap.putAll(pathToTypeMap);

        for(MonteCarloTreeNode child : children) {
            node.addChild(child.clone());
        }

        return node;
    }

    @SuppressWarnings("DefaultLocale")
    @Override
    public String toString() {
        return String.format("(%d/%d), hasAttack=%b", totalReward, visits, pathToTypeMap.values().contains(StepMode.ATTACK));
    }
}
