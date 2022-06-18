package com.imit.cosma;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.imit.cosma.util.Point;

public class Player {

    private Input input;
    private Point touchPoint;

    public Player(){
        input = Gdx.input;
        touchPoint = new Point();
    }

    public boolean touchedScreen(){
        return input.isTouched();
    }

    public Point getTouchPoint() {
        if(input.getX() != touchPoint.x && input.getY() != touchPoint.y) {
            touchPoint.set(input.getX(), input.getY());
        }
        return touchPoint;
    }
}
