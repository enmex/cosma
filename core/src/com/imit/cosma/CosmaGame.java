package com.imit.cosma;

import com.badlogic.gdx.Game;
import com.imit.cosma.gui.screen.GameScreen;
import com.imit.cosma.gui.screen.MainMenuScreen;

public class CosmaGame extends Game {

	MainMenuScreen mainMenuScreen;
	//GameScreen screen;

	@Override
	public void create() {
		mainMenuScreen = new MainMenuScreen(this);
		setScreen(mainMenuScreen);
	}

	@Override
	public void dispose() {
		mainMenuScreen.dispose();
	}
}