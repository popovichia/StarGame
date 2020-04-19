package ru.geekbrains.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import ru.geekbrains.base.ScreenBase;
import ru.geekbrains.base.FontMain;
import ru.geekbrains.environment.ButtonHelp;
import ru.geekbrains.exceptions.GameException;
import ru.geekbrains.utils.Rect;
import ru.geekbrains.environment.SpriteBackground;
import ru.geekbrains.environment.ButtonExit;
import ru.geekbrains.environment.ButtonPlay;

public class ScreenMenu extends ScreenBase {

    private final Game game;
    private Music musicMenu;
    private Texture textureBackgroundMenu;
    private SpriteBackground spriteBackground;

    private TextureAtlas textureAtlasButtons;

    private ButtonExit buttonExit;
    private ButtonPlay buttonPlay;
    private ButtonHelp buttonHelp;

    private FontMain fontMain;
    private final String AUTHOR = "Author: Popovich";
    private StringBuilder author;

    public ScreenMenu(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        textureBackgroundMenu = new Texture("pictures/BackgroundMenu.jpg");
        textureAtlasButtons = new TextureAtlas(Gdx.files.internal("textures/buttons.pack"));
        fontMain = new FontMain("fonts/fontMain.fnt", "fonts/fontMain.png");
        fontMain.setSize(0.015f);
        musicMenu = Gdx.audio.newMusic(Gdx.files.internal("musics/menu.mp3"));
        musicMenu.setLooping(true);
        musicMenu.setVolume(0.6f);
        musicMenu.play();
        author = new StringBuilder();
        initSprites();
    }

    @Override
    public void render(float delta) {
       update(delta);
       draw();
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        textureBackgroundMenu.dispose();
        textureAtlasButtons.dispose();
        fontMain.dispose();
        musicMenu.dispose();
        super.dispose();
    }

    @Override
    public void resize(Rect worldBounds) {
        spriteBackground.resize(worldBounds);
        buttonExit.resize(worldBounds);
        buttonPlay.resize(worldBounds);
        buttonHelp.resize(worldBounds);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        buttonExit.touchDown(touch, pointer, button);
        buttonPlay.touchDown(touch, pointer, button);
        buttonHelp.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        buttonExit.touchUp(touch, pointer, button);
        buttonPlay.touchUp(touch, pointer, button);
        buttonHelp.touchUp(touch, pointer, button);
        return false;
    }

    private void initSprites() {
        try {
            spriteBackground = new SpriteBackground(textureBackgroundMenu);
            buttonExit = new ButtonExit(textureAtlasButtons);
            buttonPlay = new ButtonPlay(textureAtlasButtons, game);
            buttonHelp = new ButtonHelp(textureAtlasButtons, game);
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
        spriteBackground.draw(spriteBatch);
        buttonExit.draw(spriteBatch);
        buttonPlay.draw(spriteBatch);
        buttonHelp.draw(spriteBatch);
        printInfo();
        spriteBatch.end();
    }
    private void printInfo() {
        author.setLength(0);
        fontMain.draw(spriteBatch, author.append(AUTHOR), worldBounds.getRight() - 0.02f, worldBounds.getTop() - 0.02f, Align.right);
    }
}
