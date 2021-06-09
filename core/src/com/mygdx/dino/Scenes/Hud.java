package com.mygdx.dino.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.mygdx.dino.Utils.Constants.V_HEIGHT;
import static com.mygdx.dino.Utils.Constants.V_WIDTH;

public class Hud implements Disposable {

    public Stage stage;
    private final Viewport viewport;

    public int score;
    public int hiScore;
    private float timeCount;

    Label scoreLabel;
    Label hiScoreLabel;

    public Hud(SpriteBatch spriteBatch) {

        score = 0;
        hiScore = 0;

        viewport = new FitViewport(V_WIDTH, V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, spriteBatch);

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false);

        scoreLabel = new Label(String.format("%05d", score),
                new Label.LabelStyle(new BitmapFont(), Color.BLACK));
        hiScoreLabel = new Label(String.format("HI %05d", hiScore),
                new Label.LabelStyle(new BitmapFont(), Color.GRAY));

        table.padTop(10);
        table.add().expand().top(); // empty cell
        table.add(hiScoreLabel).top().padRight(10).right();
        table.add(scoreLabel).top().padRight(10).right();

        stage.addActor(table);
    }


    public void update(float dt) {
        timeCount += 1;
        if(timeCount >= 4) {
            score += 1;
            scoreLabel.setText(String.format("%05d", score));
            timeCount = 0;
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
