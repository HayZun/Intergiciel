package linda.server.tests;

import java.util.ArrayList;

import linda.*;
import linda.Tuple;
import linda.Linda.eventMode;
import linda.Linda.eventTiming;

public class Test00_Callback_unique {

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

    public static void main(String[] a) throws Exception {
        cbmotif = new Tuple(Integer.class, String.class);

        System.out.println("        DEBUT TEST 01 : Callback unique");
        final Linda linda = new linda.server.LindaClient("//localhost:4000/");
        System.out.println("Test eventregister begin");
        linda.eventRegister(eventMode.TAKE, eventTiming.IMMEDIATE, cbmotif, new MyCallback());
        System.out.println("Test eventregister end");
        System.out.println("Résultat attendu : Le test tourne à l'infini");
    }
}