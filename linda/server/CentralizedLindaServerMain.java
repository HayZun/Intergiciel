package linda.server;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class CentralizedLindaServerMain {

    public static void main(String[] args) throws Exception {
        LocateRegistry.createRegistry(1099);
        CentralizedLindaServer lindaServer = new CentralizedLindaServer();
        Naming.rebind("linda", lindaServer);
        System.out.println("Linda server is running...");
    }
}