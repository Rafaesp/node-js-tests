package es.rafaespillaque;

import com.badlogic.gdx.math.Vector2;

public class RK4 {
	private static Derivative aux = new Derivative();
	
	private static Derivative a = new Derivative();
	private static Derivative b = new Derivative();
	private static Derivative c = new Derivative();
	private static Derivative d = new Derivative();
	
	private static void acceleration(Model m, float t) {
//		aux.dVel.set(Model.VELOCITY * t * m.dir.x, Model.VELOCITY * t * m.dir.y);
		aux.dVel.set(0.1f*t, 0.1f*t);
	}
	
	private static Derivative evaluate(final Model m, float t, float dt, Derivative d) {
		aux.dPos.set(m.pos.x +  d.dPos.x * dt, m.pos.y +  d.dPos.y * dt);
		acceleration(m, t + dt);
		
		return aux;
	}
	
	public static void integrate(Model m, float t, float dt) {
		aux.clear();
		a.cpy(evaluate(m, t, 0f, aux));
		b.cpy(evaluate(m, t, dt*0.5f, a));
		c.cpy(evaluate(m, t, dt*0.5f, b));
		d.cpy(evaluate(m, t, dt*0.5f, c));
		
		float dPosxdt = 1f/6f * (a.dPos.x + 2f*(b.dPos.x + c.dPos.x) +d.dPos.x);
		float dPosydt = 1f/6f * (a.dPos.y + 2f*(b.dPos.y + c.dPos.y) +d.dPos.x);
		float dVelxdt = 1f/6f * (a.dVel.x + 2f*(b.dVel.x + c.dVel.x) +d.dPos.x);
		float dVelydt = 1f/6f * (a.dVel.y + 2f*(b.dVel.y + c.dVel.y) +d.dPos.y);
		
		m.pos.set(m.pos.x + dPosxdt * dt, m.pos.y + dPosydt * dt);
		m.vel.set(m.vel.x + dVelxdt * dt, m.vel.y + dVelydt * dt);
	}
	
	private static class Derivative{
		public Vector2 dPos  = new Vector2(0,0);
		public Vector2 dVel  = new Vector2(0,0);
		
		public void clear() {
			dPos.set(0, 0);
			dVel.set(0, 0);
		}
		
		public void cpy(Derivative d) {
			dPos.set(d.dPos);
			dVel.set(d.dVel);
		}
	}
}
