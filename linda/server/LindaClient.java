package linda.server;

import java.rmi.Naming;
import java.util.Collection;

import linda.Callback;
import linda.Linda;
import linda.Tuple;

/** Client part of a client/server implementation of Linda.
 * It implements the Linda interface and propagates everything to the server it is connected to.
 * */
public class LindaClient implements Linda {
	
    /** Initializes the Linda implementation.
     *  @param serverURI the URI of the server, e.g. "rmi://localhost:4000/LindaServer" or "//localhost:4000/LindaServer".
     */
    public LindaClient(String serverURI) {
        LindaService linda = (LindaService) Naming.lookup(serverURI);
    }

    @Override
    public void write(Tuple t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'write'");
    }

    @Override
    public Tuple take(Tuple template) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'take'");
    }

    @Override
    public Tuple read(Tuple template) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'read'");
    }

    @Override
    public Tuple tryTake(Tuple template) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tryTake'");
    }

    @Override
    public Tuple tryRead(Tuple template) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tryRead'");
    }

    @Override
    public Collection<Tuple> takeAll(Tuple template) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'takeAll'");
    }

    @Override
    public Collection<Tuple> readAll(Tuple template) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readAll'");
    }

    @Override
    public void eventRegister(eventMode mode, eventTiming timing, Tuple template, Callback callback) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eventRegister'");
    }

    @Override
    public void debug(String prefix) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'debug'");
    }
    
    // TO BE COMPLETED

}
