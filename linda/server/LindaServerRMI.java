package linda.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LindaServerRMI {
    public static void main(String[] args) throws Exception {
        try {
            // Création du serveur Linda
            RemoteCentralizedLinda linda = new RemoteCentralizedLindaImpl();

            // Création d'un registre RMI sur le port 4000
            Registry registry = LocateRegistry.createRegistry(4000);

            // Enregistrement de linda dans le registre RMI
            registry.bind("LindaServer", linda);

            System.out.println("LindaServer ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
