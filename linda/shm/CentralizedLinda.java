package linda.shm;

import linda.*;
import linda.Linda.eventMode;
import linda.Linda.eventTiming;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;



/** Shared memory implementation of Linda. */
public class CentralizedLinda implements Linda {

    // HashMap de template et de callback pour Take
private HashMap<Tuple, ArrayList<Callback>> templateCallbackTake;

public HashMap<Tuple, ArrayList<Callback>> getTemplateCallbackTake() {
    return templateCallbackTake;
}

public void setTemplateCallbackTake(HashMap<Tuple, ArrayList<Callback>> templateCallbackTake) {
    this.templateCallbackTake = templateCallbackTake;
}

    // HashMap de template et de callback pour Read
    private HashMap<Tuple, ArrayList<Callback>> templateCallbackRead;

    public HashMap<Tuple, ArrayList<Callback>> getTemplateCallbackRead() {
        return templateCallbackRead;
    }

    public void setTemplateCallbackRead(HashMap<Tuple, ArrayList<Callback>> templateCallbackRead) {
        this.templateCallbackRead = templateCallbackRead;
    }

    // Liste de tuples
    private ArrayList<Tuple> tuples;

    public ArrayList<Tuple> getTuples() {
        return tuples;
    }

    public void setTuples(ArrayList<Tuple> tuples) {
        this.tuples = tuples;
    }

    public CentralizedLinda() {
        //init tuples list
        tuples = new ArrayList<Tuple>();
        //init templateCallbackTake
        templateCallbackTake = new HashMap<Tuple, ArrayList<Callback>>();
        //init listRead
        //init templateCallbackRead with listRead
        templateCallbackRead = new HashMap<Tuple, ArrayList<Callback>>();
    }

    @Override
    public void write(Tuple t) {
        synchronized (tuples) {
            tuples.add(t);
        }
        // create a copy of the tuples list
        List<Tuple> tuplesCopy = new ArrayList<>(tuples);
        // for each tuple in the copy
        tuplesCopy.forEach(tuple -> {
            boolean take = false;
            // foreach tuples, if matches template and template is a take template remove tuple from tuples list
            for (Tuple template : templateCallbackTake.keySet()) {
                if (tuple.matches(template) && !take) {
                    synchronized (tuple) {
                        tuples.remove(tuple);
                    }
                    templateCallbackTake.get(template).get(0).call(tuple);
                    if (templateCallbackTake.get(template).size() == 1) {
                        templateCallbackTake.remove(template);
                    }
                    else {
                        templateCallbackTake.get(template).remove(0);
                    }
                    take = true;
                }
            }
            for (Tuple template : templateCallbackRead.keySet()) {
                if (tuple.matches(template)) {
                    for (int i = 0; i < templateCallbackRead.get(template).size(); i++) {
                        templateCallbackRead.get(template).get(i).call(tuple);
                    }
                    //remove entry with the key template
                    templateCallbackRead.remove(template);
                }
            }
        });
        synchronized (this) {
            notifyAll();
        }
    }
    

    public Tuple take(Tuple template) {
        // search template in tuples list
        while (true) {
            for (Tuple tuple : tuples) {
                if (tuple.matches(template)) {
                    // remove tuple from tuples list
                    synchronized (tuple) {
                        tuples.remove(tuple);
                    }
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
    public Tuple read(Tuple template) {
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
                synchronized (tuple) {
                    tuples.remove(tuple);
                }
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
    public void eventRegister(eventMode mode, eventTiming timing, Tuple template, Callback callback) {
        //System.out.println("LindaCentralized eventRegister begin");
        if (timing == eventTiming.IMMEDIATE) {
            Tuple motifTuple = null;
                if (mode == eventMode.TAKE) {                 
                    motifTuple = tryTake(template);
                    if (motifTuple != null) {
                        callback.call(motifTuple);
                    } else {
                        //add tuple and callback to templateCallbackTake
                        ArrayList<Callback> listTake;
                        synchronized (templateCallbackTake) {
                            if (templateCallbackTake.get(template) != null) {
                                listTake = templateCallbackTake.get(template);
                                listTake.add(callback);
                                templateCallbackTake.remove(template);
                            } else {
                                listTake = new ArrayList<Callback>();
                                listTake.add(callback);
                            }
                            templateCallbackTake.put(template, listTake);
                        }
                    }
                } else {
                    motifTuple = tryRead(template);
                    if (motifTuple != null) {
                        callback.call(motifTuple);
                    } else {
                        //add tuple and callback to templateCallbackRead
                        ArrayList<Callback> listRead;
                        //
                        synchronized (templateCallbackRead) {
                            if (templateCallbackRead.get(template) != null) {
                                listRead = templateCallbackRead.get(template);
                                listRead.add(callback);
                                templateCallbackRead.remove(template);
                                
                            } else {
                                listRead = new ArrayList<Callback>();
                                listRead.add(callback);
                            }
                            templateCallbackRead.put(template, listRead);
                        }
                    }
                }                               
        } else {
            // if timing is future, current tuples are ignored.
            if (mode == eventMode.TAKE) {
                ArrayList<Callback> listTake;
                synchronized (templateCallbackTake) {
                    if (templateCallbackTake.get(template) != null) {
                        listTake = templateCallbackTake.get(template);
                        listTake.add(callback);
                        templateCallbackTake.remove(template);
                    } else {
                        listTake = new ArrayList<Callback>();
                        listTake.add(callback);
                    }
                    templateCallbackTake.put(template, listTake);
                }
            } else {
                ArrayList<Callback> listRead;
                synchronized (templateCallbackRead) {
                    if (templateCallbackRead.get(template) != null) {
                        listRead = templateCallbackRead.get(template);
                        listRead.add(callback);
                        templateCallbackRead.remove(template);
                    } else {
                        listRead = new ArrayList<Callback>();
                        listRead.add(callback);
                    }
                    templateCallbackRead.put(template, listRead);
                }
            }
         }
         //System.out.println("LindaCentralized eventRegister end");
    }

    @Override
    public synchronized void debug(String prefix) {
        System.out.println(prefix + " " + tuples.toString());
    }
}
