package linda.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import linda.Callback;
import linda.Tuple;

public class RemoteCallbackImpl extends UnicastRemoteObject implements RemoteCallback{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6327294100662954220L;
	private Callback cb;
	
	public RemoteCallbackImpl(Callback callback) throws RemoteException {
		super();
		this.cb = callback;
	}


	@Override
	public void call(Tuple t) {
		System.out.println("RemoteCallbackImpl call");
		cb.call(t);
		System.out.println("RemoteCallbackImpl call end");
    }
}
