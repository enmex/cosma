package com.imit.cosma.ai;

import com.imit.cosma.config.Config;
import com.imit.cosma.pkg.random.Randomizer;
import com.imit.cosma.model.board.Board;
import com.imit.cosma.util.Path;
import com.imit.cosma.util.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PathGenerator {
    private final List<Path<Integer>> pathsEnemy, pathsPlayer;

    public PathGenerator(){
        pathsEnemy = new ArrayList<>();
        pathsPlayer = new ArrayList<>();
    }

    public PathGenerator(ArtificialBoard board) {
        this();
        update(board);
    }

    public PathGenerator(List<Path<Integer>> pathsEnemy, List<Path<Integer>> pathsPlayer) {
        this.pathsEnemy = pathsEnemy;
        this.pathsPlayer = pathsPlayer;
    }

    public List<Path<Integer>> getEnemyPaths(){
        return pathsEnemy;
    }

    public List<Path<Integer>> getPlayerPaths() {
        return pathsPlayer;
    }

    public void update(Board board){
        pathsEnemy.clear();
        pathsPlayer.clear();
        for(int y = 0; y < Config.getInstance().BOARD_SIZE; y++){
            for(int x = 0; x < Config.getInstance().BOARD_SIZE; x++){
                if(board.isShip(x, y) && !board.getSide(x, y).isPlayer()){
                    for (Point<Integer> point : board.getAvailableCellsForMove(x, y)) {
                        pathsEnemy.add(new Path<>(x, y, point.x, point.y));
                    }
                    for (Map.Entry<Point<Integer>, Boolean> entry : board.getAvailableCellsForFire(x, y).entrySet()) {
                        if (entry.getValue()) {
                            Point<Integer> point = entry.getKey();
                            pathsEnemy.add(new Path<>(x, y, point.x, point.y));
                        }
                    }
                }
            }
        }

        for(int y = 0; y < Config.getInstance().BOARD_SIZE; y++){
            for(int x = 0; x < Config.getInstance().BOARD_SIZE; x++){
                if(board.isShip(x, y) && board.getSide(x, y).isPlayer()){
                    for (Point<Integer> point : board.getAvailableCellsForMove(x, y)) {
                        pathsPlayer.add(new Path<>(x, y, point.x, point.y));
                    }
                    for (Map.Entry<Point<Integer>, Boolean> entry : board.getAvailableCellsForFire(x, y).entrySet()) {
                        if (entry.getValue()) {
                            Point<Integer> point = entry.getKey();
                            pathsPlayer.add(new Path<>(x, y, point.x, point.y));
                        }
                    }
                }
            }
        }
    }

    public void update(ArtificialBoard board) {
        pathsEnemy.clear();
        pathsPlayer.clear();
        for (Point<Integer> enemyShip : board.getAvailableEnemyShipLocations()) {
            Set<Point<Integer>> availableCells = board.getAvailableCells(enemyShip);
            for (Point<Integer> point : availableCells) {
                pathsEnemy.add(new Path<>(enemyShip, point));
            }
        }

        for (Point<Integer> playerShip : board.getAvailablePlayerShipLocations()) {
            Set<Point<Integer>> availableCells = board.getAvailableCells(playerShip);
            for (Point<Integer> point : availableCells) {
                pathsPlayer.add(new Path<>(playerShip, point));
            }
        }
    }

    public static Path<Integer> getRandomShipPath(ArtificialBoard board) {
        Point<Integer> randomShipLocation = Randomizer.getRandom(board.getTurn().isPlayer()
                ? board.getAvailablePlayerShipLocations()
                : board.getAvailableEnemyShipLocations());
        Set<Point<Integer>> availableCells = board.getAvailableForAttack(randomShipLocation);
        if (availableCells.isEmpty()) {
            availableCells = board.getAvailableForMove(randomShipLocation);
        }

        Point<Integer> randomTargetLocation = Randomizer.getRandom(new ArrayList<>(availableCells));

        return new Path<>(randomShipLocation, randomTargetLocation);
    }

    public PathGenerator clone() {
        List<Path<Integer>> pathsAIClone = new ArrayList<>(pathsEnemy);
        List<Path<Integer>> pathsPlayerClone = new ArrayList<>(pathsPlayer);

        return new PathGenerator(pathsAIClone, pathsPlayerClone);
    }
}
