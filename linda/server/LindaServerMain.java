package linda.server;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;

public class LindaServerMain {

    public static void main(String[] args) throws Exception {
        LocateRegistry.createRegistry(4000);
        LindaServer lindaServer = new LindaServer();
        Naming.bind("rmi://localhost:4000/LindaServer", (Remote) lindaServer);
        System.out.println("Linda server is running...");
    }
}