package linda.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import linda.Linda.eventMode;
import linda.Linda.eventTiming;
import linda.Tuple;
import linda.shm.CentralizedLinda;

public class LindaRemote extends UnicastRemoteObject implements LindaObject {

	/**
	 * Auto-generated serial version UID
	 */
	private static final long serialVersionUID = 3136775299313441896L;
	
	public CentralizedLinda centralizedLinda;
	
	public LindaRemote() throws RemoteException {
		super();
		centralizedLinda = new CentralizedLinda();
	}

	@Override
	public void write(Tuple t) {
		centralizedLinda.write(t);
	}

	@Override
	public Tuple take(Tuple template) {
		Tuple t = new Tuple();
		t = centralizedLinda.take(template);
		return t;
	}

	@Override
	public Tuple read(Tuple template) {
		Tuple t = new Tuple();
		t = centralizedLinda.read(template);
		return t;
	}

	@Override
	public Tuple tryTake(Tuple template) {
		Tuple t = new Tuple();
		t = centralizedLinda.tryTake(template);
		return t;
	}

	@Override
	public Tuple tryRead(Tuple template) {
		Tuple t = new Tuple();
		t = centralizedLinda.tryRead(template);
		return t;
	}

	@Override
	public Collection<Tuple> takeAll(Tuple template) {
		ArrayList<Tuple> tuples = new ArrayList<Tuple>();
		tuples = (ArrayList<Tuple>) centralizedLinda.takeAll(template);
		return tuples;
	}

	@Override
	public Collection<Tuple> readAll(Tuple template) {
		ArrayList<Tuple> tuples = new ArrayList<Tuple>();
		tuples = (ArrayList<Tuple>) centralizedLinda.readAll(template);
		return tuples;
	}

	@Override
	public void eventRegister(eventMode mode, eventTiming timing, Tuple template, RemoteCallback callback) {
		System.out.println("LindaRemote eventRegister: start");
		centralizedLinda.eventRegister(mode, timing, template, new ServerCallback(callback));
		System.out.println("LiindaRemote eventRegister: end");
	}

	@Override
	public void debug(String prefix) {
		centralizedLinda.debug(prefix);
	}

	@Override
	public ArrayList<Tuple> getTuples() {
		return centralizedLinda.getTuples();
	}

}
