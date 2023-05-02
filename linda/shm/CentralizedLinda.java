package linda.shm;

import java.util.ArrayList;
import java.util.Collection;

import linda.Callback;
import linda.Linda;
import linda.Tuple;



/** Shared memory implementation of Linda. */
public class CentralizedLinda implements Linda {

    // create list of tuples
    private ArrayList<Tuple> tuples;

    public CentralizedLinda() {
        //private Tuple motifStringInteger = new Tuple(String.class, Integer.class);
        //TSServer.eventRegister(TupleSpace.WRITE, templateDRAW, new WhiteboardModel.EventDraw());
        //private Tuple motifIntegerString = new Tuple(Integer.class, String.class);
        //TSServer = new TupleSpace("Whiteboard", host, port);
        
        //init tuples list
        tuples = new ArrayList<Tuple>();
    }

    @Override
    public synchronized void write(Tuple t) {
        // TO BE COMPLETED
        tuples.add(t);

        // notify all threads waiting for a tuple
        notifyAll();
    }

    public synchronized Tuple take(Tuple template) {
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
        }
    }
    

    @Override
    public synchronized Tuple read(Tuple template) {
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
        }
    }
    @Override
    public Tuple tryTake(Tuple template) {
        //research template in tuples list
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
    public Tuple tryRead(Tuple template) {
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
    public Collection<Tuple> takeAll(Tuple template) {

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
    public Collection<Tuple> readAll(Tuple template) {

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
    public void eventRegister(eventMode mode, eventTiming timing, Tuple template, Callback callback) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eventRegister'");
    }

    @Override
    public void debug(String prefix) {
        System.out.println(prefix + ": " + tuples.toString());
    }
}
