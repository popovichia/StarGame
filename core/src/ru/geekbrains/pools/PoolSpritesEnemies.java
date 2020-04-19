package ru.geekbrains.pools;

import ru.geekbrains.utils.Rect;
import ru.geekbrains.enemies.ShipEnemy;

public class PoolSpritesEnemies extends PoolSprites<ShipEnemy> {

    private PoolSpritesBullets poolBullets;
    private PoolSpritesExplosions poolExplosions;
    private Rect worldBounds;

    public PoolSpritesEnemies(PoolSpritesBullets poolBullets, PoolSpritesExplosions poolExplosions, Rect worldBounds) {
        this.poolBullets = poolBullets;
        this.poolExplosions = poolExplosions;
        this.worldBounds = worldBounds;
    }

    @Override
    protected ShipEnemy newObject() {
        return new ShipEnemy(poolBullets, poolExplosions, worldBounds);
    }
}
