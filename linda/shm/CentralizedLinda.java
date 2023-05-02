package linda.shm;

import java.util.Collection;

import linda.Callback;
import linda.Linda;
import linda.Tuple;

/** Shared memory implementation of Linda. */
public class CentralizedLinda implements Linda {

    private Tuple motifStringInteger = new Tuple(String.class, Integer.class);
    private Tuple motifIntegerString = new Tuple(Integer.class, String.class);
	
    public CentralizedLinda() {

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
