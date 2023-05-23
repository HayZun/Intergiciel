package linda.server;
import java.rmi.registry.*;
import linda.*;

public class Client {

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        Registry registry = LocateRegistry.getRegistry("localhost", 4000);
        RemoteCentralizedLinda proxy = (RemoteCentralizedLinda) registry.lookup("LindaServer");

        Tuple motif = new Tuple(Integer.class, String.class);
        Tuple t = new Tuple(4, "toto");
        proxy.write(t);
        Tuple res = proxy.take(motif);
        System.out.println("(1) Resultat:" + res);
    }
    
}
