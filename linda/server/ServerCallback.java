package linda.server;

import java.rmi.RemoteException;

import linda.Callback;
import linda.Tuple;

public class ServerCallback implements Callback {
	private RemoteCallback callback;
	
	public ServerCallback(RemoteCallback cb) {
		super();
		//System.out.println("ServerCallback begin");
		this.callback = cb;
		//System.out.println("ServerCallback end");
	}
		
	@Override
	public void call(Tuple t) {
		//System.out.println("ServerCallback call");
		try {
			callback.call(t);
		} catch(RemoteException e) {
			e.printStackTrace();
		}
		//System.out.println("ServerCallback call end");
	}

}
