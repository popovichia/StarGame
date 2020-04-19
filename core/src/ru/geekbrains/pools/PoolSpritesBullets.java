package ru.geekbrains.pools;

import ru.geekbrains.base.SpriteBullet;

public class PoolSpritesBullets extends PoolSprites<SpriteBullet> {

    @Override
    protected SpriteBullet newObject() {
        return new SpriteBullet();
    }
}
