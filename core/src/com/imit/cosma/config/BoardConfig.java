package com.imit.cosma.config;

import com.imit.cosma.model.rules.move.MoveType;
import com.imit.cosma.model.rules.side.Side;
import com.imit.cosma.model.spaceship.Skeleton;
import com.imit.cosma.model.spaceship.Weapon;
import com.imit.cosma.util.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardConfig {
    private static BoardConfig boardConfig;
    private final Map<Point<Integer>, ShipConfig> productionConfig;
    private static Map<Point<Integer>, ShipConfig> devConfig;

    private BoardConfig() {
        productionConfig = new HashMap<>();
        for (int y = 0; y < 8; y+=7) {
            productionConfig.put(
                    new Point<>(0, y),
                    new ShipConfig(
                            y / 7,
                            Skeleton.DREADNOUGHT,
                            MoveType.KING,
                            Weapon.ION_CANNON,
                            Weapon.ION_CANNON,
                            Weapon.TORPEDO_LAUNCHER,
                            Weapon.TORPEDO_LAUNCHER,
                            Weapon.LASER
                    )
            );
            productionConfig.put(
                    new Point<>(1, y),
                    new ShipConfig(
                            y / 7,
                            Skeleton.BATTLESHIP,
                            MoveType.HORSE,
                            Weapon.ION_CANNON,
                            Weapon.TORPEDO_LAUNCHER,
                            Weapon.LASER,
                            Weapon.MACHINE_GUN
                    )
            );
            productionConfig.put(
                    new Point<>(2, y),
                    new ShipConfig(
                            y / 7,
                            Skeleton.DESTROYER,
                            MoveType.OFFICER,
                            Weapon.TORPEDO_LAUNCHER,
                            Weapon.LASER
                    )
            );
            productionConfig.put(
                    new Point<>(3, y),
                    new ShipConfig(
                            y / 7,
                            Skeleton.CORVETTE,
                            MoveType.QUEEN,
                            Weapon.MACHINE_GUN
                    )
            );
            productionConfig.put(
                    new Point<>(4, y),
                    new ShipConfig(
                            y / 7,
                            Skeleton.CORVETTE,
                            MoveType.QUEEN,
                            Weapon.MACHINE_GUN
                    )
            );
            productionConfig.put(
                    new Point<>(5, y),
                    new ShipConfig(
                            y / 7,
                            Skeleton.DESTROYER,
                            MoveType.OFFICER,
                            Weapon.TORPEDO_LAUNCHER,
                            Weapon.LASER
                    )
            );
            productionConfig.put(
                    new Point<>(6, y),
                    new ShipConfig(
                            y / 7,
                            Skeleton.BATTLESHIP,
                            MoveType.HORSE,
                            Weapon.ION_CANNON,
                            Weapon.TORPEDO_LAUNCHER,
                            Weapon.LASER,
                            Weapon.MACHINE_GUN
                    )
            );
            productionConfig.put(
                    new Point<>(7, y),
                    new ShipConfig(
                            y / 7,
                            Skeleton.DREADNOUGHT,
                            MoveType.KING,
                            Weapon.ION_CANNON,
                            Weapon.ION_CANNON,
                            Weapon.TORPEDO_LAUNCHER,
                            Weapon.TORPEDO_LAUNCHER,
                            Weapon.LASER
                    )
            );
        }
        devConfig = new HashMap<>();
        devConfig.put(
                new Point<>(3, 4),
                new ShipConfig(
                        1,
                        Skeleton.CORVETTE,
                        MoveType.KING,
                        Weapon.ION_CANNON
                )
        );
        devConfig.put(
                new Point<>(3, 3),
                new ShipConfig(
                        0,
                        Skeleton.DREADNOUGHT,
                        MoveType.KING,
                        Weapon.ION_CANNON
                )
        );
    }

    public static BoardConfig getInstance() {
        if (boardConfig == null) {
            boardConfig = new BoardConfig();
        }
        return boardConfig;
    }

    public Map<Point<Integer>, ShipConfig> getProductionConfig() {
        return productionConfig;
    }

    public Map<Point<Integer>, ShipConfig> getDevConfig() {
        return devConfig;
    }
}
