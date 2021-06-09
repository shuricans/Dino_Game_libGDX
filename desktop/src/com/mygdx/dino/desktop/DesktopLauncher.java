package com.mygdx.dino.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.dino.Dino;

import static com.mygdx.dino.Utils.Constants.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = TITLE;
		config.width = WIDTH_DESKTOP;
		config.height = HEIGHT_DESKTOP;
		config.forceExit = false;
		new LwjglApplication(new Dino(), config);
	}
}
