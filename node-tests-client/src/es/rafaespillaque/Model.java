package es.rafaespillaque;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Model {
	public static final float VELOCITY = 120f;
	public Vector2 pos = new Vector2(0, 0);
	public Vector2 vel = new Vector2(0, 0);
	public Vector2 dir = new Vector2(0, 0);
	private float stateTime = 0f;
	private boolean local;
	
	public Model() {
	}
	
	public Model(boolean local) {
		this.local = local;
	}
	
	public void update(float dt) {
		stateTime += dt;
		pos.x += dir.x * dt * VELOCITY;
		pos.y += dir.y * dt * VELOCITY;
	}
	
	public void render(SpriteBatch batcher) {
		if(local)
			batcher.draw(Assets.blueCircle, pos.x, pos.y);
		else
			batcher.draw(Assets.redCircle, pos.x, pos.y);
	}
}
