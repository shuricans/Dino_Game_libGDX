package com.mygdx.dino.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygdx.dino.Screens.PlayScreen;

import static com.mygdx.dino.Utils.Constants.*;

public class TRex extends Sprite {

    public enum State {DEAD, JUMP, STAND, RUN, LOW_RUN};
    public State currentState;
    public State previousState;

    public World world;
    public Body body;
    public Body bodyLow;

    protected Fixture fixture;

    private float stateTimer;
    private TextureRegion tRexStand;
    private TextureRegion tRexJump;
    private TextureRegion tRexDead;
    private Animation<TextureRegion> tRexRun;
    private Animation<TextureRegion> tRexLowRun;

    private int regionHeight, regionWidth;

    public boolean lowRunning = false;

    public TRex(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion(REGION_NAME_TREX));

        this.world = world;

        currentState = State.STAND;
        previousState = State.STAND;
        stateTimer = 0;

        defineTextureTRex();
        defineBodyTRex();
    }


    private void defineTextureTRex() {

        regionHeight = regionWidth = getRegionHeight(); // 59 x 59

        Array<TextureRegion> frames = new Array<>();

        for(int i = 1; i < 3; i++) {
            frames.add(new TextureRegion(getTexture(), i * regionWidth + getRegionX(), getRegionY(), regionWidth, regionHeight));
        }
        tRexRun = new Animation<>(0.1f, frames);
        frames.clear();

        for(int i = 3; i < 5; i++) {
            frames.add(new TextureRegion(getTexture(), i * regionWidth + getRegionX(), getRegionY(), regionWidth, regionHeight));
        }
        tRexLowRun = new Animation<>(0.1f, frames);
        frames.clear();

        tRexStand = new TextureRegion(getTexture(), getRegionX(), getRegionY(), regionWidth, regionHeight);
        setBounds(0, 0, regionWidth / PPM, regionHeight / PPM);
        setRegion(tRexStand);

        tRexDead = new TextureRegion(getTexture(), 5 * getRegionWidth() + getRegionX(), getRegionY(), regionWidth, regionHeight);
        tRexJump = tRexStand; // means jump equals stand
    }

    private void defineBodyTRex() {

        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(V_WIDTH / 10f / PPM, V_HEIGHT / 2f / PPM);
        body = world.createBody(bDef);

        BodyDef bDefLow = new BodyDef();
        bDefLow.type = BodyDef.BodyType.DynamicBody;
        bDefLow.position.set(V_WIDTH / 10f / PPM, V_HEIGHT / 4f / PPM);
        bodyLow = world.createBody(bDefLow);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(regionWidth / 2f / PPM);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(regionWidth / 2f / PPM, regionHeight / 4f / PPM);

        fixture = body.createFixture(circleShape, 1f);
        fixture.setUserData("trex");

        bodyLow.setActive(false);
        bodyLow.createFixture(polygonShape, 1f);

        circleShape.dispose();
        polygonShape.dispose();
    }

    public void update(float dt) {

        body.setActive(!lowRunning);
        bodyLow.setActive(lowRunning);

        setPosition(body.getPosition().x - getWidth() / 2,
                body.getPosition().y - getHeight() / 2);

        setRegion(getFrame(dt));

    }

    private TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch(currentState){
            case STAND:
                region = tRexStand;
                break;
            case JUMP:
                region = tRexJump;
                break;
            case LOW_RUN:
                region = tRexLowRun.getKeyFrame(stateTimer, true);
                break;
            case DEAD:
                region = tRexDead;
                break;
            default: // run
                region = tRexRun.getKeyFrame(stateTimer, true);
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    private State getState() {
        if(body.getLinearVelocity().y > 0 || (body.getLinearVelocity().y < 0 && previousState == State.JUMP)) {
            return State.JUMP;
        } else if(lowRunning) {
            return State.LOW_RUN;
        }

        return State.RUN;
    }

}
