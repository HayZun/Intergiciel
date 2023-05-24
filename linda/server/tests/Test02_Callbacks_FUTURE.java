package linda.server.tests;

import java.util.ArrayList;

import linda.*;
import linda.Tuple;
import linda.Linda.eventMode;
import linda.Linda.eventTiming;

// Utilisation du mode FUTURE uniquement
// Tests de callbacks, toutes les combinaisons possibles à 3 callbacks et 1 write :
// 3 read :          rrrw, wrrr, rwrr, rrwr
// 2 read + 1 take : trrw, wtrr, twrr, trwr 
//                   rtrw, wrtr, rwtr, rtwr 
//                   rrtw, wrrt, rwrt, rrwt
// 1 read + 2 take : rttw, wrtt, rwtt, rtwt
//                   trtw, wtrt, twrt, trwt
//                   ttrw, wttr, twtr, ttwr
// 3 take :          tttw, wttt, twtt, ttwt

public class Test02_Callbacks_FUTURE {

    private static Tuple cbmotif;
    private static int nb_callbacks = 0;
    private static int nb_echoues = 0;
    private static ArrayList<String> tests_echoues;

    static void incr_nb(){
        nb_callbacks += 1;
    }

    static int get_nb(){
        return nb_callbacks;
    }

    static void reset_callbacks(){
        nb_callbacks = 0;
    }

    static void fin_test(int attendu, int num_test, Linda linda){
        if (attendu == nb_callbacks){
            System.out.println("Test réussi : " + nb_callbacks + "/3 callbacks réussis comme prévu. ");
        } else {
            System.out.println("Test échoué : " + nb_callbacks + "/3 callbacks réussis.");
            System.out.println("Le score souhaité était de : " + attendu + "/3 callbacks réussis.");
            String test = num_test + "";
            tests_echoues.add(test);
            nb_echoues += 1;
        }
        System.out.println("        FIN TEST " + num_test);
        System.out.println("---------------------------------");
        System.out.println();
        linda.takeAll(cbmotif);
        remise_zero(linda);
    }

    static void resultat_final(int nb_tests){
        int nb_reussi = nb_tests - nb_echoues;
        System.out.println("Nombre de tests réussis : " + nb_reussi + "/" + nb_tests);
        if (nb_echoues != 0){
            System.out.println("Liste des tests échoués : " + tests_echoues);
        }
    }

    static void remise_zero(Linda linda){
        //
        System.out.println("NETTOYAGE DEBUT");
        linda.write(new Tuple(4, "foo"));
        linda.write(new Tuple(4, "foo"));
        linda.write(new Tuple(4, "foo"));
        linda.takeAll(cbmotif);
        reset_callbacks();
        System.out.println("NETTOYAGE FIN");
        System.out.println();
        //
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
        tests_echoues = new ArrayList<String>();
        cbmotif = new Tuple(Integer.class, String.class);

        System.out.println("        DEBUT TEST 01 : rrrw");
        final Linda linda = new linda.server.LindaClient("//localhost:4000/");
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));
        linda.debug("(2)");
        fin_test(3,01,linda);
        
        System.out.println("        DEBUT TEST 02 : wrrr");
        linda.write(new Tuple(4, "foo"));
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.debug("(2)");
        fin_test(0,02,linda);

        System.out.println("        DEBUT TEST 03 : rwrr");
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.debug("(2)");
        fin_test(1,03,linda);

        System.out.println("        DEBUT TEST 04 : rrwr");
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.debug("(2)");
        fin_test(2,04,linda);

        System.out.println("        DEBUT TEST 05 : trrw");
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));
        linda.debug("(2)");
        fin_test(3,05,linda);

        System.out.println("        DEBUT TEST 06 : wtrr");
        linda.write(new Tuple(4, "foo"));
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.debug("(2)");
        fin_test(0,06,linda);

        System.out.println("        DEBUT TEST 07 : twrr");
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.debug("(2)");
        fin_test(1,07,linda);        

        System.out.println("        DEBUT TEST 08 : trwr");
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.debug("(2)");
        fin_test(2,8,linda);

        System.out.println("        DEBUT TEST 09 : rtrw");
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));
        linda.debug("(2)");
        fin_test(3,9,linda);
        

        System.out.println("        DEBUT TEST 10 : wrtr");
        linda.write(new Tuple(4, "foo"));
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.debug("(2)");
        fin_test(0,10,linda);

        System.out.println("        DEBUT TEST 11 : rwtr");
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.debug("(2)");
        fin_test(1,11,linda);

        System.out.println("        DEBUT TEST 12 : rtwr");
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.debug("(2)");
        fin_test(2,12,linda);

        System.out.println("        DEBUT TEST 13 : rrtw");
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));
        linda.debug("(2)");
        fin_test(3,13,linda);
        

        System.out.println("        DEBUT TEST 14 : wrrt");
        linda.write(new Tuple(4, "foo"));
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.debug("(2)");
        fin_test(0,14,linda);
        

        System.out.println("        DEBUT TEST 15 : rwrt");
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.debug("(2)");
        fin_test(1,15,linda);
        

        System.out.println("        DEBUT TEST 16 : rrwt");
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.debug("(2)");
        fin_test(2,16,linda);
        

        System.out.println("        DEBUT TEST 17 : rttw");
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));
        linda.debug("(2)");
        fin_test(2,17,linda);

        System.out.println("        DEBUT TEST 18 : wrtt");
        linda.write(new Tuple(4, "foo"));
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.debug("(2)");
        fin_test(0,18,linda);

        System.out.println("        DEBUT TEST 19 : rwtt");
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.debug("(2)");
        fin_test(1,19,linda);

        System.out.println("        DEBUT TEST 20 : rtwt");
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.debug("(2)");
        fin_test(2,20,linda);

        System.out.println("        DEBUT TEST 21 : trtw");
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));
        linda.debug("(2)");
        fin_test(2,21,linda);

        System.out.println("        DEBUT TEST 22 : wtrt");
        linda.write(new Tuple(4, "foo"));
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.debug("(2)");
        fin_test(0,22,linda);

        System.out.println("        DEBUT TEST 23 : twrt");
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.debug("(2)");
        fin_test(1,23,linda);

        System.out.println("        DEBUT TEST 24 : trwt");
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.debug("(2)");
        fin_test(2,24,linda);

        System.out.println("        DEBUT TEST 25 : ttrw");
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));
        linda.debug("(2)");
        fin_test(2,25,linda);
        
        System.out.println("        DEBUT TEST 26 : wttr");
        linda.write(new Tuple(4, "foo"));
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.debug("(2)");
        fin_test(0,26,linda);

        System.out.println("        DEBUT TEST 27 : twtr");
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.debug("(2)");
        fin_test(1,27,linda);

        System.out.println("        DEBUT TEST 28 : ttwr");
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));
        linda.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.debug("(2)");
        fin_test(1,28,linda);

        System.out.println("        DEBUT TEST 29 : tttw");
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));
        linda.debug("(2)");
        fin_test(1,29,linda);

        System.out.println("        DEBUT TEST 30 : wttt");
        linda.write(new Tuple(4, "foo"));
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.debug("(2)");
        fin_test(0,30,linda);

        System.out.println("        DEBUT TEST 31 : twtt");
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.debug("(2)");
        fin_test(1,31,linda);

        System.out.println("        DEBUT TEST 32 : ttwt");
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.write(new Tuple(4, "foo"));
        linda.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda.debug("(2)");
        fin_test(1,32,linda);

        resultat_final(32);

        System.exit(0);
    }
}