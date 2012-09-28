package es.rafaespillaque;

/*
 * socket.io-java-client Test.java
 *
 * Copyright (c) 2012, Enno Boland
 * socket.io-java-client is a implementation of the socket.io protocol in Java.
 * 
 * See LICENSE file for more information
 */
import io.socket.SocketIO;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class RealTimeApp implements ApplicationListener {
	private SocketIO socket;
	private SpriteBatch batcher;
	private HashMap<Integer, Model> users;
	private Model user;
	private int id = -1;
	private MyCallbackAdapter callback;
	private Vector2 pointer;
	private final float dt = 0.01f;
	private float accumulator = 0f;
	
	private int count = 0;
	
	@Override
	public void create() {
		batcher = new SpriteBatch();
		users = new HashMap<Integer, Model>();
		user = new Model(true);
		user.pos.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		pointer = new Vector2();
		
		Assets.load();

		initSocketIO();

	}

	@Override
	public void render() {
		float frameTime = Gdx.graphics.getDeltaTime();
		accumulator += frameTime;
		update(frameTime);
		present();
	}

	private void update(float frameTime) {
		user.dir.set(0f, 0f);
		if(count < 30)
			user.dir.x = 1;
		
		if(count < 40)
			System.out.println(user.pos.x);
		count++;
//		boolean keys = false;
//		boolean touched = checkScreen();
//		if (!touched) {
//			keys = checkKeys();
//		}
//		if (touched || keys) {
//			sendPos();
//		}
		while(accumulator >= dt) {
			user.update(dt);
//			for(Model user : users.values()){
//				user.update(dt);
//			}
			accumulator -= dt;
		}
		
	}
	
	private boolean checkScreen() {
		if (Gdx.input.isTouched()) {

			pointer.set(Gdx.input.getX(), Gdx.input.getY());

			if (pointer.x > 3 * Gdx.graphics.getWidth() / 4) {
				user.dir.x = 1f;
			}
			if (pointer.x < Gdx.graphics.getWidth() / 4) {
				user.dir.x = -1f;
			}
			if (pointer.y > 3 * Gdx.graphics.getHeight() / 4) {
				user.dir.y = -1f;
			}
			if (pointer.y < Gdx.graphics.getHeight() / 4) {
				user.dir.y = 1f;
			}
			return true;
		}
		return false;
	}
	
	private boolean checkKeys() {
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)
				|| Gdx.input.isKeyPressed(Input.Keys.RIGHT)
				|| Gdx.input.isKeyPressed(Input.Keys.UP)
				|| Gdx.input.isKeyPressed(Input.Keys.DOWN)) {

			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				user.dir.x = -1f;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				user.dir.x = 1f;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				user.dir.y = 1f;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				user.dir.y = -1f;
			}
			return true;
		}
		return false;
	}
	

	private void present() {
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batcher.begin();
		user.render(batcher);
		for(Model user : users.values()){
			user.render(batcher);
		}
		batcher.end();
	}

	private void initSocketIO() {
		socket = new SocketIO();
		callback = new MyCallbackAdapter() {

			@Override
			public void on(String event, JSONObject json) {
				try {
					if (event.equals("id")) {
						id = json.getInt("id");
						Log.w("tengo id!!:  " + id);
					}else if(event.equals("newPos")){
						int newPosId = json.getInt("id"); 
						if(newPosId != id){
							updatePos(newPosId, json.getJSONObject("pos"));
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		};
		try {
//			URL url = new URL("http://127.0.0.1:8080");
			URL url = new URL("http://node-tests.nodester.com:80");
//			URL url = new URL("http://192.168.0.19:8080");
//			URL url = new URL("http://85.136.162.160:8080");
			socket.connect(url, new MyIOCallback(callback));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}

	private void sendPos() {
		if(id != -1) {
			try {
				String json = "{'x':" + user.pos.x + " ,'y':" + user.pos.y
						+ "}";

				socket.emit("newPos", new JSONObject(json));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void updatePos(int newPosId, JSONObject jsonObject) {
		try {
			float x = Float.parseFloat(jsonObject.getString("x"));
			float y = Float.parseFloat(jsonObject.getString("y"));
			Model m = new Model(false);
			m.pos.set(x, y);
			users.put(newPosId, m);
			
		
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void resize(int arg0, int arg1) {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

	@Override
	public void pause() {
	}
}
