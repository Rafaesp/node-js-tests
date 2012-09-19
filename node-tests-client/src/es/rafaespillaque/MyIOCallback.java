package es.rafaespillaque;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIOException;

import org.json.JSONObject;

public class MyIOCallback implements IOCallback{
	
	private MyCallbackAdapter callback;
	
	public MyIOCallback(MyCallbackAdapter callback){
		this.callback = callback;
	}
	
	@Override
	public void onMessage(JSONObject json, IOAcknowledge ack) {
	}

	@Override
	public void onMessage(String data, IOAcknowledge ack) {
	}

	@Override
	public void onError(SocketIOException socketIOException) {
		System.out.println("an Error occured");
		socketIOException.printStackTrace();
	}

	@Override
	public void onDisconnect() {
		System.out.println("Connection terminated.");
	}

	@Override
	public void onConnect() {
	}

	public void on(String event, IOAcknowledge ack, Object... args) {
		callback.on(event, (JSONObject)args[0]);
	}

}
