package linda.test;

import linda.*;
import linda.Linda.eventMode;
import linda.Linda.eventTiming;

public class Testccw1 {
    public static void main(String[] a) throws Exception {
        final Linda linda = new linda.shm.CentralizedLinda();
        linda.write(new Tuple(4, 5));
        System.out.println("");
        System.out.println("Ok, I have found Linda.");
    }
}