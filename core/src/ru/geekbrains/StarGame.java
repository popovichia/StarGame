package ru.geekbrains;

import com.badlogic.gdx.Game;

import ru.geekbrains.screens.ScreenMenu;

public class StarGame extends Game {

	@Override
	public void create() {
		setScreen(new ScreenMenu(this));
	}
}
