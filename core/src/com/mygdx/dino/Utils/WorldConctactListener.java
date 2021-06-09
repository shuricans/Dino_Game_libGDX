package com.mygdx.dino.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.dino.Screens.PlayScreen;


public class WorldConctactListener implements ContactListener {

    private PlayScreen screen;

    public WorldConctactListener(PlayScreen screen) {
        this.screen = screen;
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() == "trex" || fixB.getUserData() == "trex"){
            Fixture head = fixA.getUserData() == "trex" ? fixA : fixB;
            Fixture object = head == fixA ? fixB : fixA;

            if(object.getUserData() != null && object.getUserData().equals("bird")){
                screen.gameOver = true;
                Gdx.app.log("hit bird", "");
            }

        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
