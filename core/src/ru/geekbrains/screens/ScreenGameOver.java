package ru.geekbrains.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.FontMain;
import ru.geekbrains.base.ScreenBase;
import ru.geekbrains.environment.ButtonNewGame;
import ru.geekbrains.environment.SpriteBackground;
import ru.geekbrains.environment.Star;
import ru.geekbrains.exceptions.GameException;
import ru.geekbrains.utils.Rect;

public class ScreenGameOver extends ScreenBase {
    private Game game;

    private Texture textureBackground;
    private SpriteBackground background;

    private ButtonNewGame buttonNewGame;
    private FontMain fontMain;
    private static final float FONT_SIZE = 0.02f;

    private Music music;

    private StringBuilder sbFrags;
    private StringBuilder sbHP;
    private StringBuilder sbLevel;

    private int frags;
    public ScreenGameOver(Game game) {
        this.game = game;
    }
    @Override
    public void show() {
        super.show();
        textureBackground = new Texture("pictures/BackgroundGameOver.jpeg");
        music = Gdx.audio.newMusic(Gdx.files.internal("musics/music.mp3"));
        music.setLooping(true);
        music.play();
        initSprites();
        frags = 0;
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void render(float delta) {
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
    }

    @Override
    public void dispose() {
        textureBackground.dispose();
        music.dispose();
        super.dispose();
    }


    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }
    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        return false;
    }

    private void initSprites() {
        try {
            background = new SpriteBackground(textureBackground);
        } catch (GameException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(float delta) {
    }

    private void draw() {
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        background.draw(spriteBatch);
        spriteBatch.end();
    }

}
