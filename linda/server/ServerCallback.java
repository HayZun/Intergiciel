package linda.server;

import java.rmi.RemoteException;

import linda.Callback;
import linda.Tuple;

public class ServerCallback implements Callback {
	private RemoteCallback callback;
	
	public ServerCallback(RemoteCallback cb) {
		super();
		this.callback = cb;
	}
		
	@Override
	public void call(Tuple t) {
		try {
			callback.call(t);
		} catch(RemoteException e) {
			e.printStackTrace();
		}
		
	}

}
