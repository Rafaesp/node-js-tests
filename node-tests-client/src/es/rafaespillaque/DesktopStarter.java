package es.rafaespillaque;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class DesktopStarter {
        public static void main (String[] args) {
                try {
					new LwjglApplication(new RealTimeApp(), "RealTime", 960, 480, false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }
}