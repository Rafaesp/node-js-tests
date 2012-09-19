package es.rafaespillaque;

import com.badlogic.gdx.Gdx;

public class Log {
	
	public static void w(String msg){
		Gdx.app.log("TAG", msg);
	}
	
	public static void w(String format, Object...args){
		Gdx.app.log("TAG", String.format(format, args));
	}

}
