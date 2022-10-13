package com.imit.cosma.config;

import com.badlogic.gdx.Gdx;
import com.imit.cosma.model.board.content.Space;
import com.imit.cosma.model.rules.move.HorseMovingStyle;
import com.imit.cosma.model.rules.move.IdleMovingStyle;
import com.imit.cosma.model.rules.move.KingMovingStyle;
import com.imit.cosma.model.rules.move.MovingStyle;
import com.imit.cosma.model.rules.move.OfficerMovingStyle;
import com.imit.cosma.model.rules.move.QueenMovingStyle;
import com.imit.cosma.model.rules.move.WeakRookMovingStyle;
import com.imit.cosma.util.IntegerPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    public final int DEFAULT_SHIPS_NUMBER = 24;
    private static Config instance;

    public final float FRAME_DURATION = 1 / 8f;

    public final float ANIMATION_DURATION = 60;
    public final float ROTATION_VELOCITY = 2f;
    public final double PANEL_TO_SCREEN_RATIO = 0.3;
    public final double PANEL_OFFSET = 0.03;
    public final int BOARD_SIZE = 8;
    public final int DEFAULT_SPRITE_SIZE = 128;
    public final float MUSIC_FADE_STEP = 0.25f;

    public final int MIN_HEALTH_KIT_HEALTH_POINTS = 150;
    public final int MAX_HEALTH_KIT_HEALTH_POINTS = 500;

    public final String BACKGROUND_PATH = "background.png";
    public final String GRID_PATH = "grid.png";
    public final String INFORMATION_PANEL_PATH =  "spaceship_panel.png";
    public final String FONT_PATH = "font\\font.fnt";
    public final String SELECTED_CELL_PATH = "selected.png";

    public final MovingStyle KING_MOVING_STYLE;
    public final MovingStyle QUEEN_MOVING_STYLE;
    public final MovingStyle OFFICER_MOVING_STYLE;
    public final MovingStyle HORSE_MOVING_STYLE;
    public final MovingStyle WEAK_ROOK_MOVING_STYLE;
    public final MovingStyle IDLE_MOVING_STYLE;

    public final Space SPACE;
    public final List<IntegerPoint> EMPTY_LIST = new ArrayList<>();
    public final Map<IntegerPoint, String> EMPTY_MAP = new HashMap<>();
    public final float SPACE_DEBRIS_SPAWN_CHANCE = 0f;
    public final float BLACK_HOLE_SPAWN_CHANCE = 1f;
    public final float LOOT_SPAWN_CHANCE = 0f;

    public final int WORLD_WIDTH = Gdx.graphics.getWidth();
    public final int WORLD_HEIGHT = Gdx.graphics.getHeight();

    public final int BOARD_WIDTH = WORLD_WIDTH;
    public final int BOARD_HEIGHT = (int) (WORLD_HEIGHT * 0.45);

    public final int BOARD_CELL_WIDTH = BOARD_WIDTH / BOARD_SIZE;
    public final int BOARD_CELL_HEIGHT = BOARD_HEIGHT / BOARD_SIZE;
    public final int BOARD_Y = (int) (WORLD_HEIGHT * 0.35);

    public final int INFO_PANEL_WIDTH = Gdx.graphics.getWidth();
    public final int INFO_PANEL_HEIGHT = (int) (WORLD_HEIGHT * PANEL_TO_SCREEN_RATIO);
    public final int INFO_PANEL_BOTTOM = (int) (WORLD_HEIGHT * PANEL_OFFSET);

    private final String SPACESHIP_ANIMATIONS_DIRECTORY = "animation\\spaceship\\";
    private final String GAME_OBJECT_ANIMATIONS_DIRECTORY = "animation\\game_object\\";
    private final String WEAPON_ANIMATIONS_DIRECTORY = "animation\\weapon\\";

    public final String CORVETTE_IDLE_ATLAS_PATH = SPACESHIP_ANIMATIONS_DIRECTORY + "idle_corvette.atlas";
    public final String DESTROYER_IDLE_ATLAS_PATH = SPACESHIP_ANIMATIONS_DIRECTORY + "idle_destroyer.atlas";
    public final String DREADNOUGHT_IDLE_ATLAS_PATH = SPACESHIP_ANIMATIONS_DIRECTORY + "idle_dreadnought.atlas";
    public final String BATTLESHIP_IDLE_ATLAS_PATH = SPACESHIP_ANIMATIONS_DIRECTORY + "idle_battleship.atlas";

    public final String CORVETTE_DESTRUCTION_ATLAS_PATH = SPACESHIP_ANIMATIONS_DIRECTORY + "destruction_corvette.atlas";
    public final String DESTROYER_DESTRUCTION_ATLAS_PATH = SPACESHIP_ANIMATIONS_DIRECTORY + "destruction_destroyer.atlas";
    public final String DREADNOUGHT_DESTRUCTION_ATLAS_PATH = SPACESHIP_ANIMATIONS_DIRECTORY + "destruction_dreadnought.atlas";
    public final String BATTLESHIP_DESTRUCTION_ATLAS_PATH = SPACESHIP_ANIMATIONS_DIRECTORY + "destruction_battleship.atlas";

    public final String CORVETTE_MOVEMENT_ATLAS_PATH = SPACESHIP_ANIMATIONS_DIRECTORY + "movement_corvette.atlas";
    public final String DESTROYER_MOVEMENT_ATLAS_PATH = SPACESHIP_ANIMATIONS_DIRECTORY + "movement_destroyer.atlas";
    public final String DREADNOUGHT_MOVEMENT_ATLAS_PATH = SPACESHIP_ANIMATIONS_DIRECTORY + "movement_dreadnought.atlas";
    public final String BATTLESHIP_MOVEMENT_ATLAS_PATH = SPACESHIP_ANIMATIONS_DIRECTORY + "movement_battleship.atlas";

    public final String BLACK_HOLE_IDLE_ATLAS_PATH = GAME_OBJECT_ANIMATIONS_DIRECTORY + "idle_black_hole.atlas";
    public final String HEALTH_KIT_IDLE_ATLAS_PATH = BLACK_HOLE_IDLE_ATLAS_PATH;//GAME_OBJECT_ANIMATIONS_DIRECTORY + "idle_health_kit.atlas";
    public final String DAMAGE_KIT_IDLE_ATLAS_PATH = BLACK_HOLE_IDLE_ATLAS_PATH;//GAME_OBJECT_ANIMATIONS_DIRECTORY + "idle_damage_kit.atlas";
    public final String SPACE_DEBRIS_1_MOVEMENT_ATLAS_PATH = GAME_OBJECT_ANIMATIONS_DIRECTORY + "space_debris_1.atlas";
    public final String SPACE_DEBRIS_2_MOVEMENT_ATLAS_PATH = GAME_OBJECT_ANIMATIONS_DIRECTORY + "space_debris_2.atlas";
    public final String SPACE_DEBRIS_3_MOVEMENT_ATLAS_PATH = GAME_OBJECT_ANIMATIONS_DIRECTORY + "space_debris_3.atlas";
    public final String SPACE_DEBRIS_4_MOVEMENT_ATLAS_PATH = GAME_OBJECT_ANIMATIONS_DIRECTORY + "space_debris_4.atlas";

    public final String BLACK_HOLE_SPAWN_ATLAS_PATH = GAME_OBJECT_ANIMATIONS_DIRECTORY + "spawn_black_hole.atlas";
    public final String HEALTH_KIT_SPAWN_ATLAS_PATH = BLACK_HOLE_SPAWN_ATLAS_PATH;//GAME_OBJECT_ANIMATIONS_DIRECTORY + "spawn_health_kit.atlas";
    public final String DAMAGE_KIT_SPAWN_ATLAS_PATH = BLACK_HOLE_SPAWN_ATLAS_PATH;//GAME_OBJECT_ANIMATIONS_DIRECTORY + "spawn_damage_kit.atlas";

    public final String MACHINE_GUN_SHOT_ATLAS_PATH = WEAPON_ANIMATIONS_DIRECTORY + "shot_machine_gun.atlas";
    public final String LASER_SHOT_ATLAS_PATH = WEAPON_ANIMATIONS_DIRECTORY + "shot_laser.atlas";
    public final String ION_CANNON_SHOT_ATLAS_PATH = WEAPON_ANIMATIONS_DIRECTORY + "shot_ion_cannon.atlas";
    public final String TORPEDO_LAUNCHER_SHOT_ATLAS_PATH = WEAPON_ANIMATIONS_DIRECTORY + "shot_machine_gun.atlas";

    public final String MACHINE_GUN_EXPLOSION_ATLAS_PATH = WEAPON_ANIMATIONS_DIRECTORY + "explosion_machine_gun.atlas";
    public final String LASER_EXPLOSION_ATLAS_PATH = WEAPON_ANIMATIONS_DIRECTORY + "explosion_laser.atlas";
    public final String ION_CANNON_EXPLOSION_ATLAS_PATH = WEAPON_ANIMATIONS_DIRECTORY + "explosion_ion_cannon.atlas";
    public final String TORPEDO_LAUNCHER_EXPLOSION_ATLAS_PATH = WEAPON_ANIMATIONS_DIRECTORY + "explosion_torpedo_launcher.atlas";

    public final String IDLE_ANIMATION_REGION_NAME = "idle";
    public final String MOVEMENT_ANIMATION_REGION_NAME = "move";

    public boolean SOUNDS_ON = true;
    public boolean MUSIC_ON = true;
    public final String SOUNDS_DIRECTORY = "sound\\";
    public final String ROTATION_SOUND = SOUNDS_DIRECTORY + "rotation.ogg";

    public final String MACHINE_GUN_SHOT_SOUND = SOUNDS_DIRECTORY + "machine_gun_shot.ogg";
    public final String LASER_SHOT_SOUND = SOUNDS_DIRECTORY + "laser_shot.ogg";
    public final String ION_CANNON_SHOT_SOUND = SOUNDS_DIRECTORY + "ion_cannon_shot.ogg";
    public final String TORPEDO_LAUNCHER_SHOT_SOUND = SOUNDS_DIRECTORY + "torpedo_launcher_shot.ogg";

    public final String MACHINE_GUN_EXPLOSION_SOUND = SOUNDS_DIRECTORY + "machine_gun_explosion.ogg";
    public final String LASER_EXPLOSION_SOUND = SOUNDS_DIRECTORY + "laser_explosion.ogg";
    public final String ION_CANNON_EXPLOSION_SOUND = SOUNDS_DIRECTORY + "ion_cannon_explosion.ogg";
    public final String TORPEDO_LAUNCHER_EXPLOSION_SOUND = SOUNDS_DIRECTORY + "torpedo_launcher_explosion.ogg";

    private Config(){
        KING_MOVING_STYLE = new KingMovingStyle();
        QUEEN_MOVING_STYLE = new QueenMovingStyle();
        OFFICER_MOVING_STYLE = new OfficerMovingStyle();
        HORSE_MOVING_STYLE = new HorseMovingStyle();
        WEAK_ROOK_MOVING_STYLE = new WeakRookMovingStyle();
        IDLE_MOVING_STYLE = new IdleMovingStyle();

        SPACE = new Space();
    }

    public static Config getInstance() {
        if(instance == null){
            instance = new Config();
        }
        return instance;
    }
}
