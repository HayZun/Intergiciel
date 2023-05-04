package linda.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import linda.Callback;
import linda.Linda;
import linda.Linda.eventMode;
import linda.Linda.eventTiming;
import linda.shm.CentralizedLinda;
import linda.Tuple;

public class CentralizedLindaServer extends UnicastRemoteObject implements LindaService {

    private Linda linda;
    private ArrayList<Tuple> tuples;
    private HashMap<Tuple, Callback> templateCallbackTake;
    private HashMap<Tuple, Callback> templateCallbackRead;

    public CentralizedLindaServer() throws RemoteException {
        super();
        tuples = new ArrayList<Tuple>();
        templateCallbackTake = new HashMap<Tuple, Callback>();
        templateCallbackRead = new HashMap<Tuple, Callback>();
    }

    @Override
    public synchronized void write(Tuple t) throws RemoteException {
        tuples.add(t);
        // create a copy of the tuples list
        List<Tuple> tuplesCopy = new ArrayList<>(tuples);
        // for each tuple in the copy
        tuplesCopy.forEach(tuple -> {
            boolean take = false;
            // foreach tuples, if matches template and template is a take template remove tuple from tuples list
            for (Tuple template : templateCallbackTake.keySet()) {
                if (tuple.matches(template) && !take) {
                    tuples.remove(tuple);
                    templateCallbackTake.get(template).call(tuple);
                    templateCallbackTake.remove(template);
                    take = true;
                }
            }
            for (Tuple template : templateCallbackRead.keySet()) {
                if (tuple.matches(template)) {
                    templateCallbackRead.get(template).call(tuple);
                    templateCallbackRead.remove(template);
                }
            }
        });
        notifyAll();
    }

    @Override
    public synchronized Tuple take(Tuple template) throws RemoteException {
        // search template in tuples list
        while (true) {
            for (Tuple tuple : tuples) {
                if (tuple.matches(template)) {
                    // remove tuple from tuples list
                    tuples.remove(tuple);
                    // return the tuple
                    return tuple;
                }
            }
            // if no tuple found, wait for a write operation
            try {
                wait();
            } catch (InterruptedException e) {
                // handle the exception
            }
            return take(template);
        }
    }

    @Override
    public synchronized Tuple read(Tuple template) throws RemoteException {
        // search template in tuples list
        while (true) {
            for (Tuple tuple : tuples) {
                if (tuple.matches(template)) {
                    // return the tuple
                    return tuple;
                }
            }
            // if no tuple found, wait for a write operation
            try {
                wait();
            } catch (InterruptedException e) {
                // handle the exception

            }
            return read(template);
        }
    }

    @Override
    public synchronized Tuple tryTake(Tuple template) throws RemoteException {
        for (Tuple tuple : tuples) {
            if (tuple.matches(template)) {
                //remove tuple from tuples list
                tuples.remove(tuple);
                return tuple;
            }
        }

        // if template not found, return null
        return null;
    }

    @Override
    public synchronized Tuple tryRead(Tuple template) throws RemoteException {
        //research template in tuples list
        for (Tuple tuple : tuples) {
            if (tuple.matches(template)) {
                return tuple;
            }
        }

        // if template not found, return null
        return null;
    }

    @Override
    public synchronized Collection<Tuple> takeAll(Tuple template) throws RemoteException {
        // create list of tuples to remove
        Collection<Tuple> tuplesToRemove = new ArrayList<Tuple>();

        //research template in tuples list
        for (Tuple tuple : tuples) {
            if (tuple.matches(template)) {
                tuplesToRemove.add(tuple);
            }
        }

        //remove tuples from tuples list
        tuples.removeAll(tuplesToRemove);

        return tuplesToRemove;
    }

    @Override
    public synchronized Collection<Tuple> readAll(Tuple template) throws RemoteException {
        // create list of tuples to return
        Collection<Tuple> tuplesToReturn = new ArrayList<Tuple>();

        //research template in tuples list
        for (Tuple tuple : tuples) {
            if (tuple.matches(template)) {
                tuplesToReturn.add(tuple);
            }
        }

        return tuplesToReturn;
    }

    @Override
    public synchronized void eventRegister(eventMode mode, eventTiming timing, Tuple template, Callback callback)
            throws RemoteException {
                if (timing == eventTiming.IMMEDIATE) {
                    Tuple motifTuple = null;
                        if (mode == eventMode.TAKE) {
                            motifTuple = tryTake(template);
                            if (motifTuple != null) {
                                callback.call(motifTuple);
                            } else {
                                templateCallbackTake.put(template, callback);
                            }
                        } else {
                            motifTuple = tryRead(template);
                            if (motifTuple != null) {
                                callback.call(motifTuple);
                            } else {
                                templateCallbackRead.put(template, callback);
                            }
                        }                               
                } else {
                    // if timing is future, current tuples are ignored.
                    if (mode == eventMode.TAKE) {
                        templateCallbackTake.put(template, callback);
                    } else {
                        templateCallbackRead.put(template, callback);
                    }
                }
    }

    @Override
    public void debug(String prefix) throws RemoteException {
        linda.debug(prefix);
    }
}

