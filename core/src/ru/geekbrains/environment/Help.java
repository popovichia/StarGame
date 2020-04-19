package ru.geekbrains.environment;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import ru.geekbrains.base.FontMain;
import ru.geekbrains.exceptions.GameException;
import ru.geekbrains.player.ShipPlayer;
import ru.geekbrains.pools.PoolSpritesBullets;
import ru.geekbrains.pools.PoolSpritesEnemies;
import ru.geekbrains.pools.PoolSpritesExplosions;
import ru.geekbrains.screens.ScreenGameOver;
import ru.geekbrains.utils.EnemyEmitter;
import ru.geekbrains.utils.Rect;

public class Help {

    private Game game;
    private Rect worldBounds;
    private SpriteBatch spriteBatch;

    private FontMain fontMain;
    private static final float FONT_MARGIN = 0.01f;
    private static final float FONT_SIZE = 0.02f;
    private static final float WAIT_INTERVAL = 180f;
    private static final StringBuilder STRING_BUILDER_START_GAME = new StringBuilder("START");
    private static final StringBuilder STRING_BUILDER_TOUCH_LEFT = new StringBuilder("TOUCH LEFT");
    private static final StringBuilder STRING_BUILDER_TOUCH_RIGHT = new StringBuilder("TOUCH RIGHT");
    private static final StringBuilder STRING_BUILDER_WAIT_ENEMY_SMALL = new StringBuilder("WAIT ENEMY SMALL AND SHOOT");
    private static final StringBuilder STRING_BUILDER_WAIT_ENEMY_MEDIUM = new StringBuilder("WAIT ENEMY MEDIUM AND SHOOT");
    private static final StringBuilder STRING_BUILDER_WAIT_ENEMY_BIG = new StringBuilder("WAIT ENEMY BIG AND SHOOT");

    private static String currentHelpStep = "START";
    private float speedInterval = 1f;
    private float currentInterval = 0f;

    private TextureAtlas textureAtlas;
    private TextureAtlas textureAtlasShipPlayer;
    private ShipPlayer shipPlayer;
    private ScreenGameOver screenGameOver;

    private PoolSpritesBullets poolBullets;
    private PoolSpritesEnemies poolSpritesEnemies;
    private PoolSpritesExplosions poolExplosions;
    private EnemyEmitter enemyEmitter;

    private Sound laserSound;
    private Sound bulletSound;
    private Sound explosionSound;
    private State state;
    private State prevState;

    private int frags;

    public Help(Rect worldBounds, SpriteBatch spriteBatch) {
        this.worldBounds = worldBounds;
        this.spriteBatch = spriteBatch;
        textureAtlasShipPlayer = new TextureAtlas(Gdx.files.internal("textures/ShipPlayer.pack"));
        textureAtlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        poolBullets = new PoolSpritesBullets();
        poolExplosions = new PoolSpritesExplosions(textureAtlas, explosionSound);
        poolSpritesEnemies = new PoolSpritesEnemies(poolBullets, poolExplosions, worldBounds);
        try {
            shipPlayer = new ShipPlayer(textureAtlasShipPlayer, poolBullets, poolExplosions, laserSound);
            shipPlayer.resize(worldBounds);
        } catch (GameException e) {
            throw new RuntimeException(e);
        }
        enemyEmitter = new EnemyEmitter(textureAtlas, poolSpritesEnemies, this.worldBounds, bulletSound);
        fontMain = new FontMain("fonts/fontMain.fnt", "fonts/fontMain.png");
        fontMain.setSize(FONT_SIZE);
        state = State.PLAYING;
        prevState = State.PLAYING;
        frags = 0;
    }
    public boolean Start(float delta) {
        boolean result = false;
        update(delta);
        checkCollisions();
        freeAllDestroyed();
        draw();
        result = true;
        return result;
    }

    private void update(float delta) {
        poolExplosions.updateActiveSprites(delta);
        if (state == State.PLAYING) {
            shipPlayer.update(delta);
            poolBullets.updateActiveSprites(delta);
            poolSpritesEnemies.updateActiveSprites(delta);
//            enemyEmitter.generate(delta, frags);
        } else if (state == State.GAME_OVER) {
//            buttonNewGame.update(delta);
        }
        if (currentInterval < WAIT_INTERVAL) {
            currentInterval += speedInterval;
        }
    }

    private void checkCollisions() {
        if (state != State.PLAYING) {
            return;
        }
    }

    private void freeAllDestroyed() {
        poolBullets.freeAllDestroyedActiveObjects();
        poolSpritesEnemies.freeAllDestroyedActiveObjects();
        poolExplosions.freeAllDestroyedActiveObjects();
    }

    private void draw() {
        switch (state) {
            case PLAYING:
                shipPlayer.draw(spriteBatch);
                poolSpritesEnemies.drawActiveSprites(spriteBatch);
                poolBullets.drawActiveSprites(spriteBatch);
                break;
            case GAME_OVER:
                game.setScreen(new ScreenGameOver(this.game));
                break;
        }
        poolExplosions.drawActiveSprites(spriteBatch);
        if (currentInterval < WAIT_INTERVAL && currentHelpStep.equals("START")) {
            fontMain.setColor(Color.BLACK);
            fontMain.draw(spriteBatch, STRING_BUILDER_START_GAME, 0f, 0f, Align.center);
        } else if (currentInterval >= WAIT_INTERVAL && currentHelpStep.equals("START")) {
            currentHelpStep = "MOVE LEFT";
            currentInterval = 0f;
        }
        if (currentInterval < WAIT_INTERVAL && currentHelpStep.equals("MOVE LEFT")) {
            fontMain.setColor(Color.BLUE);
            fontMain.draw(spriteBatch, STRING_BUILDER_TOUCH_LEFT, worldBounds.getLeft() + FONT_MARGIN, 0f, Align.left);
            if (worldBounds.getLeft() < shipPlayer.getLeft()) {
                shipPlayer.touchDown(new Vector2(worldBounds.getLeft(), 0f), 0, 0);
            } else {
                shipPlayer.touchUp(new Vector2(worldBounds.getLeft(), 0f), 0, 0);
            }
        } else if (currentInterval >= WAIT_INTERVAL && currentHelpStep.equals("MOVE LEFT")) {
            currentHelpStep = "MOVE RIGHT";
            currentInterval = 0f;
        }
        if (currentInterval < WAIT_INTERVAL && currentHelpStep.equals("MOVE RIGHT")) {
            fontMain.setColor(Color.GREEN);
            fontMain.draw(spriteBatch, STRING_BUILDER_TOUCH_RIGHT, worldBounds.getRight() + FONT_MARGIN, 0f, Align.right);
            if (worldBounds.getRight() > shipPlayer.getRight()) {
                shipPlayer.touchDown(new Vector2(worldBounds.getRight(), 0f), 0, 0);
            } else {
                shipPlayer.touchUp(new Vector2(worldBounds.getRight(), 0f), 0, 0);
            }
        } else if (currentInterval >= WAIT_INTERVAL && currentHelpStep.equals("MOVE RIGHT")) {
            currentHelpStep = "MOVE CENTER";
            currentInterval = 0f;
        }
        if (currentInterval < WAIT_INTERVAL && currentHelpStep.equals("MOVE CENTER")) {
            if (shipPlayer.pos.x > 0f) {
                shipPlayer.touchDown(new Vector2(worldBounds.getLeft(), 0f), 0, 0);
            } else {
                shipPlayer.touchUp(new Vector2(worldBounds.getLeft(), 0f), 0, 0);
            }
        } else if (currentInterval >= WAIT_INTERVAL && currentHelpStep.equals("MOVE CENTER")) {
            currentHelpStep = "WAIT ENEMY SMALL";
            currentInterval = 0f;
        }
        if (currentInterval < WAIT_INTERVAL && currentHelpStep.equals("WAIT ENEMY SMALL")) {
            fontMain.setColor(Color.BLACK);
            fontMain.draw(spriteBatch, STRING_BUILDER_WAIT_ENEMY_SMALL, 0f, 0f, Align.center);
        } else if (currentInterval >= WAIT_INTERVAL
                && currentHelpStep.equals("WAIT ENEMY SMALL")
                && poolSpritesEnemies.getActiveObjects().isEmpty()) {
            enemyEmitter.generate("small");
            currentHelpStep = "WAIT ENEMY MEDIUM";
            currentInterval = 0f;
        }
        if (currentInterval < WAIT_INTERVAL
                && currentHelpStep.equals("WAIT ENEMY MEDIUM")) {
            fontMain.setColor(Color.BLACK);
            fontMain.draw(spriteBatch, STRING_BUILDER_WAIT_ENEMY_MEDIUM, 0f, 0f, Align.center);
        } else if (currentInterval >= WAIT_INTERVAL
                && currentHelpStep.equals("WAIT ENEMY MEDIUM")
                && poolSpritesEnemies.getActiveObjects().isEmpty()) {
            enemyEmitter.generate("medium");
            currentHelpStep = "WAIT ENEMY BIG";
            currentInterval = 0f;
        }
        if (currentInterval < WAIT_INTERVAL && currentHelpStep.equals("WAIT ENEMY BIG")) {
            fontMain.setColor(Color.BLACK);
            fontMain.draw(spriteBatch, STRING_BUILDER_WAIT_ENEMY_BIG, 0f, 0f, Align.center);
        } else if (currentInterval >= WAIT_INTERVAL
                && currentHelpStep.equals("WAIT ENEMY BIG")
                && poolSpritesEnemies.getActiveObjects().isEmpty()) {
            enemyEmitter.generate("big");
            currentHelpStep = "";
        }
    }
    public String getFrags() {
        return String.valueOf(frags);
    }
    public String getPlayerShipHp() {
        return String.valueOf(shipPlayer.getHp());
    }
    public String getGameLevel() {
        return String.valueOf(enemyEmitter.getLevel());
    }
    public boolean dispose() {
        boolean result = false;
        result = true;
        return result;
    }
}
