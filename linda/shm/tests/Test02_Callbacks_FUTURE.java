package linda.shm.tests;

import java.util.ArrayList;
import java.util.List;

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

    static void fin_test(int attendu, int num_test){
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
        reset_callbacks();
    }

    static void resultat_final(int nb_tests){
        int nb_reussi = nb_tests - nb_echoues;
        System.out.println("Nombre de tests réussis : " + nb_reussi + "/" + nb_tests);
        if (nb_echoues != 0){
            System.out.println("Liste des tests échoués : " + tests_echoues);
        }
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
        final Linda linda01 = new linda.shm.CentralizedLinda();
        linda01.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda01.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda01.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda01.write(new Tuple(4, "foo"));
        linda01.debug("(2)");
        fin_test(3,01);
        
        System.out.println("        DEBUT TEST 02 : wrrr");
        final Linda linda02 = new linda.shm.CentralizedLinda();
        linda02.write(new Tuple(4, "foo"));
        linda02.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda02.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda02.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda02.debug("(2)");
        fin_test(0,02);

        System.out.println("        DEBUT TEST 03 : rwrr");
        final Linda linda03 = new linda.shm.CentralizedLinda();
        linda03.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda03.write(new Tuple(4, "foo"));
        linda03.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda03.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda03.debug("(2)");
        fin_test(1,03);

        System.out.println("        DEBUT TEST 04 : rrwr");
        final Linda linda04 = new linda.shm.CentralizedLinda();
        linda04.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda04.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda04.write(new Tuple(4, "foo"));
        linda04.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda04.debug("(2)");
        fin_test(2,04);

        System.out.println("        DEBUT TEST 05 : trrw");
        final Linda linda05 = new linda.shm.CentralizedLinda();
        linda05.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda05.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda05.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda05.write(new Tuple(4, "foo"));
        linda05.debug("(2)");
        fin_test(3,05);

        System.out.println("        DEBUT TEST 06 : wtrr");
        final Linda linda06 = new linda.shm.CentralizedLinda();
        linda06.write(new Tuple(4, "foo"));
        linda06.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda06.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda06.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda06.debug("(2)");
        fin_test(0,06);
        System.out.println("Résultat attendu : 1 success");

        System.out.println("        DEBUT TEST 07 : twrr");
        final Linda linda07 = new linda.shm.CentralizedLinda();
        linda07.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda07.write(new Tuple(4, "foo"));
        linda07.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda07.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda07.debug("(2)");
        fin_test(1,07);

        System.out.println("        DEBUT TEST 08 : trwr");
        final Linda linda08 = new linda.shm.CentralizedLinda();
        linda08.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda08.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda08.write(new Tuple(4, "foo"));
        linda08.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda08.debug("(2)");
        fin_test(2,8);

        System.out.println("        DEBUT TEST 09 : rtrw");
        final Linda linda09 = new linda.shm.CentralizedLinda();
        linda09.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda09.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda09.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda09.write(new Tuple(4, "foo"));
        linda09.debug("(2)");
        fin_test(3,9);

        System.out.println("        DEBUT TEST 10 : wrtr");
        final Linda linda10 = new linda.shm.CentralizedLinda();
        linda10.write(new Tuple(4, "foo"));
        linda10.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda10.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda10.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda10.debug("(2)");
        fin_test(0,10);

        System.out.println("        DEBUT TEST 11 : rwtr");
        final Linda linda11 = new linda.shm.CentralizedLinda();
        linda11.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda11.write(new Tuple(4, "foo"));
        linda11.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda11.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda11.debug("(2)");
        fin_test(1,11);

        System.out.println("        DEBUT TEST 12 : rtwr");
        final Linda linda12 = new linda.shm.CentralizedLinda();
        linda12.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda12.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda12.write(new Tuple(4, "foo"));
        linda12.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda12.debug("(2)");
        fin_test(2,12);

        System.out.println("        DEBUT TEST 13 : rrtw");
        final Linda linda13 = new linda.shm.CentralizedLinda();
        linda13.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda13.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda13.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda13.write(new Tuple(4, "foo"));
        linda13.debug("(2)");
        fin_test(3,13);

        System.out.println("        DEBUT TEST 14 : wrrt");
        final Linda linda14 = new linda.shm.CentralizedLinda();
        linda14.write(new Tuple(4, "foo"));
        linda14.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda14.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda14.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda14.debug("(2)");
        fin_test(0,14);

        System.out.println("        DEBUT TEST 15 : rwrt");
        final Linda linda15 = new linda.shm.CentralizedLinda();
        linda15.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda15.write(new Tuple(4, "foo"));
        linda15.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda15.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda15.debug("(2)");
        fin_test(1,15);

        System.out.println("        DEBUT TEST 16 : rrwt");
        final Linda linda16 = new linda.shm.CentralizedLinda();
        linda16.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda16.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda16.write(new Tuple(4, "foo"));
        linda16.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda16.debug("(2)");
        fin_test(2,16);

        System.out.println("        DEBUT TEST 17 : rttw");
        final Linda linda17 = new linda.shm.CentralizedLinda();
        linda17.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda17.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda17.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda17.write(new Tuple(4, "foo"));
        linda17.debug("(2)");
        fin_test(2,17);

        System.out.println("        DEBUT TEST 18 : wrtt");
        final Linda linda18 = new linda.shm.CentralizedLinda();
        linda18.write(new Tuple(4, "foo"));
        linda18.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda18.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda18.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda18.debug("(2)");
        fin_test(0,18);

        System.out.println("        DEBUT TEST 19 : rwtt");
        final Linda linda19 = new linda.shm.CentralizedLinda();
        linda19.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda19.write(new Tuple(4, "foo"));
        linda19.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda19.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda19.debug("(2)");
        fin_test(1,19);

        System.out.println("        DEBUT TEST 20 : rtwt");
        final Linda linda20 = new linda.shm.CentralizedLinda();
        linda20.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda20.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda20.write(new Tuple(4, "foo"));
        linda20.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda20.debug("(2)");
        fin_test(2,20);

        System.out.println("        DEBUT TEST 21 : trtw");
        final Linda linda21 = new linda.shm.CentralizedLinda();
        linda21.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda21.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda21.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda21.write(new Tuple(4, "foo"));
        linda21.debug("(2)");
        fin_test(2,21);

        System.out.println("        DEBUT TEST 22 : wtrt");
        final Linda linda22 = new linda.shm.CentralizedLinda();
        linda22.write(new Tuple(4, "foo"));
        linda22.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda22.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda22.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda22.debug("(2)");
        fin_test(0,22);

        System.out.println("        DEBUT TEST 23 : twrt");
        final Linda linda23 = new linda.shm.CentralizedLinda();
        linda23.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda23.write(new Tuple(4, "foo"));
        linda23.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda23.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda23.debug("(2)");
        fin_test(1,23);

        System.out.println("        DEBUT TEST 24 : trwt");
        final Linda linda24 = new linda.shm.CentralizedLinda();
        linda24.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda24.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda24.write(new Tuple(4, "foo"));
        linda24.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda24.debug("(2)");
        fin_test(2,24);

        System.out.println("        DEBUT TEST 25 : ttrw");
        final Linda linda25 = new linda.shm.CentralizedLinda();
        linda25.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda25.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda25.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda25.write(new Tuple(4, "foo"));
        linda25.debug("(2)");
        fin_test(2,25);

        System.out.println("        DEBUT TEST 26 : wttr");
        final Linda linda26 = new linda.shm.CentralizedLinda();
        linda26.write(new Tuple(4, "foo"));
        linda26.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda26.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda26.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda26.debug("(2)");
        fin_test(0,26);
        
        System.out.println("        DEBUT TEST 27 : twtr");
        final Linda linda27 = new linda.shm.CentralizedLinda();
        linda27.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda27.write(new Tuple(4, "foo"));
        linda27.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda27.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda27.debug("(2)");
        fin_test(1,27);

        System.out.println("        DEBUT TEST 28 : ttwr");
        final Linda linda28 = new linda.shm.CentralizedLinda();
        linda28.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda28.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda28.write(new Tuple(4, "foo"));
        linda28.eventRegister(eventMode.READ, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda28.debug("(2)");
        fin_test(1,28);

        System.out.println("        DEBUT TEST 29 : tttw");
        final Linda linda29 = new linda.shm.CentralizedLinda();
        linda29.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda29.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda29.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda29.write(new Tuple(4, "foo"));
        linda29.debug("(2)");
        fin_test(1,29);

        System.out.println("        DEBUT TEST 30 : wttt");
        final Linda linda30 = new linda.shm.CentralizedLinda();
        linda30.write(new Tuple(4, "foo"));
        linda30.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda30.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda30.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda30.debug("(2)");
        fin_test(0,30);

        System.out.println("        DEBUT TEST 31 : twtt");
        final Linda linda31 = new linda.shm.CentralizedLinda();
        linda31.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda31.write(new Tuple(4, "foo"));
        linda31.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda31.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda31.debug("(2)");
        fin_test(1,31);

        System.out.println("        DEBUT TEST 32 : ttwt");
        final Linda linda32 = new linda.shm.CentralizedLinda();
        linda32.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda32.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda32.write(new Tuple(4, "foo"));
        linda32.eventRegister(eventMode.TAKE, eventTiming.FUTURE, cbmotif, new MyCallback());
        linda32.debug("(2)");
        fin_test(1,32);

        resultat_final(32);
    }
}