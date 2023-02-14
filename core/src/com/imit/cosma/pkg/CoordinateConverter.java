package com.imit.cosma.pkg;

import com.imit.cosma.config.Config;
import com.imit.cosma.util.Point;

import java.util.ArrayList;
import java.util.List;

public class CoordinateConverter {
    public static Point<Float> toScreenPoint(Point<Integer> boardPoint) {
        float x = boardPoint.x * Config.getInstance().BOARD_CELL_WIDTH;
        float y = boardPoint.y * Config.getInstance().BOARD_CELL_HEIGHT + Config.getInstance().BOARD_Y;
        return new Point<>(x, y);
    }


    public static Point<Float> toOriginCenterScreenPoint(Point<Integer> boardPoint) {
        return new Point<>(
                boardPoint.x * Config.getInstance().BOARD_CELL_WIDTH + Config.getInstance().BOARD_CELL_WIDTH / 2f,
                boardPoint.y * Config.getInstance().BOARD_CELL_HEIGHT + Config.getInstance().BOARD_Y + Config.getInstance().BOARD_CELL_HEIGHT / 2f
        );
    }

    public static Point<Integer> toBoardPoint(Point<Float> screenPoint) {
        return new Point<>(
                (int) (screenPoint.x / Config.getInstance().BOARD_CELL_WIDTH),
                (int) ((screenPoint.y - Config.getInstance().BOARD_Y) / Config.getInstance().BOARD_CELL_HEIGHT)
        );
    }

    public static List<Point<Float>> toScreenPoints(List<Point<Integer>> boardPoints) {
        List<Point<Float>> screenPoints = new ArrayList<>();
        for (Point<Integer> boardPoint : boardPoints) {
            screenPoints.add(toScreenPoint(boardPoint));
        }

        return screenPoints;
    }

    public static List<Point<Float>> toOriginCenterScreenPoints(List<Point<Integer>> boardPoints) {
        List<Point<Float>> screenPoints = new ArrayList<>();
        for (Point<Integer> boardPoint : boardPoints) {
            screenPoints.add(toOriginCenterScreenPoint(boardPoint));
        }

        return screenPoints;
    }
}
