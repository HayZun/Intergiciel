package linda.shm;

import java.util.Collection;

import linda.Callback;
import linda.Linda;
import linda.Tuple;

/** Shared memory implementation of Linda. */
public class CentralizedLinda implements Linda {

    
    public CentralizedLinda() {
        private Tuple motifStringInteger = new Tuple(String.class, Integer.class);
        //TSServer.eventRegister(TupleSpace.WRITE, templateDRAW, new WhiteboardModel.EventDraw());
        private Tuple motifIntegerString = new Tuple(Integer.class, String.class);
        TSServer = new TupleSpace("Whiteboard", host, port);

    }

    public void write(Tuple t) {
        // TO BE COMPLETED
    }

    public Tuple take(Tuple template) {
        // TO BE COMPLETED
        return null;
    }

    public Tuple read(Tuple template) {
        // TO BE COMPLETED
        return null;
    }

    public Tuple tryTake(Tuple template) {
        // TO BE COMPLETED
        return null;
    }

    public Tuple tryRead(Tuple template) {
        // TO BE COMPLETED
        return null;
    }

    public Tuple waitAndTake(Tuple template) {
        // TO BE COMPLETED
        return null;
    }

    public Tuple waitAndRead(Tuple template) {
        // TO BE COMPLETED
        return null;
    }

    public void eventRegister(eventMode mode, eventTiming timing, Tuple template, Callback callback) {
        // TO BE COMPLETED
    }

    public void debug(String prefix) {
        // TO BE COMPLETED
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

}
