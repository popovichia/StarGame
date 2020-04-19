package ru.geekbrains.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import ru.geekbrains.base.ScreenBase;
import ru.geekbrains.base.FontMain;
import ru.geekbrains.exceptions.GameException;
import ru.geekbrains.utils.Rect;
import ru.geekbrains.pools.PoolSpritesBullets;
import ru.geekbrains.pools.PoolSpritesEnemies;
import ru.geekbrains.pools.PoolSpritesExplosions;
import ru.geekbrains.environment.SpriteBackground;
import ru.geekbrains.base.SpriteBullet;
import ru.geekbrains.environment.ButtonNewGame;
import ru.geekbrains.enemies.ShipEnemy;
import ru.geekbrains.player.ShipPlayer;
import ru.geekbrains.environment.Star;
import ru.geekbrains.utils.EnemyEmitter;

public class ScreenGameLevel1 extends ScreenBase {

    private enum State {PLAYING, PAUSE, GAME_OVER}
    private Game game;
    private static final int STAR_COUNT = 16;
    private static final float FONT_MARGIN = 0.01f;
    private static final float FONT_SIZE = 0.02f;
    private static final String FRAGS = "Frags: ";
    private static final String HP = "HP: ";
    private static final String LEVEL = "Level: ";

    private Texture textureBackground;
    private SpriteBackground background;

    private TextureAtlas atlas;
    private TextureAtlas textureAtlasShipPlayer;
    private Star[] stars;
    private ShipPlayer shipPlayer;
    private ScreenGameOver screenGameOver;
    private ButtonNewGame buttonNewGame;

    private PoolSpritesBullets poolBullets;
    private PoolSpritesEnemies poolEnemies;
    private PoolSpritesExplosions poolExplosions;
    private EnemyEmitter enemyEmitter;

    private Music music;
    private Sound laserSound;
    private Sound bulletSound;
    private Sound explosion;
    private State state;
    private State prevState;

    private FontMain fontMain;
    private StringBuilder sbFrags;
    private StringBuilder sbHP;
    private StringBuilder sbLevel;

    private int frags;
    public ScreenGameLevel1(Game game) {
        this.game = game;
    }
    @Override
    public void show() {
        super.show();
        textureBackground = new Texture("pictures/BackgroundGameLevel1.jpg");
        textureAtlasShipPlayer = new TextureAtlas(Gdx.files.internal("textures/ShipPlayer.pack"));
        atlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        explosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        poolBullets = new PoolSpritesBullets();
        poolExplosions = new PoolSpritesExplosions(atlas, explosion);
        poolEnemies = new PoolSpritesEnemies(poolBullets, poolExplosions, worldBounds);
        enemyEmitter = new EnemyEmitter(atlas, poolEnemies, worldBounds, bulletSound);
        fontMain = new FontMain("fonts/fontMain.fnt", "fonts/fontMain.png");
        fontMain.setSize(FONT_SIZE);
        sbFrags = new StringBuilder();
        sbHP = new StringBuilder();
        sbLevel = new StringBuilder();
        music = Gdx.audio.newMusic(Gdx.files.internal("musics/level1.mp3"));
        music.setLooping(true);
        music.play();
        initSprites();
        state = State.PLAYING;
        prevState = State.PLAYING;
        frags = 0;
    }

    public void startNewGame() {
        state = State.PLAYING;
        shipPlayer.startNewGame(worldBounds);
        frags = 0;
        poolBullets.freeAllActiveObjects();
        poolEnemies.freeAllActiveObjects();
        poolExplosions.freeAllActiveObjects();
    }

    @Override
    public void pause() {
        prevState = state;
        state = State.PAUSE;
        music.pause();
    }

    @Override
    public void resume() {
        state = prevState;
        music.play();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        checkCollisions();
        freeAllDestroyed();
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (Star star : stars) {
            star.resize(worldBounds);
        }
        shipPlayer.resize(worldBounds);
        buttonNewGame.resize(worldBounds);
    }

    @Override
    public void dispose() {
        textureBackground.dispose();
        atlas.dispose();
        poolBullets.dispose();
        poolEnemies.dispose();
        music.dispose();
        laserSound.dispose();
        bulletSound.dispose();
        explosion.dispose();
        fontMain.dispose();
        super.dispose();
    }


    @Override
    public boolean keyDown(int keycode) {
        if (state == State.PLAYING) {
            shipPlayer.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == State.PLAYING) {
            shipPlayer.keyUp(keycode);
        }
        return false;
    }
    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING) {
            shipPlayer.touchDown(touch, pointer, button);
        } else if (state == State.GAME_OVER) {
            buttonNewGame.touchDown(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING) {
            shipPlayer.touchUp(touch, pointer, button);
        } else if (state == State.GAME_OVER) {
            buttonNewGame.touchUp(touch, pointer, button);
        }
        return false;
    }

    private void initSprites() {
        try {
            background = new SpriteBackground(textureBackground);
            stars = new Star[STAR_COUNT];
            for (int i = 0; i < STAR_COUNT; i++) {
                stars[i] =  new Star(atlas);
            }
            shipPlayer = new ShipPlayer(textureAtlasShipPlayer, poolBullets, poolExplosions, laserSound);
            buttonNewGame = new ButtonNewGame(atlas, this);
        } catch (GameException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(float delta) {
        for (Star star : stars) {
            star.update(delta);
        }
        poolExplosions.updateActiveSprites(delta);
        if (state == State.PLAYING) {
            shipPlayer.update(delta);
            poolBullets.updateActiveSprites(delta);
            poolEnemies.updateActiveSprites(delta);
            enemyEmitter.generate(delta, frags);
        } else if (state == State.GAME_OVER) {
            buttonNewGame.update(delta);
        }

    }

    private void checkCollisions() {
        if (state != State.PLAYING) {
            return;
        }
        List<ShipEnemy> shipEnemyList = poolEnemies.getActiveObjects();
        List<SpriteBullet> spriteBulletList = poolBullets.getActiveObjects();
        for (ShipEnemy shipEnemy : shipEnemyList) {
            if (shipEnemy.isDestroyed()) {
                continue;
            }
            float minDist = shipEnemy.getHalfWidth() + shipPlayer.getHalfWidth();
            if (shipPlayer.pos.dst(shipEnemy.pos) < minDist) {
                shipEnemy.destroy();
                frags++;
                shipPlayer.damage(shipEnemy.getDamage());
            }
            for (SpriteBullet spriteBullet : spriteBulletList) {
                if (spriteBullet.getOwner() != shipPlayer || spriteBullet.isDestroyed()) {
                    continue;
                }
                if (shipEnemy.isBulletCollision(spriteBullet)) {
                    shipEnemy.damage(spriteBullet.getDamage());
                    spriteBullet.destroy();
                    if (shipEnemy.isDestroyed()) {
                        frags++;
                    }
                }
            }
        }
        for (SpriteBullet spriteBullet : spriteBulletList) {
            if (spriteBullet.getOwner() == shipPlayer || spriteBullet.isDestroyed()) {
                continue;
            }
            if (shipPlayer.isBulletCollision(spriteBullet)) {
                shipPlayer.damage(spriteBullet.getDamage());
                spriteBullet.destroy();
            }
        }
        if (shipPlayer.isDestroyed()) {
            state = State.GAME_OVER;
        }
    }

    private void freeAllDestroyed() {
        poolBullets.freeAllDestroyedActiveObjects();
        poolEnemies.freeAllDestroyedActiveObjects();
        poolExplosions.freeAllDestroyedActiveObjects();
    }

    private void draw() {
        Gdx.gl.glClearColor(0.5f, 0.7f, 0.8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        background.draw(spriteBatch);
        for (Star star : stars) {
            star.draw(spriteBatch);
        }
        switch (state) {
            case PLAYING:
                shipPlayer.draw(spriteBatch);
                poolEnemies.drawActiveSprites(spriteBatch);
                poolBullets.drawActiveSprites(spriteBatch);
                break;
            case GAME_OVER:
                game.setScreen(new ScreenGameOver(this.game));
                break;
        }
        poolExplosions.drawActiveSprites(spriteBatch);
        printInfo();
        spriteBatch.end();
    }

    private void printInfo() {
        sbFrags.setLength(0);
        sbHP.setLength(0);
        sbLevel.setLength(0);
        fontMain.draw(spriteBatch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft() + FONT_MARGIN, worldBounds.getTop() - FONT_MARGIN);
        fontMain.draw(spriteBatch, sbHP.append(HP).append(shipPlayer.getHp()), worldBounds.pos.x, worldBounds.getTop() - FONT_MARGIN, Align.center);
        fontMain.draw(spriteBatch, sbLevel.append(LEVEL).append(enemyEmitter.getLevel()), worldBounds.getRight() - FONT_MARGIN, worldBounds.getTop() - FONT_MARGIN, Align.right);
    }
}