package ru.geekbrains.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.exceptions.GameException;
import ru.geekbrains.utils.Rect;
import ru.geekbrains.utils.Regions;

public class Sprite extends Rect {

    protected float angle;
    protected float scale = 1f;
    protected TextureRegion[] textureRegions;
    protected int frame;
    private boolean destroyed = false;

    public Sprite() {

    }

    public Sprite(TextureRegion textureRegion) throws GameException {
        if (textureRegion == null) {
            throw new GameException("Region is null");
        }
        textureRegions = new TextureRegion[1];
        textureRegions[0] = textureRegion;
    }

    public Sprite(TextureRegion textureRegion, int rows, int cols, int frames) throws GameException {
        if (textureRegion == null) {
            throw new GameException("Region is null");
        }
        this.textureRegions = Regions.split(textureRegion, rows, cols, frames);
    }

    public void setHeightProportion(float height) {
        setHeight(height);
        float aspect = textureRegions[frame].getRegionWidth() / (float) textureRegions[frame].getRegionHeight();
        setWidth(height * aspect);
    }

    public void update(float delta) {
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(
                textureRegions[frame],
                getLeft(),
                getBottom(),
                halfWidth,
                halfHeight,
                getWidth(),
                getHeight(),
                scale,
                scale,
                angle
        );
    }

    public void resize(Rect worldBounds) {

    }

    public boolean touchDown(Vector2 touch, int pointer, int button) {
        return false;
    }

    public boolean touchUp(Vector2 touch, int pointer, int button) {
        return false;
    }

    public boolean touchDragged(Vector2 touch, int pointer) {
        return false;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void destroy() {
        destroyed = true;
    }

    public void flushDestroy() {
        destroyed = false;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
