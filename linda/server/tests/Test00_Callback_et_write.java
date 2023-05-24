package linda.server.tests;

import java.util.ArrayList;

import linda.*;
import linda.Tuple;
import linda.Linda.eventMode;
import linda.Linda.eventTiming;

public class Test00_Callback_et_write {

    private static Tuple cbmotif;
    private static int nb_callbacks = 0;

    static void incr_nb(){
        nb_callbacks += 1;
    }

    static int get_nb(){
        return nb_callbacks;
    }

    private static class MyCallback implements Callback {
        public void call(Tuple t) {
            System.out.println("CB got "+t);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            incr_nb();
            System.out.println("CB done with "+t + " | Nombres callbacks réussis : " + get_nb());
        }
    }

    static void fin_test(){
        if (nb_callbacks == 1){
            System.out.println("Test réussi : 1/1 callbacks réussis comme prévu. ");
        } else {
            System.out.println("Test échoué : 0/1 callbacks réussis.");
            System.out.println("Le score souhaité était de 1/1 callbacks réussis.");
        }
        System.out.println("        FIN TEST ");
        System.out.println("---------------------------------");
        System.out.println();
    }

    public static void main(String[] a) throws Exception {
        cbmotif = new Tuple(Integer.class, String.class);

        System.out.println("        DEBUT TEST 01 : Callback unique");
        final Linda linda = new linda.server.LindaClient("//localhost:4000/");
        linda.debug("(2)");
        linda.write(new Tuple(4, "foo"));
        linda.debug("(2)");
        System.out.println("Test eventregister begin");
        linda.eventRegister(eventMode.TAKE, eventTiming.IMMEDIATE, cbmotif, new MyCallback());
        System.out.println("Test eventregister end");
        
        fin_test();
    }
}