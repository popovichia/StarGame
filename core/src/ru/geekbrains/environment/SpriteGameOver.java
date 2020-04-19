package ru.geekbrains.environment;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.exceptions.GameException;
import ru.geekbrains.utils.Rect;

public class SpriteGameOver extends Sprite {

    public SpriteGameOver(TextureAtlas atlas) throws GameException {
        super(atlas.findRegion("message_game_over"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.07f);
        setTop(0.1f);
    }
}
