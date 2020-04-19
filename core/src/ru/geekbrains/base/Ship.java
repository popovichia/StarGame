package ru.geekbrains.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.exceptions.GameException;
import ru.geekbrains.utils.Rect;
import ru.geekbrains.pools.PoolSpritesBullets;
import ru.geekbrains.pools.PoolSpritesExplosions;

public abstract class Ship extends Sprite {

    private static final float DAMAGE_ANIMATE_INTERVAL = 0.1f;
    private static final float DELTA_COEFF = 1.2f;
    private float savedDelta = 0f;

    protected Rect worldBounds;
    protected PoolSpritesBullets poolBullets;
    protected PoolSpritesExplosions poolExplosions;
    protected TextureRegion bulletTextureRegion;
    protected Vector2 bulletV;
    protected Vector2 bulletPos;
    protected float bulletHeight;
    protected int damage;
    protected Sound shootSound;
    protected int hp;

    protected Vector2 v0;
    protected Vector2 v;

    protected float reloadInterval;
    protected float reloadTimer;
    protected float damageAnimateTimer = DAMAGE_ANIMATE_INTERVAL;

    public Ship() {
    }

    public Ship(TextureRegion region, int rows, int cols, int frames) throws GameException {
        super(region, rows, cols, frames);
    }

    @Override
    public void update(float delta) {
        if (savedDelta == 0f) {
            savedDelta = delta;
        }
        if (delta > savedDelta*DELTA_COEFF) {
            delta = savedDelta;
        }
        pos.mulAdd(v, delta);
        damageAnimateTimer += delta;
        if (damageAnimateTimer >= DAMAGE_ANIMATE_INTERVAL) {
            frame = 0;
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        boom();
    }

    public void damage(int damage) {
        damageAnimateTimer = 0f;
        frame = 1;
        hp -= damage;
        if (hp <= 0) {
            hp = 0;
            destroy();
        }
    }

    public int getDamage() {
        return damage;
    }

    public int getHp() {
        return hp;
    }

    protected void autoShoot(float delta) {
        reloadTimer += delta;
        if (reloadTimer >= reloadInterval) {
            reloadTimer = 0f;
            shoot();
        }
    }

    private void shoot() {
        SpriteBullet spriteBullet = poolBullets.obtain();
        spriteBullet.set(this, bulletTextureRegion, bulletPos, bulletV, bulletHeight, worldBounds, damage);
        shootSound.play(0.2f);
    }

    private void boom() {
        Explosion explosion = poolExplosions.obtain();
        explosion.set(pos, getHeight());
    }

}
