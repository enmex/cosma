package com.imit.cosma.ai;

import com.imit.cosma.model.board.Board;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Stack;

import java.util.ArrayList;
import java.util.List;

public class DecisionTree {
    private MoveGenerator generator;
    private Path currentPath;
    private Board board;
    private Stack<Board> states;
    private int bestAdvantage;

    public DecisionTree(Board board){
        generator = new MoveGenerator(board);
        currentPath = null;
        this.board = board.clone();
        states = new Stack<>();
        bestAdvantage = -1;
    }

    public int calculateBestMove(int depth, boolean playerTurn){
        states.push(board.clone());
        if(currentPath != null) {
            board.makeArtificialTurn(currentPath);
            update(board);
        }

        int advantage = 0;

        if(depth == 0){
            board = states.pop();
            return bestAdvantage;
        }

        if(playerTurn){
            List<Path> paths = new ArrayList<>(generator.getPlayerPaths());
            for (Path path : paths) {
                advantage = Math.min(board.getHealthPoints(path.getTarget()), board.getDamagePoints(path.getSource()));
                currentPath = path;
                System.out.println();
                if(bestAdvantage < advantage) {
                    System.out.print("player->[");
                    bestAdvantage = calculateBestMove(depth - 1, false);
                }
            }
        }
        else{
            List<Path> paths = new ArrayList<>(generator.getEnemyPaths());
            for (Path path : paths) {
                advantage = Math.min(board.getHealthPoints(path.getTarget()), board.getDamagePoints(path.getSource()));
                currentPath = path;
                if(bestAdvantage < advantage) {
                    System.out.print("enemy->[");
                    bestAdvantage = calculateBestMove(depth - 1, true);
                }
            }
        }
        System.out.print("]");
        board = states.pop();
        return advantage;
    }

    public Path getCurrentPath() {
        return currentPath;
    }

    public void update(Board board){
        generator.update(board);
    }
}
