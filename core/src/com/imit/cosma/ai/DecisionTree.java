package com.imit.cosma.ai;

import com.imit.cosma.model.board.Board;
import com.imit.cosma.util.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DecisionTree implements Cloneable{
    private final MoveGenerator generator;

    private Path currentPath;

    private Board board;
    private final Stack<Board> states;

    private boolean playerTurn;

    public DecisionTree(Board board){
        generator = new MoveGenerator(board);
        currentPath = null;
        this.board = board;
        states = new Stack<>();
        playerTurn = false;
    }

    public int calculateBestPath(int depth, int turns, int alpha, int beta){
        int advantage;
        doTurn(currentPath);

        if(turns == 2 || board.sideCompletedTurn()){
            playerTurn = !playerTurn;
            depth--;
            turns = 0;
            board.updateSide();
        }

        if(depth == 0){
            undoTurn();
            return calculatePathAdvantage(currentPath);
        }

        List<Path> paths = new ArrayList<>(playerTurn ? generator.getPlayerPaths() : generator.getEnemyPaths());
        int minMaxEval = playerTurn ? Integer.MAX_VALUE : -Integer.MAX_VALUE;
        for(Path path : paths){
            currentPath = path;

            advantage = calculateBestPath(depth, turns + 1, alpha, beta);

            if(playerTurn){
                minMaxEval = Math.min(minMaxEval, advantage);
                beta = Math.min(advantage, beta);
            }
            else{
                minMaxEval = Math.max(minMaxEval, advantage);
                alpha = Math.max(advantage, alpha);
            }
            if(beta <= alpha){
                undoTurn();
                break;
            }
        }
        undoTurn();

        return minMaxEval;
    }

    public void update(Board board){
        this.board = board;
        generator.update(board);
    }

    public Board getBoard() {
        return board;
    }

    public void setRootPath(Path rootPath) {
        this.currentPath = rootPath;
    }

    private int calculatePathAdvantage(Path path){
        return board.getMaxHealthPoints(path.getTarget())
                - board.getHealthPoints(path.getTarget())
                + board.getDamagePoints(path.getTarget());
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

    @Override
    public DecisionTree clone() {
        DecisionTree decisionTree = new DecisionTree(board);
        decisionTree.currentPath = currentPath;
        decisionTree.playerTurn = playerTurn;

        return decisionTree;
    }
}