package com.imit.cosma.model.board.weather;

public class SpaceDebris implements SpaceWeather {
    private final int minPiecesNumber;
    private final int maxPiecesNumber;

    private final int minDamage;
    private final int maxDamage;

    public SpaceDebris() {
        minPiecesNumber = 5;
        maxPiecesNumber = 10; //TODO config

        minDamage = 100;
        maxDamage = 1000;
    }

    @Override
    public int getPiecesNumber() {
        return (int) (Math.random() * (maxPiecesNumber - minPiecesNumber) + minPiecesNumber);
    }

    @Override
    public int generateDamage() {
        return (int) (Math.random() * (maxDamage - minDamage) + minDamage);
    }
}
