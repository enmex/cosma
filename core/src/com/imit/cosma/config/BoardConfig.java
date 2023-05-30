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
    private static Map<Point<Integer>, ShipConfig> dev2Config;

    private static Map<Point<Integer>, ShipConfig> dev3Config;

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
        dev2Config = new HashMap<>();
        dev2Config.put(
                new Point<>(3, 7),
                new ShipConfig(
                        1,
                        Skeleton.DREADNOUGHT,
                        MoveType.OFFICER,
                        Weapon.ION_CANNON
                )
        );
        dev2Config.put(
                new Point<>(1, 6),
                new ShipConfig(
                        0,
                        Skeleton.CORVETTE,
                        MoveType.KING,
                        Weapon.ION_CANNON
                )
        );
        dev2Config.put(
                new Point<>(4, 6),
                new ShipConfig(
                        0,
                        Skeleton.CORVETTE,
                        MoveType.KING,
                        Weapon.ION_CANNON
                )
        );
        dev3Config = new HashMap<>();
        dev3Config.put(
                new Point<>(0, 7),
                new ShipConfig(
                        1,
                        Skeleton.DREADNOUGHT,
                        MoveType.KING,
                        Weapon.ION_CANNON,
                        Weapon.ION_CANNON,
                        Weapon.TORPEDO_LAUNCHER,
                        Weapon.TORPEDO_LAUNCHER,
                        Weapon.LASER
                )
        );
        dev3Config.put(
                new Point<>(1, 7),
                new ShipConfig(
                        1,
                        Skeleton.BATTLESHIP,
                        MoveType.HORSE,
                        Weapon.ION_CANNON,
                        Weapon.TORPEDO_LAUNCHER,
                        Weapon.LASER,
                        Weapon.MACHINE_GUN
                )
        );
        dev3Config.put(
                new Point<>(2, 7),
                new ShipConfig(
                        1,
                        Skeleton.DESTROYER,
                        MoveType.OFFICER,
                        Weapon.TORPEDO_LAUNCHER,
                        Weapon.LASER
                )
        );
        dev3Config.put(
                new Point<>(3, 7),
                new ShipConfig(
                        1,
                        Skeleton.CORVETTE,
                        MoveType.QUEEN,
                        Weapon.MACHINE_GUN
                )
        );
        dev3Config.put(
                new Point<>(4, 7),
                new ShipConfig(
                        1,
                        Skeleton.CORVETTE,
                        MoveType.QUEEN,
                        Weapon.MACHINE_GUN
                )
        );
        dev3Config.put(
                new Point<>(5, 7),
                new ShipConfig(
                        1,
                        Skeleton.DESTROYER,
                        MoveType.OFFICER,
                        Weapon.TORPEDO_LAUNCHER,
                        Weapon.LASER
                )
        );
        dev3Config.put(
                new Point<>(6, 7),
                new ShipConfig(
                        1,
                        Skeleton.BATTLESHIP,
                        MoveType.HORSE,
                        Weapon.ION_CANNON,
                        Weapon.TORPEDO_LAUNCHER,
                        Weapon.LASER,
                        Weapon.MACHINE_GUN
                )
        );
        dev3Config.put(
                new Point<>(7, 7),
                new ShipConfig(
                        1,
                        Skeleton.DREADNOUGHT,
                        MoveType.KING,
                        Weapon.ION_CANNON,
                        Weapon.ION_CANNON,
                        Weapon.TORPEDO_LAUNCHER,
                        Weapon.TORPEDO_LAUNCHER,
                        Weapon.LASER
                )
        );
        dev3Config.put(
                new Point<>(4, 0),
                new ShipConfig(
                        0,
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

    public Map<Point<Integer>, ShipConfig> getDev2Config() {
        return dev2Config;
    }

    public Map<Point<Integer>, ShipConfig> getDev3Config() {
        return dev3Config;
    }
}
