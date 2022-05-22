package com.imit.cosma.ai;

import com.imit.cosma.model.board.Board;
import com.imit.cosma.util.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DecisionTree {
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
        int advantage = 0;
        if(turns == 2){
            playerTurn = !playerTurn;
            depth--;
            turns = 0;
        }
        doTurn(currentPath);

        //добрались до листа - заканчиваем рекурсию
        if(depth == 0){
            undoTurn();
            return beta;
        }

        //вычисляем все ответвления от текущего узла
        List<Path> paths = new ArrayList<>(playerTurn ? generator.getPlayerPaths() : generator.getEnemyPaths());
        for(Path path : paths){
            currentPath = path;
            advantage = calculateBestPath(depth, turns + 1, alpha, beta);

            int currentScore = calculatePathAdvantage(currentPath);

            if(playerTurn){
                beta = Math.min(-currentScore, beta);
            }
            else{
                alpha = Math.max(currentScore, alpha);
            }

            if(alpha >= beta){
                undoTurn();
                return alpha;
            }
        }
        undoTurn();

        return advantage;
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
}
