package linda.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LindaServer {
	
	 public static void main (String[] args) throws Exception {
	        //  create naming service
	        try {
	        	Registry registry = LocateRegistry.createRegistry(4000);

	            //  create LindaObject,
	            //  register object in naming service
	            LindaRemote LindaImpl = new LindaRemote();
	            registry.bind("LindaServer", LindaImpl);

	            // service ready : awaiting for calls
	            System.out.println ("// system ready //");
	        } catch (java.rmi.server.ExportException e) {
	            //System.out.println("A registry is already running, proceeding...");
	        }
	    }
}
