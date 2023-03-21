package com.imit.cosma.ai;

import com.imit.cosma.pkg.random.Randomizer;

public class TreeProcessTask implements Runnable {
    private final MCTree tree;
    private final MCTreeNode treeNode;
    private final ArtificialBoard board;

    public TreeProcessTask(MCTree tree, MCTreeNode treeNode, ArtificialBoard board) {
        this.tree = tree;
        this.treeNode = treeNode;
        this.board = board;
    }

    @Override
    public void run() {
        board.doTurn(treeNode.getPath());
        tree.expand(board, treeNode, !board.getTurn().isPlayer());
        MCTreeNode randomChildNode = Randomizer.getRandom(treeNode.getChildren());
        board.doTurn(randomChildNode.getPath());
        tree.simulate(board, randomChildNode);
        tree.backpropogate(randomChildNode, randomChildNode.getReward());
        treeNode.setLocked(false);
    }
}
