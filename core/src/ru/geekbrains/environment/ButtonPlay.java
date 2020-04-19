package ru.geekbrains.environment;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.ScaledButton;
import ru.geekbrains.exceptions.GameException;
import ru.geekbrains.utils.Rect;
import ru.geekbrains.screens.ScreenGameLevel1;

public class ButtonPlay extends ScaledButton {

    private final Game game;

    public ButtonPlay(TextureAtlas atlas, Game game) throws GameException {
        super(atlas.findRegion("ButtonPlay"));
        this.game = game;
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.12f);
        setLeft(worldBounds.getLeft() + 0.05f);
        setBottom(worldBounds.getBottom() + 0.05f);
    }


    @Override
    public void action() {
        game.setScreen(new ScreenGameLevel1(this.game));
    }
}
