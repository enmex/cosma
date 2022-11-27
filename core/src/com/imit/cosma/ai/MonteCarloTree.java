package com.imit.cosma.ai;

import com.imit.cosma.config.Config;
import com.imit.cosma.model.rules.StepMode;
import com.imit.cosma.pkg.random.Randomizer;
import com.imit.cosma.util.MutualLinkedMap;
import com.imit.cosma.util.Path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MonteCarloTree extends DecisionTree {
    private final int SIMULATION_DEPTH = 5;
    private MonteCarloTreeNode root;
    private ArtificialBoard board;

    public MonteCarloTree(ArtificialBoard board){
        super(board);
        root = new MonteCarloTreeNode();
    }

    @Override
    public MutualLinkedMap<Path, StepMode> treeSearch(ArtificialBoard board) {
        this.board = board;
        if(root.children.isEmpty()) {
            expand(root);
        }

        MonteCarloTreeNode bestNode = selectBestNode(root);

        expand(bestNode);

        MonteCarloTreeNode randomChildNode = Randomizer.getRandom(bestNode.children);
        simulate(randomChildNode);

        backpropogate(randomChildNode, randomChildNode.reward);

        return randomChildNode.pathToTypeMap;
    }

    private void expand(MonteCarloTreeNode current) {
        PathGenerator generator = new PathGenerator(board);
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

                initNode(current, pathToTypeMap);
                childrenNumber++;
            } else {
                for(Path secondPath : pathsForSecondTurn) {

                    ArtificialBoard secondTurnBoard = firstTurnBoard.clone();

                    StepMode secondMode = secondTurnBoard.doTurn(secondPath);

                    MutualLinkedMap<Path, StepMode> pathToTypeMap = new MutualLinkedMap<>();
                    pathToTypeMap.put(secondPath, secondMode);
                    pathToTypeMap.put(firstPath, firstTurnMode);

                    initNode(current, pathToTypeMap);
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
        ArtificialBoard clonedBoard = board.clone();
        MonteCarloTreeNode node = parentNode.clone();

        int totalReward = 0;

        for (int i = 0; i < SIMULATION_DEPTH; i++) {
            MutualLinkedMap<Path, StepMode> pathToStepModeMap = new MutualLinkedMap<>();

            Path firstTurnPath = PathGenerator.getRandomShipPath(clonedBoard);
            pathToStepModeMap.put(firstTurnPath, clonedBoard.doTurn(firstTurnPath));

            Path secondTurnPath = PathGenerator.getRandomShipPath(clonedBoard);

            pathToStepModeMap.put(secondTurnPath, clonedBoard.doTurn(secondTurnPath));

            MonteCarloTreeNode childNode = new MonteCarloTreeNode(node, pathToStepModeMap);
            childNode.setAdvantage(clonedBoard, secondTurnPath);

            totalReward += childNode.reward;
            node.addChild(childNode);

            node = childNode;
        }

        parentNode.reward = totalReward;
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
            current.reward = reward;
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

    private void initNode(MonteCarloTreeNode parent, MutualLinkedMap<Path, StepMode> pathToTypeMap) {
        MonteCarloTreeNode node = parent.getChild(pathToTypeMap.keySet());

        if(node == null) {
            node = new MonteCarloTreeNode(parent, pathToTypeMap);
            parent.addChild(node);
        }
    }

}

class MonteCarloTreeNode {
    protected MutualLinkedMap<Path, StepMode> pathToTypeMap;
    protected boolean playerTurn;
    protected int reward;
    protected int visits;
    protected MonteCarloTreeNode parent;
    protected boolean isInitiated;
    protected List<MonteCarloTreeNode> children;

    public MonteCarloTreeNode() {
        pathToTypeMap = new MutualLinkedMap<>();
        children = new ArrayList<>();
        playerTurn = false;
        isInitiated = false;
    }

    public MonteCarloTreeNode(MonteCarloTreeNode parent, MutualLinkedMap<Path, StepMode> pathToTypeMap) {
        this.pathToTypeMap = pathToTypeMap;
        children = new ArrayList<>();
        this.playerTurn = !parent.playerTurn;
        this.parent = parent;
    }

    public MonteCarloTreeNode(MutualLinkedMap<Path, StepMode> pathToTypeMap, boolean playerTurn) {
        this.pathToTypeMap = pathToTypeMap;
        this.playerTurn = playerTurn;
        children = new ArrayList<>();

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

        return (double) reward / visits + Math.sqrt(Math.log(parent.visits) / visits);
    }

    public void setAdvantage(ArtificialBoard board, Path path) {
        reward = board.getMaxHealthPoints(path.getTarget())
                - board.getHealthPoints(path.getTarget())
                + board.getDamagePoints(path.getTarget());
    }

    public Path getByStepMode(StepMode stepMode) {
        return pathToTypeMap.getKey(stepMode);
    }

    public MonteCarloTreeNode clone() {
        MonteCarloTreeNode node = new MonteCarloTreeNode();
        node.reward = reward;
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
        return String.format("(%d/%d), hasAttack=%b", reward, visits, pathToTypeMap.values().contains(StepMode.ATTACK));
    }
}
