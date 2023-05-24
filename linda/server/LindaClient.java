package linda.server;

import java.util.ArrayList;
import java.util.Collection;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import linda.Callback;
import linda.Linda;
import linda.Tuple;

/** Client part of a client/server implementation of Linda.
 * It implements the Linda interface and propagates everything to the server it is connected to.
 * */
public class LindaClient implements Linda {
	
	private static LindaObject linda;
	
    /** Initializes the Linda implementation.
     *  @param serverURI the URI of the server, e.g. "rmi://localhost:4000/LindaServer" or "//localhost:4000/LindaServer".
     */
    public LindaClient(String serverURI) {
    	
    	Registry registry = null;
    	
    	// getRegistry with URI != localhost
    	try {
			registry = LocateRegistry.getRegistry(getHost(serverURI), Integer.parseInt(getPort(serverURI)));
		} catch (NumberFormatException | RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    
		try {
			linda = (LindaObject) registry.lookup("LindaServer");
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
    }

	@Override
	public void write(Tuple t) {
		try {
			linda.write(t);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Tuple take(Tuple template) {
		Tuple t = new Tuple();
		try {
			t = linda.take(template);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return t;
	}

	@Override
	public Tuple read(Tuple template) {
		Tuple t = new Tuple();
		try {
			t = linda.read(template);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return t;
	}

	@Override
	public Tuple tryTake(Tuple template) {
		Tuple t = new Tuple();
		try {
			t = linda.tryTake(template);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return t;
	}

	@Override
	public Tuple tryRead(Tuple template) {
		Tuple t = new Tuple();
		try {
			t = linda.tryRead(template);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return t;
	}

	@Override
	public Collection<Tuple> takeAll(Tuple template) {
		ArrayList<Tuple> tuples = new ArrayList<Tuple>();		
		try {
			tuples = (ArrayList<Tuple>) linda.takeAll(template);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return tuples;
	}
//  create LindaObject,

	@Override
	public Collection<Tuple> readAll(Tuple template) {
		ArrayList<Tuple> tuples = new ArrayList<Tuple>();		
		try {
			tuples = (ArrayList<Tuple>) linda.readAll(template);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return tuples;
	}

	@Override
	public void eventRegister(eventMode mode, eventTiming timing, Tuple template, Callback callback) {
		try {
			RemoteCallback cb = new RemoteCallbackImpl(callback);
			linda.eventRegister(mode, timing, template, cb);
			System.out.println("Event registered");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void debug(String prefix) {
		try {
			System.out.println(prefix + " " + linda.getTuples().toString());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public String getHost(String uri) {
		String host;
    	if (uri.startsWith("rmi")) {
			String[] parts = uri.split(":");
			parts = parts[1].split(":");
			host = parts[0].replace("//", "");
		} else {
			String[] parts = uri.split(":");
			host = parts[0].replace("//", "");
		}
    	return host;
    	
    }
    
    public String getPort(String uri) {
		String port;
		if (uri.startsWith("rmi")) {
			String[] parts = uri.split(":");
			parts = parts[1].split(":");
			parts = parts[1].split("/");
			port = parts[0];
		} else {
			String[] parts = uri.split(":");
			parts = parts[1].split("/");
			port = parts[0];
		}
    	return port;
    	
    }
}
