package es.rafaespillaque;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Assets {
	
	public static Texture redCircle;
	public static Texture blueCircle;
	
	public static void load(){
		redCircle = new Texture(Gdx.files.internal("red_circle.png"));
		blueCircle = new Texture(Gdx.files.internal("blue_circle.png"));
	}

}
