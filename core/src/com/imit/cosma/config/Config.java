package com.imit.cosma.config;

import static com.badlogic.gdx.utils.XmlReader.Element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.XmlReader;
import com.imit.cosma.model.board.content.Space;
import com.imit.cosma.model.rules.move.HorseMovingStyle;
import com.imit.cosma.model.rules.move.IdleMovingStyle;
import com.imit.cosma.model.rules.move.KingMovingStyle;
import com.imit.cosma.model.rules.move.MovingStyle;
import com.imit.cosma.model.rules.move.OfficerMovingStyle;
import com.imit.cosma.model.rules.move.QueenMovingStyle;
import com.imit.cosma.model.rules.move.WeakRookMovingStyle;

public class Config {
    public int DEFAULT_SHIPS_NUMBER = 1;
    private static Config instance;

    public float FRAME_DURATION = 1 / 8f;

    public int BLACK_HOLE_MAX_DAMAGE = 10000;
    public int ANIMATION_SPEED;
    public float ANIMATION_DURATION = 100;
    public float ROTATION_VELOCITY = 2f;
    public double PANEL_TO_SCREEN_RATIO = 0.3;
    public double PANEL_OFFSET = 0.03;
    public int BOARD_SIZE = 8;
    public int MOVEMENT_ANIMATION_PHASES = 3;
    public String BACKGROUND_PATH = "background.png";
    public String GRID_PATH = "grid.png";
    public String SPACESHIP_PATH = "spaceships.png";
    public String INFORMATION_PANEL_PATH =  "spaceship_panel.png";
    public String FONT_PATH = "font\\font.fnt";
    public String SELECTED_CELL_PATH = "selected.png";
    public String GAME_OBJECTS_PATH = "objects.png";
    public String WIDGETS_PATH = "widgets.png";

    public MovingStyle KING_MOVING_STYLE;
    public MovingStyle QUEEN_MOVING_STYLE;
    public MovingStyle OFFICER_MOVING_STYLE;
    public MovingStyle HORSE_MOVING_STYLE;
    public MovingStyle WEAK_ROOK_MOVING_STYLE;
    public MovingStyle IDLE_MOVING_STYLE;

    public TextureRegion SPACESHIP_ATLAS;
    public TextureRegion GAME_OBJECTS_ATLAS;

    public Space SPACE;
    public float SPACE_DEBRIS_SPAWN_CHANCE = 0.05f;
    public float BLACK_HOLE_SPAWN_CHANCE = 0.1f;
    public float SUPPLY_KIT_SPAWN_CHANCE = 0.4f;

    public int WORLD_WIDTH = Gdx.graphics.getWidth();
    public int WORLD_HEIGHT = Gdx.graphics.getHeight();

    public int BOARD_WIDTH = WORLD_WIDTH;
    public int BOARD_HEIGHT = (int) (WORLD_HEIGHT * 0.45);

    public int BOARD_CELL_WIDTH = BOARD_WIDTH / BOARD_SIZE;
    public int BOARD_CELL_HEIGHT = BOARD_HEIGHT / BOARD_SIZE;
    public int BOARD_Y = (int) (WORLD_HEIGHT * 0.35);

    private final String SPACESHIP_ANIMATIONS_DIRECTORY = "animation\\spaceship\\";
    private final String GAME_OBJECT_ANIMATIONS_DIRECTORY = "animation\\game_object\\";
    private final String WEAPON_ANIMATIONS_DIRECTORY = "animation\\weapon\\";

    public String CORVETTE_IDLE_ATLAS_PATH = SPACESHIP_ANIMATIONS_DIRECTORY + "idle_corvette.atlas";
    public String DESTROYER_IDLE_ATLAS_PATH = SPACESHIP_ANIMATIONS_DIRECTORY + "idle_destroyer.atlas";
    public String DREADNOUGHT_IDLE_ATLAS_PATH = SPACESHIP_ANIMATIONS_DIRECTORY + "idle_dreadnought.atlas";
    public String BATTLESHIP_IDLE_ATLAS_PATH = SPACESHIP_ANIMATIONS_DIRECTORY + "idle_battleship.atlas";

    public String CORVETTE_DESTRUCTION_ATLAS_PATH = SPACESHIP_ANIMATIONS_DIRECTORY + "destruction_corvette.atlas";
    public String DESTROYER_DESTRUCTION_ATLAS_PATH = SPACESHIP_ANIMATIONS_DIRECTORY + "destruction_destroyer.atlas";
    public String DREADNOUGHT_DESTRUCTION_ATLAS_PATH = SPACESHIP_ANIMATIONS_DIRECTORY + "destruction_dreadnought.atlas";
    public String BATTLESHIP_DESTRUCTION_ATLAS_PATH = SPACESHIP_ANIMATIONS_DIRECTORY + "destruction_battleship.atlas";

    public String CORVETTE_MOVEMENT_ATLAS_PATH = SPACESHIP_ANIMATIONS_DIRECTORY + "movement_corvette.atlas";
    public String DESTROYER_MOVEMENT_ATLAS_PATH = SPACESHIP_ANIMATIONS_DIRECTORY + "movement_destroyer.atlas";
    public String DREADNOUGHT_MOVEMENT_ATLAS_PATH = SPACESHIP_ANIMATIONS_DIRECTORY + "movement_dreadnought.atlas";
    public String BATTLESHIP_MOVEMENT_ATLAS_PATH = SPACESHIP_ANIMATIONS_DIRECTORY + "movement_battleship.atlas";

    public String BLACK_HOLE_IDLE_ATLAS_PATH = GAME_OBJECT_ANIMATIONS_DIRECTORY + "idle_black_hole.atlas";
    public String HEALTH_KIT_IDLE_ATLAS_PATH = GAME_OBJECT_ANIMATIONS_DIRECTORY + "idle_health_kit.atlas";
    public String DAMAGE_KIT_IDLE_ATLAS_PATH = GAME_OBJECT_ANIMATIONS_DIRECTORY + "idle_damage_kit.atlas";

    public String BLACK_HOLE_SPAWN_ATLAS_PATH = GAME_OBJECT_ANIMATIONS_DIRECTORY + "spawn_black_hole.atlas";
    public String HEALTH_KIT_SPAWN_ATLAS_PATH = GAME_OBJECT_ANIMATIONS_DIRECTORY + "spawn_health_kit.atlas";
    public String DAMAGE_KIT_SPAWN_ATLAS_PATH = GAME_OBJECT_ANIMATIONS_DIRECTORY + "spawn_damage_kit.atlas";

    public String MACHINE_GUN_SHOT_ATLAS_PATH = WEAPON_ANIMATIONS_DIRECTORY + "shot_machine_gun.atlas";
    public String LASER_SHOT_ATLAS_PATH = WEAPON_ANIMATIONS_DIRECTORY + "shot_laser.atlas";
    public String ION_CANNON_SHOT_ATLAS_PATH = WEAPON_ANIMATIONS_DIRECTORY + "shot_ion_cannon.atlas";
    public String TORPEDO_LAUNCHER_SHOT_ATLAS_PATH = WEAPON_ANIMATIONS_DIRECTORY + "shot_machine_gun.atlas";

    public String MACHINE_GUN_EXPLOSION_ATLAS_PATH = WEAPON_ANIMATIONS_DIRECTORY + "explosion_machine_gun.atlas";
    public String LASER_EXPLOSION_ATLAS_PATH = WEAPON_ANIMATIONS_DIRECTORY + "explosion_laser.atlas";
    public String ION_CANNON_EXPLOSION_ATLAS_PATH = WEAPON_ANIMATIONS_DIRECTORY + "explosion_ion_cannon.atlas";
    public String TORPEDO_LAUNCHER_EXPLOSION_ATLAS_PATH = WEAPON_ANIMATIONS_DIRECTORY + "explosion_torpedo_launcher.atlas";

    public String IDLE_ANIMATION_REGION_NAME = "idle";
    public String MOVEMENT_ANIMATION_REGION_NAME = "move";

    private Config(){
        KING_MOVING_STYLE = new KingMovingStyle();
        QUEEN_MOVING_STYLE = new QueenMovingStyle();
        OFFICER_MOVING_STYLE = new OfficerMovingStyle();
        HORSE_MOVING_STYLE = new HorseMovingStyle();
        WEAK_ROOK_MOVING_STYLE = new WeakRookMovingStyle();
        IDLE_MOVING_STYLE = new IdleMovingStyle();

        SPACESHIP_ATLAS = new TextureRegion(new Texture(SPACESHIP_PATH));
        GAME_OBJECTS_ATLAS = new TextureRegion(new Texture(GAME_OBJECTS_PATH));

        SPACE = new Space();
    }

    private static Element getElement(){
        XmlReader xmlReader = new XmlReader();
        return xmlReader.parse(Gdx.files.internal("gameconfig.xml"));
    }

    public static Config getInstance() {
        if(instance == null){
            instance = new Config();
        }
        return instance;
    }

}
