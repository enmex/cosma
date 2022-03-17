package com.imit.cosma;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Player {

    private Input input;

    public Player(){
        input = Gdx.input;
    }

    public boolean touchedScreen(){
        return input.isTouched();
    }

    public boolean touchedBoard() {
        return false;
    }

    public int getX(){
        return input.getX();
    }

    public int getY(){
        return input.getY();
    }

}
