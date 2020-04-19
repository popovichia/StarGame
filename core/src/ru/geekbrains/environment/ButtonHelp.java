package ru.geekbrains.environment;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.ScaledButton;
import ru.geekbrains.exceptions.GameException;
import ru.geekbrains.screens.ScreenGameLevel1;
import ru.geekbrains.screens.ScreenHelp;
import ru.geekbrains.utils.Rect;

public class ButtonHelp extends ScaledButton {
    private final Game game;

    public ButtonHelp(TextureAtlas textureAtlas, Game game) throws GameException {
        super(textureAtlas.findRegion("ButtonHelp"));
        this.game = game;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.12f);
        setBottom(worldBounds.getBottom() + 0.05f);
    }


    @Override
    public void action() {
        game.setScreen(new ScreenHelp(this.game));
    }
}
