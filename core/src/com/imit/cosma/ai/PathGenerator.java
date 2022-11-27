package com.imit.cosma.ai;

import com.imit.cosma.config.Config;
import com.imit.cosma.pkg.random.Randomizer;
import com.imit.cosma.util.IntegerPoint;
import com.imit.cosma.model.board.Board;
import com.imit.cosma.util.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PathGenerator {
    private final List<Path> pathsAI, pathsPlayer;

    public PathGenerator(ArtificialBoard board){
        pathsAI = new ArrayList<>();
        pathsPlayer = new ArrayList<>();
        update(board);
    }

    public PathGenerator(List<Path> pathsAI, List<Path> pathsPlayer) {
        this.pathsAI = pathsAI;
        this.pathsPlayer = pathsPlayer;
    }

    public List<Path> getEnemyPaths(){
        return pathsAI;
    }

    public List<Path> getPlayerPaths() {
        return pathsPlayer;
    }

    public void update(Board board){
        pathsAI.clear();
        pathsPlayer.clear();
        for(int y = 0; y < Config.getInstance().BOARD_SIZE; y++){
            for(int x = 0; x < Config.getInstance().BOARD_SIZE; x++){
                if(board.isShip(x, y) && !board.getSide(x, y).isPlayer()){
                    for (IntegerPoint point : board.getAvailableCellsForMove(x, y)) {
                        pathsAI.add(new Path(x, y, point.x, point.y));
                    }
                    for (Map.Entry<IntegerPoint, Boolean> entry : board.getAvailableCellsForFire(x, y).entrySet()) {
                        if (entry.getValue()) {
                            IntegerPoint point = entry.getKey();
                            pathsAI.add(new Path(x, y, point.x, point.y));
                        }
                    }
                }
            }
        }

        for(int y = 0; y < Config.getInstance().BOARD_SIZE; y++){
            for(int x = 0; x < Config.getInstance().BOARD_SIZE; x++){
                if(board.isShip(x, y) && board.getSide(x, y).isPlayer()){
                    for (IntegerPoint point : board.getAvailableCellsForMove(x, y)) {
                        pathsPlayer.add(new Path(x, y, point.x, point.y));
                    }
                    for (Map.Entry<IntegerPoint, Boolean> entry : board.getAvailableCellsForFire(x, y).entrySet()) {
                        if (entry.getValue()) {
                            IntegerPoint point = entry.getKey();
                            pathsPlayer.add(new Path(x, y, point.x, point.y));
                        }
                    }
                }
            }
        }
    }

    public void update(ArtificialBoard board) {
        pathsAI.clear();
        pathsPlayer.clear();
        for(int y = 0; y < Config.getInstance().BOARD_SIZE; y++){
            for(int x = 0; x < Config.getInstance().BOARD_SIZE; x++){
                if(board.isShip(x, y) && !board.getSide(x, y).isPlayer()){
                    for (IntegerPoint point : board.getAvailableCellsForMove(x, y)) {
                        pathsAI.add(new Path(x, y, point.x, point.y));
                    }
                    for (IntegerPoint point : board.getAvailableCellsForFire(x, y)) {
                        pathsAI.add(new Path(x, y, point.x, point.y));
                    }
                }
            }
        }

        for(int y = 0; y < Config.getInstance().BOARD_SIZE; y++){
            for(int x = 0; x < Config.getInstance().BOARD_SIZE; x++){
                if(board.isShip(x, y) && board.getSide(x, y).isPlayer()){
                    for (IntegerPoint point : board.getAvailableCellsForMove(x, y)) {
                        pathsPlayer.add(new Path(x, y, point.x, point.y));
                    }
                    for (IntegerPoint point : board.getAvailableCellsForFire(x, y)) {
                        pathsPlayer.add(new Path(x, y, point.x, point.y));
                    }
                }
            }
        }
    }

    public static Path getRandomShipPath(ArtificialBoard board) {
        IntegerPoint randomShipLocation = Randomizer.getRandom(board.getTurn().isPlayer() ? board.getPlayerShipLocations() : board.getEnemyShipLocations());
        IntegerPoint randomTargetLocation = Randomizer.getRandom(new ArrayList<>(board.getAvailableCellsForMove(randomShipLocation)));

        return new Path(randomShipLocation, randomTargetLocation);
    }

    public PathGenerator clone() {
        List<Path> pathsAIClone = new ArrayList<>(pathsAI);
        List<Path> pathsPlayerClone = new ArrayList<>(pathsPlayer);

        return new PathGenerator(pathsAIClone, pathsPlayerClone);
    }
}