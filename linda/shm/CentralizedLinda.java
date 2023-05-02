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

    // create tuple space server
    //private TupleSpace TSServer;

    // create host and port
    //private String host = "localhost";
    //private int port = 4000;

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

     /** Registers a callback which will be called when a tuple matching the template appears.
     * If the mode is Take, the found tuple is removed from the tuplespace.
     * The callback is fired once. It may re-register itself if necessary.
     * If timing is immediate, the callback may immediately fire if a matching tuple is already present; if timing is future, current tuples are ignored.
     * Beware: a callback should never block as the calling context may be the one of the writer (see also {@link AsynchronousCallback} class).
     * Callbacks are not ordered: if more than one may be fired, the chosen one is arbitrary.
     * Beware of loop with a READ/IMMEDIATE re-registering callback !
     *
     * @param mode read or take mode.
     * @param timing (potentially) immediate or only future firing.
     * @param template the filtering template.
     * @param callback the callback to call if a matching tuple appears.
     */
    public void eventRegister(eventMode mode, eventTiming timing, Tuple template, Callback callback) {
        // verify if a tuple matching the template appears
        while (true) {
            if (timing == eventTiming.IMMEDIATE) {
                // if mode is future, verify if a tuple matching the template appears
                for (Tuple tuple : tuples) {
                    if (tuple.matches(template)) {
                        // if mode is take, remove tuple from tuples list
                        if (mode == eventMode.TAKE) {   
                            tuples.remove(tuple);
                        }
                        // call the callback
                        callback.call(tuple);
                    }
                }
            }
            else {
                // if mode is future, wait for a write operation
                try {
                    wait();
                } catch (InterruptedException e) {
                    // handle the exception
                }
            }
        }
    }

    @Override
    public void debug(String prefix) {
        for (Tuple tuple : tuples ) {
            System.out.println(prefix + tuple.toString());
        }
    }
}
