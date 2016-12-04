package com.exovum.ld37warmup;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.exovum.testgame.ScreenDispatcher;

/**
 * Created by exovu_000 on 12/3/2016.
 */

public class LD37Warmup extends Game {

    SpriteBatch batch;
    AssetManager am;

    private ScreenDispatcher screenDispatcher;

    @Override
    public void create() {
        // Load the assets before hand, and setup AssetManager to access later
        am = Assets.load();
        batch = new SpriteBatch();
        Screen splashScreen = new SplashScreenWarmup(batch, screenDispatcher);
        Screen gameScreen = new GameScreenWarmup(batch, screenDispatcher);
        screenDispatcher.AddScreen(splashScreen);
        screenDispatcher.AddScreen(gameScreen);
        setScreen(splashScreen);
    }

    @Override
    public void render () {
        float r = 0/255f;
        float g = 24f/255f;
        float b = 72f/255f;
        Gdx.gl.glClearColor(r, g, b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Screen nextScreen = screenDispatcher.getNextScreen();
        if(nextScreen != getScreen()){
            setScreen(nextScreen);
        }

        super.render();
    }
}
