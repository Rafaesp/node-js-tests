package es.rafaespillaque.android;

import com.badlogic.gdx.backends.android.AndroidApplication;

import es.rafaespillaque.RealTimeApp;

public class AndroidStarter extends AndroidApplication {
	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initialize(new RealTimeApp(), false);
	}
}
