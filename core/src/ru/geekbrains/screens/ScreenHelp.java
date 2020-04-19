package ru.geekbrains.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Align;

import ru.geekbrains.base.FontMain;
import ru.geekbrains.base.ScreenBase;
import ru.geekbrains.environment.Help;
import ru.geekbrains.environment.SpriteBackground;
import ru.geekbrains.exceptions.GameException;
import ru.geekbrains.utils.Rect;

public class ScreenHelp extends ScreenBase {

    private Game game;
    private Help help;

    private Texture textureBackground;
    private SpriteBackground spriteBackground;

    private Music music;

    private FontMain fontMain;
    private static final float FONT_SIZE = 0.02f;
    private static final float FONT_MARGIN = 0.01f;
    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";
    private StringBuilder sbFrags;
    private StringBuilder sbHP;
    private StringBuilder sbLevel;

    public ScreenHelp(Game game) {
        this.game = game;
    }
    @Override
    public void show() {
        super.show();
        textureBackground = new Texture("pictures/BackgroundHelp.jpeg");
        try {
            spriteBackground = new SpriteBackground(textureBackground);
        } catch (GameException gameException) {
            gameException.printStackTrace();
        }
        music = Gdx.audio.newMusic(Gdx.files.internal("musics/help.mp3"));
        music.setLooping(true);
        music.play();
        fontMain = new FontMain("fonts/fontMain.fnt", "fonts/fontMain.png");
        fontMain.setSize(FONT_SIZE);
        sbFrags = new StringBuilder();
        sbHP = new StringBuilder();
        sbLevel = new StringBuilder();
        help = new Help(worldBounds, spriteBatch);
    }

    @Override
    public void pause() {
    }
    @Override
    public void resume() {
    }
    @Override
    public void render(float delta) {
        super.render(delta);
        spriteBatch.begin();
        spriteBackground.draw(spriteBatch);
        printGameInfo();
        help.Start(delta);
        spriteBatch.end();
    }
    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        spriteBackground.resize(worldBounds);
    }
    @Override
    public void dispose() {
        textureBackground.dispose();
        music.dispose();
        fontMain.dispose();
        super.dispose();
//        textureAtlas.dispose();
//        poolBullets.dispose();
//        poolSpritesEnemies.dispose();
//        laserSound.dispose();
//        bulletSound.dispose();
//        explosionSound.dispose();
    }
    private void printGameInfo() {
        sbFrags.setLength(0);
        fontMain.draw(spriteBatch,
                sbFrags.append(FRAGS).append(help.getFrags()),
                worldBounds.getLeft() + FONT_MARGIN,
                worldBounds.getTop() - FONT_MARGIN,
                Align.left);
        sbHP.setLength(0);
        fontMain.draw(spriteBatch,
                sbHP.append(HP).append(help.getPlayerShipHp()),
                worldBounds.pos.x,
                worldBounds.getTop() - FONT_MARGIN,
                Align.center);
        sbLevel.setLength(0);
        fontMain.draw(spriteBatch,
                sbLevel.append(LEVEL).append(help.getGameLevel()),
                worldBounds.getRight() - FONT_MARGIN,
                worldBounds.getTop() - FONT_MARGIN,
                Align.right);
    }
}
