package linda.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;

import linda.Callback;
import linda.Linda.eventMode;
import linda.Linda.eventTiming;
import linda.Tuple;
import linda.shm.CentralizedLinda;

public class RemoteCentralizedLindaImpl extends UnicastRemoteObject implements RemoteCentralizedLinda {
    private CentralizedLinda linda;

    public RemoteCentralizedLindaImpl() throws RemoteException {
        super();
        linda = new CentralizedLinda();
    }

    @Override
    public void write(Tuple t) throws RemoteException {
        linda.write(t);
    }

    @Override
    public void debug(String prefix) throws RemoteException {
        linda.debug(prefix);
    }

    @Override
    public Tuple take(Tuple template) throws RemoteException {
        return linda.take(template);
    }

    @Override
    public Tuple read(Tuple template) throws RemoteException {
        return linda.read(template);
    }

    @Override
    public Tuple tryTake(Tuple template) throws RemoteException {
        return linda.tryTake(template);
    }

    @Override
    public Tuple tryRead(Tuple template) throws RemoteException {
        return linda.tryRead(template);
    }

    @Override
    public Collection<Tuple> takeAll(Tuple template) throws RemoteException {
        return linda.takeAll(template);
    }

    @Override
    public Collection<Tuple> readAll(Tuple template) throws RemoteException {
        return linda.readAll(template);
    }

    @Override
    public ArrayList<Tuple> getTuples() throws RemoteException {
        return linda.getTuples();
    }

    @Override
    public void eventRegister(eventMode mode, eventTiming timing, Tuple template, Callback callback) {
        //convert callback to seriaziable
        System.out.println("RemoteCentralizedLindaImpl: eventRegister");
        linda.eventRegister(mode, timing, template, callback);
    }
}
