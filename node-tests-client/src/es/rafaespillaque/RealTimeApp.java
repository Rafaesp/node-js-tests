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
	private HashMap<Integer, Vector2> users;
	private Vector2 user;
	private int id;
	private MyCallbackAdapter callback;
	private Vector2 pointer;

	@Override
	public void create() {
		batcher = new SpriteBatch();
		users = new HashMap<Integer, Vector2>();
		user = new Vector2();
		user.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
		pointer = new Vector2();
		
		Assets.load();

		initSocketIO();

	}

	@Override
	public void render() {
		update();
		present();
	}

	private void update() {
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT) ||
		   Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||
		   Gdx.input.isKeyPressed(Input.Keys.UP) ||
		   Gdx.input.isKeyPressed(Input.Keys.DOWN) || 
		   Gdx.input.isTouched()){
			
			pointer.set(Gdx.input.getX(), Gdx.input.getY());
			
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
				user.x -= 2f;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
				user.x += 2f;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
				user.y += 2f;
			}
			if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
				user.y -= 2f;
			}
			if(pointer.x > 3*Gdx.graphics.getWidth()/4){
				user.x += 2f;
			}if(pointer.x < Gdx.graphics.getWidth()/4){
				user.x -= 2f;
			}if(pointer.y > 3*Gdx.graphics.getHeight()/4){
				user.y -= 2f;
			}if(pointer.y < Gdx.graphics.getHeight()/4){
				user.y += 2f;
			}
			
			
			sendPos();
		}
		
	}

	private void present() {
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClearColor(1, 1, 1, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		batcher.begin();
		batcher.draw(Assets.redCircle, user.x, user.y);
		for(Vector2 user : users.values()){
			batcher.draw(Assets.blueCircle, user.x, user.y);
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
		try {
			String json = "{'x':" + user.x + " ,'y':" + user.y + "}";

			socket.emit("newPos", new JSONObject(json));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void updatePos(int newPosId, JSONObject jsonObject) {
		try {
			float x = Float.parseFloat(jsonObject.getString("x"));
			float y = Float.parseFloat(jsonObject.getString("y"));
			users.put(newPosId, new Vector2(x, y));
		
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
