package com.imit.cosma.model.board.weather;

//Космическая погода - это совокупность объектов приземляющихся на случайные участки доски, нанося урон кораблям
public interface SpaceWeather {
    int getPiecesNumber();
    int generateDamage();
}
