package ru.geekbrains.base;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.utils.Rect;

public class SpriteBullet extends Sprite {

    private Rect worldBounds;
    private final Vector2 v = new Vector2();
    private int damage;
    private Sprite owner;

    public SpriteBullet() {
        textureRegions = new TextureRegion[1];
    }

    public void set(
            Sprite owner,
            TextureRegion region,
            Vector2 pos0,
            Vector2 v0,
            float height,
            Rect worldBounds,
            int damage
    ) {
       this.owner = owner;
       this.textureRegions[0] = region;
       this.pos.set(pos0);
       this.v.set(v0);
       setHeightProportion(height);
       this.worldBounds = worldBounds;
       this.damage = damage;
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        if (isOutside(worldBounds)) {
            destroy();
        }
    }

    public int getDamage() {
        return damage;
    }

    public Object getOwner() {
        return owner;
    }
}
