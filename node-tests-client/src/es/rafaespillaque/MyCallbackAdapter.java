package es.rafaespillaque;

import org.json.JSONObject;


public abstract class MyCallbackAdapter {
	public abstract void on(String event, JSONObject json);
}