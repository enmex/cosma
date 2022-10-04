package com.imit.cosma.pkg;

import com.imit.cosma.config.Config;
import com.imit.cosma.util.IntegerPoint;

import java.util.ArrayList;
import java.util.List;

public class CoordinateConverter {
    public static IntegerPoint toScreenPoint(IntegerPoint boardPoint) {
        return new IntegerPoint(boardPoint.x * Config.getInstance().BOARD_CELL_WIDTH,
                boardPoint.y * Config.getInstance().BOARD_CELL_HEIGHT + Config.getInstance().BOARD_Y
        );
    }

    public static IntegerPoint toOriginCenterScreenPoint(IntegerPoint boardPoint) {
        return new IntegerPoint(boardPoint.x * Config.getInstance().BOARD_CELL_WIDTH + Config.getInstance().BOARD_CELL_WIDTH / 2,
                boardPoint.y * Config.getInstance().BOARD_CELL_HEIGHT + Config.getInstance().BOARD_Y + Config.getInstance().BOARD_CELL_HEIGHT / 2
        );
    }

    public static IntegerPoint toBoardPoint(IntegerPoint screenPoint) {
        return new IntegerPoint(
                screenPoint.x / Config.getInstance().BOARD_CELL_WIDTH,
                (screenPoint.y - Config.getInstance().BOARD_Y) / Config.getInstance().BOARD_CELL_HEIGHT
        );
    }

    public static List<IntegerPoint> toScreenPoints(List<IntegerPoint> boardPoints) {
        List<IntegerPoint> screenPoints = new ArrayList<>();
        for (IntegerPoint boardPoint : boardPoints) {
            screenPoints.add(toScreenPoint(boardPoint));
        }

        return screenPoints;
    }

    public static List<IntegerPoint> toOriginCenterScreenPoints(List<IntegerPoint> boardPoints) {
        List<IntegerPoint> screenPoints = new ArrayList<>();
        for (IntegerPoint boardPoint : boardPoints) {
            screenPoints.add(toOriginCenterScreenPoint(boardPoint));
        }

        return screenPoints;
    }
}
