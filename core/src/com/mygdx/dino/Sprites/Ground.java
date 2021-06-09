package com.mygdx.dino.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.dino.Screens.PlayScreen;

import static com.mygdx.dino.Utils.Constants.*;

public class Ground extends Sprite {

    public World world;
    public Body body;
    private final static float speed = -3f / PPM;
    private boolean isFirst = true;

    private float zeroPointX;
    private float halfWidth, halfHeight;

    public Ground(World world, PlayScreen screen, boolean isFirst) {
        super(screen.getAtlas().findRegion(REGION_NAME_ROAD));
        this.world = world;
        this.isFirst = isFirst;

        defineTextureRoad();
        defineBodyRoad();
    }

    private void defineTextureRoad() {
        TextureRegion textureRegion = new TextureRegion(
                getTexture(),
                getRegionX(),
                getRegionY(),
                getRegionWidth(),
                getRegionHeight());

        setBounds(0, 0, getRegionWidth() / PPM, getRegionHeight() / PPM);
        setRegion(textureRegion);

        zeroPointX = -V_WIDTH / 2f / PPM;
        halfWidth = getWidth() / 2;
        halfHeight = getHeight() / 2;
    }

    private void defineBodyRoad() {
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.KinematicBody;
        if(isFirst) {
            bDef.position.set(V_WIDTH / 2f / PPM, (getRegionHeight() + 50) / 2f / PPM);
        } else {
            bDef.position.set((V_WIDTH + V_WIDTH / 2f) / PPM, (getRegionHeight() + 50) / 2f / PPM);
        }
        body = world.createBody(bDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(getRegionWidth() / 2f / PPM, getRegionHeight() / 2f / PPM);

        body.createFixture(polygonShape, 1f);
        body.setActive(false);

        polygonShape.dispose();
    }

    public void update(float dt) {
        setPosition(body.getPosition().x - halfWidth, body.getPosition().y - halfHeight);

        body.setTransform(body.getPosition().x + speed, body.getPosition().y, 0);

        if(body.getPosition().x <= zeroPointX) {
            body.setTransform((V_WIDTH + V_WIDTH / 2f) / PPM,
                    body.getPosition().y, 0);
        }
    }
}
