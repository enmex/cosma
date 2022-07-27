package com.imit.cosma.model.board.weather;

public class SpaceDebris implements SpaceWeather {
    private double strength;

    private int minPiecesNumber;
    private int maxPiecesNumber;

    private SpaceDebris() {
        strength = Math.random() * 2;

        minPiecesNumber = /...
    }

    @Override
    public int getPiecesNumber() {
        return 0;
    }

    @Override
    public int getDamage() {
        return 0;
    }
}
