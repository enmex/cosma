package com.imit.cosma.config;

import static com.badlogic.gdx.utils.XmlReader.Element;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;

public class Config {
    public int DEFAULT_SHIPS_NUMBER = 16;
    private static Config instance;

    public int ANIMATION_SPEED;
    public float ANIMATION_DURATION = 30;
    public int INFINITY_ANIMATION_DURATION = 9999;
    public int FRAMES_AMOUNT_SHIPS = 4;
    public float ROTATION_VELOCITY = 2f;
    public int SHIP_SPRITE_SIZE = 128;
    public int SHOT_SPRITE_SIZE = 64;
    public double PANEL_TO_SCREEN_RATIO = 0.3;
    public double PANEL_OFFSET = 0.03;
    public double HEALTH_BAR_WIDTH_TO_PANEL_RATIO = 0.6;
    public double HEALTH_BAR_HEIGHT_TO_PANEL_RATIO = 0.1;
    public int BOARD_SIZE = 8;
    public int SPACESHIP_ROWS = 1;
    public int MOVEMENT_ANIMATION_PHASES = 3;
    public String BACKGROUND_PATH = "background.png";
    public String GRID_PATH = "grid.png";
    public String SPACESHIP_PATH = "spaceships.png";
    public String INFORMATION_PANEL_PATH =  "spaceship_panel.png";
    public String FONT_PATH = "font\\font.fnt";
    public String SELECTED_CELL_PATH = "selected.png";

    private Config(){
        Element element = getElement();
        //ANIMATION_SPEED = element.getInt("animation_speed");
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
