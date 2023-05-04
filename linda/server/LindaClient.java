package linda.server;

import linda.Callback;
import linda.Linda;
import linda.Linda.eventMode;
import linda.Linda.eventTiming;
import linda.Tuple;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Collection;

public class LindaClient implements Linda {

    private final LindaServer server;

    public LindaClient(String serverURL) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(serverURL);
        server = (LindaServer) registry.lookup("LindaServer");
    }

    @Override
    public void write(Tuple t) {
        server.write(t);
    }

    @Override
    public Tuple take(Tuple template) {
       return server.take(template);
    }

    @Override
    public Tuple read(Tuple template) {
        return server.read(template);
    }

    @Override
    public Tuple tryTake(Tuple template) {
        return server.tryTake(template);
    }

    @Override
    public Tuple tryRead(Tuple template) {
        return server.tryRead(template);
    }

    @Override
    public void eventRegister(eventMode mode, eventTiming timing, Tuple template, Callback callback) {
        server.eventRegister(mode, timing, template, callback);
    }

    @Override
    public void debug(String prefix) {
        server.debug(prefix);
    }

    @Override
    public Collection<Tuple> takeAll(Tuple template) {
        return server.takeAll(template);
    }

    @Override
    public Collection<Tuple> readAll(Tuple template) {
        return server.readAll(template);
    }

}
