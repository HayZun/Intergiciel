package linda.shm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import linda.Callback;
import linda.Linda;
import linda.Tuple;



/** Shared memory implementation of Linda. */
public class CentralizedLinda implements Linda {

    //create Hasmap Template and callback 
    private HashMap<Tuple, Callback> templateCallbackTake;
    private HashMap<Tuple, Callback> templateCallbackRead;


    // create list of tuples
    private ArrayList<Tuple> tuples;

    public CentralizedLinda() {
        //init tuples list
        tuples = new ArrayList<Tuple>();
        //init templateCallbackTake
        templateCallbackTake = new HashMap<Tuple, Callback>();
        //init templateCallbackRead
        templateCallbackRead = new HashMap<Tuple, Callback>();
    }

    @Override
    public synchronized void write(Tuple t) {
        tuples.add(t);
        // create a copy of the tuples list
        List<Tuple> tuplesCopy = new ArrayList<>(tuples);
        // for each tuple in the copy
        tuplesCopy.forEach(tuple -> {
            boolean take = false;
            // foreach tuples, if matches template and template is a take template remove tuple from tuples list
            for (Tuple template : templateCallbackTake.keySet()) {
                if (tuple.matches(template) && !take) {
                    tuples.removeIf(t1->t1.matches(template));
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
            return take(template);
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
            return read(template);
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
    public synchronized void eventRegister(eventMode mode, eventTiming timing, Tuple template, Callback callback) {
        //sleep five seconds

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
    public synchronized void debug(String prefix) {
        System.out.println(prefix + " " + tuples.toString());

    }
}
