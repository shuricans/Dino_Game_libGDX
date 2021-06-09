package com.mygdx.dino.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.dino.Dino;
import com.mygdx.dino.Scenes.Hud;
import com.mygdx.dino.Sprites.Bird;
import com.mygdx.dino.Sprites.Cloud;
import com.mygdx.dino.Sprites.Ground;
import com.mygdx.dino.Sprites.TRex;
import com.mygdx.dino.Utils.WorldConctactListener;

import static com.mygdx.dino.Utils.Constants.V_HEIGHT;
import static com.mygdx.dino.Utils.Constants.V_WIDTH;
import static com.mygdx.dino.Utils.Constants.PPM;

public class PlayScreen implements Screen {

    static final int GAME_READY = 0;
    static final int GAME_RUNNING = 1;
    static final int GAME_OVER = 2;

    int state;

    public boolean gameOver = false;

    private Dino game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Hud hud;
    private final TextureAtlas atlas;

    private World world;
    private Box2DDebugRenderer b2dr;

    private final TRex tRex;
    private Bird bird;
    private Cloud cloud;
    private Ground ground, ground2;

    public PlayScreen(Dino game) {
        state = GAME_READY;

        atlas = new TextureAtlas("texture.pack");

        this.game = game;
        gameCam = new OrthographicCamera();

        //create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort = new FitViewport(V_WIDTH / PPM, V_HEIGHT / PPM, gameCam);

        //create our game HUD for scores
        hud = new Hud(game.batch);

        //initially set our gamcam to be centered correctly at the start of of map
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        //create our Box2D world, setting no gravity in X, -10 gravity in Y
        world = new World(new Vector2(0, -11), false);

        world.setContactListener(new WorldConctactListener(this));

        //allows for debug lines of our box2d world.
        b2dr = new Box2DDebugRenderer();

        createPlatform(V_WIDTH, 30);

        tRex = new TRex(world, this);
        bird = new Bird(world, this);
        cloud = new Cloud(world, this);
        ground = new Ground(world, this, true);
        ground2 = new Ground(world, this, false);
    }


    private void update(float dt) {
        //handle user input first
        handleInput();

        //takes 1 step in the physics simulation(60 times per second)
        world.step(1 / 60f, 6, 2);

        hud.update(dt);

        tRex.update(dt);
        cloud.update();
        ground.update(dt);
        ground2.update(dt);
        bird.update(dt);
    }

    private void handleInput() {
        if ((Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) &&
                tRex.body.getLinearVelocity().y == 0) {
            tRex.body.applyLinearImpulse(new Vector2(0, 1.3f), tRex.body.getWorldCenter(), true);
            tRex.lowRunning = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && tRex.body.getLinearVelocity().y == 0) {
            tRex.lowRunning = true;
        }
        if (!Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            tRex.lowRunning = false;
        }

    }

    private Body createPlatform(float width, float height) {

        Body body;
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.StaticBody;
        bDef.position.set(width / 2 / PPM, height / 2 / PPM);
        body = world.createBody(bDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(width / 2 / PPM, height / 2 / PPM);

        body.createFixture(polygonShape, 1.0f);
        polygonShape.dispose();

        return body;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

            update(Gdx.graphics.getDeltaTime());

            Gdx.gl.glClearColor(1, 1, 1, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


            //renderer our Box2DDebugLines
            b2dr.render(world, gameCam.combined);

            game.batch.setProjectionMatrix(gameCam.combined);

            game.batch.begin();
            ground.draw(game.batch);
            ground2.draw(game.batch);
            cloud.draw(game.batch);
            bird.draw(game.batch);
            tRex.draw(game.batch);
            game.batch.end();

            game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
            hud.stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        world.dispose();
        hud.dispose();
        b2dr.dispose();
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }
}
