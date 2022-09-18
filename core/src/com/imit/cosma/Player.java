package com.imit.cosma;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.imit.cosma.util.IntegerPoint;

public class Player {

    private Input input;
    private IntegerPoint touchPoint;

    public Player(){
        input = Gdx.input;
        touchPoint = new IntegerPoint();
    }

    public boolean touchedScreen(){
        return input.isTouched();
    }

    public IntegerPoint getTouchPoint() {
        if(input.getX() != touchPoint.x && input.getY() != touchPoint.y) {
            touchPoint.set(input.getX(), input.getY());
        }
        return touchPoint;
    }
}
