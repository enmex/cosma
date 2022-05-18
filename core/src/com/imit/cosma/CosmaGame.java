package com.imit.cosma;

import com.badlogic.gdx.Game;
import com.imit.cosma.gui.screen.GameScreen;

public class CosmaGame extends Game {

	GameScreen gameScreen;

	@Override
	public void create() {
		gameScreen = new GameScreen();
		setScreen(gameScreen);
	}

	@Override
	public void dispose() {
		gameScreen.dispose();
	}
}