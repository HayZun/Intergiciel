package linda.test;

import linda.*;
import linda.Linda.eventMode;
import linda.Linda.eventTiming;

public class Testccw1 {

    private static Linda linda;
    private static Tuple cbmotif;
    private static int nb_callbacks = 0;

    void incr_nb(){
        nb_callbacks += 1;
    }

    static int get_nb(){
        return nb_callbacks;
    }

    private static class MyCallback implements Callback {
        public void call(Tuple t) {
            System.out.println("CB got "+t);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            System.out.println("CB done with "+t + " | Nombres callbacks réussis : " + get_nb());
        }
    }

    public static void main(String[] a) throws Exception {
        final Linda linda = new linda.shm.CentralizedLinda();
        nb_callbacks = 0;
        cbmotif = new Tuple(Integer.class, String.class);
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));


        linda.debug("(2)");
    }
}