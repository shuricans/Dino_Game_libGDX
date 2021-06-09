package com.mygdx.dino.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.dino.Screens.PlayScreen;

import java.util.concurrent.ThreadLocalRandom;

import static com.mygdx.dino.Utils.Constants.*;

public class Cloud extends Sprite {

    public World world;
    public Body body;
    private final static float speed = -1.2f / PPM;

    private float timer;
    private float zeroPointX;
    private float halfWidth, halfHeight;

    public Cloud(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion(REGION_NAME_CLOUD));
        this.world = world;

        defineTextureCloud();
        defineBodyCloud();

        timer += Gdx.graphics.getDeltaTime();
    }

    private void defineTextureCloud() {
        TextureRegion textureRegion = new TextureRegion(
                getTexture(),
                getRegionX(),
                getRegionY(),
                getRegionWidth(),
                getRegionHeight());

        setBounds(0, 0, getRegionWidth() / PPM, getRegionHeight() / PPM);
        setRegion(textureRegion);

        zeroPointX = -getRegionWidth() / 2f / PPM;
        halfWidth = getWidth() / 2;
        halfHeight = getHeight() / 2;
    }

    private void defineBodyCloud() {

        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.KinematicBody;
        bDef.position.set((V_WIDTH + getRegionWidth() / 2f) / PPM, getRandomY());
        body = world.createBody(bDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(getRegionWidth() / 2f / PPM, getRegionHeight() / 2f / PPM);

        body.createFixture(polygonShape, 1f);
        body.setActive(false);

        polygonShape.dispose();
    }

    public void update() {
        setPosition(body.getPosition().x - halfWidth, body.getPosition().y - halfHeight);

        body.setTransform(body.getPosition().x + speed, body.getPosition().y, 0);

        if (body.getPosition().x <= zeroPointX) {
            body.setTransform((V_WIDTH + getRegionWidth() / 2f) / PPM,
                    getRandomY(),
                    0);
        }
    }

    private float getRandomY() {
        return ThreadLocalRandom.current().nextInt(55, 170) / PPM;
    }

    public void letsGo() {

    }

}
