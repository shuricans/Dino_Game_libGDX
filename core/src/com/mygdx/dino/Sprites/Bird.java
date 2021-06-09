package com.mygdx.dino.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.dino.Screens.PlayScreen;

import java.util.concurrent.ThreadLocalRandom;

import static com.mygdx.dino.Utils.Constants.*;

public class Bird extends Sprite {

    public World world;
    public Body body;

    public boolean isFly = false;
    private final static float speed = -4f / PPM;

    private float zeroPointX;
    private float halfWidth, halfHeight;

    private Fixture fixture;

    private float stateTimer = 0f;
    private Animation<TextureRegion> tBirdFly;

    public Bird(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion(REGION_NAME_BIRD));
        this.world = world;

        defineTextureBird();
        defineBodyBird();
    }

    private void defineTextureBird() {
        Array<TextureRegion> frames = new Array<>();

        for (int i = 0; i < 2; i++) {
            frames.add(
                    new TextureRegion(
                            getTexture(),
                            i * getRegionWidth() + getRegionX(),
                            getRegionY(),
                            getRegionWidth(),
                            getRegionHeight()));
        }
        tBirdFly = new Animation<>(0.1f, frames);
        frames.clear();

        setBounds(0, 0, getRegionWidth() / PPM, getRegionHeight() / PPM);

        zeroPointX = -getRegionWidth() / 2f / PPM;
        halfWidth = getWidth() / 2;
        halfHeight = getHeight() / 2;
    }

    private void defineBodyBird() {

        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.KinematicBody;
        bDef.position.set((V_WIDTH + getRegionWidth() / 2f) / PPM, getRandomY());
        body = world.createBody(bDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(getRegionWidth() / 2f / PPM);

        body.createFixture(circleShape, 1f);
        body.setActive(true);

        fixture = body.getFixtureList().get(0);
        fixture.setUserData("bird");

        circleShape.dispose();
    }

    public void update(float dt) {

        if (body.getPosition().x <= zeroPointX) {
            body.setTransform((V_WIDTH + getRegionWidth() / 2f) / PPM,
                    getRandomY(),
                    0);
        }

        setPosition(body.getPosition().x - halfWidth, body.getPosition().y - halfHeight);

        body.setTransform(body.getPosition().x + speed, body.getPosition().y, 0);


        stateTimer += Gdx.graphics.getDeltaTime();
        setRegion(tBirdFly.getKeyFrame(stateTimer + dt, true));

    }

    private float getRandomY() {
        int x = ThreadLocalRandom.current().nextInt(10) >= 5 ? 0 : 1;
        if(x == 0) { // low fly
            return ThreadLocalRandom.current().nextInt(55, 68) / PPM;
        } // high fly
        return ThreadLocalRandom.current().nextInt(95, 170) / PPM;
    }
}














