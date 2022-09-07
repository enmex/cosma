package com.imit.cosma;

import com.badlogic.gdx.Game;
import com.imit.cosma.gui.screen.GameScreen;
import com.imit.cosma.gui.screen.MainMenuScreen;

public class CosmaGame extends Game {

	MainMenuScreen mainMenuScreen;

	@Override
	public void create() {
		mainMenuScreen = new MainMenuScreen();
		setScreen(mainMenuScreen);
	}

	@Override
	public void render() {
		if (mainMenuScreen.)
	}

	@Override
	public void dispose() {
		mainMenuScreen.dispose();
	}
}