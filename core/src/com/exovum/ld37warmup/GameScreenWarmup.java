package com.exovum.ld37warmup;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.exovum.ld37warmup.components.BodyComponent;
import com.exovum.ld37warmup.components.TransformComponent;
import com.exovum.ld37warmup.components.TreeComponent;
import com.exovum.ld37warmup.components.AnimationComponent;
import com.exovum.ld37warmup.components.StateComponent;
import com.exovum.ld37warmup.components.TextureComponent;
import com.exovum.ld37warmup.systems.AnimationSystem;
import com.exovum.ld37warmup.systems.PhysicsDebugSystem;
import com.exovum.ld37warmup.systems.PhysicsSystem;
import com.exovum.ld37warmup.systems.RenderingSystem;
import com.exovum.ld37warmup.systems.UselessStateSwapSystem;
import com.exovum.testgame.IScreenDispatcher;

/**
 * Created by exovu_000 on 12/3/2016.
 */

public class GameScreenWarmup extends ScreenAdapter {

    private boolean initialized;
    private float elapsedTime = 0f;
    private int secondsToSplash = 10;
    private World world; // Box2D World

    private PooledEngine engine; // Ashley ECS engine

    private SpriteBatch batch;
    private IScreenDispatcher dispatcher;

    public GameScreenWarmup(SpriteBatch batch, IScreenDispatcher dispatcher) {
        super();
        this.batch = batch;
        this.dispatcher = dispatcher;
    }

    private void init() {
        Gdx.app.log("Game Screen Warmup", "Initializing");
        initialized = true;

        // world has 0 x-acceleration, and -9.8f y-accel [gravity]
        world = new World(new Vector2(0f, -9.8f), true);
        engine = new PooledEngine();

        // create ECS system to process rendering. renders entities via TextureComponents
        RenderingSystem renderingSystem = new RenderingSystem(batch);
        engine.addSystem(renderingSystem);
        engine.addSystem(new AnimationSystem());
        // add ECS system to process physics in the Box2D world
        engine.addSystem(new PhysicsSystem(world));

        engine.addSystem(new PhysicsDebugSystem(world, renderingSystem.getCamera()));
        engine.addSystem(new UselessStateSwapSystem());

        //Entity e = buildBall(world);
        //Entity e = buildTree(world);
        //engine.addEntity(e);
        engine.addEntity(buildTreeEntity(world));

        initialized = true;
    }

    private void update(float delta) {
        engine.update(delta);

        elapsedTime += delta;
        //if elapsedTime/1000f > secondsToEvent { then do stuff }
    }

    @Override
    public void render(float delta) {
        if(initialized) {
            update(delta);
        } else {
            init();
        }
    }

    private Entity buildTree(World world) {
        Entity e = engine.createEntity();
        e.add(new TreeComponent());

        AnimationComponent a = new AnimationComponent();
        a.animations.put("DEFAULT", new Animation(1f/16f, Assets.getTreeArray(), Animation.PlayMode.LOOP));
        a.animations.put("RUNNING", new Animation(1f/8f, Assets.getTreeMoveArray(), Animation.PlayMode.LOOP));
        e.add(a);
        StateComponent state = new StateComponent();
        state.set("DEFAULT");
        e.add(state);
        TextureComponent tc = new TextureComponent();
        e.add(tc);

        TransformComponent tfc = new TransformComponent();
        tfc.position.set(10f, 10f, 1f);
        tfc.rotation = 15f;
        tfc.scale.set(0.25f, 0.25f);
        e.add(tfc);

        // setup the physics body
        BodyComponent bc = new BodyComponent();
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        bodyDef.position.set(10f, 23f);

        bc.body = world.createBody(bodyDef);
        //bc.body.applyAngularImpulse(50f, true); // apply impulse to move body

        CircleShape circle = new CircleShape();
        circle.setRadius(tc.region.getRegionWidth());

        //Create fixture definition to apply to the shape
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 40f;
        fixtureDef.friction = 0.4f;

        return e;
    }

    private Entity buildTreeEntity(World world) {
        Entity e = engine.createEntity();
        // Add Tree Component to Tree Entity
        e.add(new TreeComponent());

        // Add Animation Component to Tree Entity
        AnimationComponent a = new AnimationComponent();
        // for tag "DEFAULT" put an animation with parameter: duration=1/8f, TextureRegion, PlayMode;
        a.animations.put("DEFAULT", new Animation(1f/8f, Assets.getTreeArray(), Animation.PlayMode.LOOP));
        a.animations.put("RUNNING", new Animation(1f/8f, Assets.getTreeMoveArray(), Animation.PlayMode.LOOP));
        e.add(a);

        // Add State Component to Tree Entity
        StateComponent state = new StateComponent();
        state.set("DEFAULT");
        e.add(state);

        // Add Texture Component to Tree Entity
        TextureComponent tc = new TextureComponent();
        e.add(tc);

        // Add TransformComponent to Tree Entity
        TransformComponent tfc = new TransformComponent();
        tfc.position.set(30f, 10f, 1f);
        tfc.rotation = 0f; // try no rotation
        tfc.scale.set(1.0f, 1.0f); // try no scale
        e.add(tfc);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        // set position of body into the world
        //bodyDef.position.set(new Vector2(6f, 1f));
        bodyDef.position.set(10f, 23f);

        BodyComponent bc = new BodyComponent();
        // create body from bodyDef and add to the world
        bc.body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(2f);
        //PolygonShape treeBox = new PolygonShape();
        //treeBox.setAsBox(5f, 1f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 20f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f; // makes it bounce slightly
        bc.body.createFixture(fixtureDef);
        //bc.body.createFixture(treeBox, 0.0f);

        // Clean Up
        circle.dispose();
        //treeBox.dispose();

        // Add Body Component to Tree Entity
        e.add(bc);

        // This isn't used anymore?
        Vector2 screenMeters = RenderingSystem.getScreenSizeInMeters();
        Gdx.app.log("Game Screen Warmup", "Screen Meters: " + screenMeters.x + " x " + screenMeters.y);
        Vector2 screenPixels = RenderingSystem.getScreenSizeInPixesl();
        Gdx.app.log("Game Screen Warmup", "Screen Pixels:" + screenPixels.x + " x " + screenPixels.y);

        return e;
    }

    private Entity buildBall(World world) {
        // TODO buildBall
        Entity e = engine.createEntity();
        e.add(new BallComponent());

        AnimationComponent a = new AnimationComponent();
        a.animations.put("DEFAULT", new Animation(1f/16f, Assets.getBallArray(), Animation.PlayMode.LOOP));
        a.animations.put("MOVING", new Animation(1f/16f, Assets.getBallMoveArray(), Animation.PlayMode.LOOP));
        e.add(a);

        return e;
    }

}
