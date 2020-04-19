package ru.geekbrains.environment;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.ScaledButton;
import ru.geekbrains.exceptions.GameException;
import ru.geekbrains.utils.Rect;

public class ButtonExit extends ScaledButton {

    public ButtonExit(TextureAtlas atlas) throws GameException {
        super(atlas.findRegion("ButtonExit"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.13f);
        setRight(worldBounds.getRight() - 0.05f);
        setBottom(worldBounds.getBottom() + 0.05f);
    }

    @Override
    public void action() {
        Gdx.app.exit();
    }
}
