package linda.server;

import linda.*;

public class ClientTest {
	
	public static void main(String[] args) {
		
		try {
			LindaServer.main(args);
		} catch (Exception e1) {
			e1.printStackTrace();
			System.out.println("failed to launch init server");
		}
		
		new Thread() {
            public void run() {
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                LindaClient client = new LindaClient("//localhost:1099/LindaServer");
                Tuple motif = new Tuple(1, "a");
                Tuple res = client.take(motif);
                System.out.println("(1) Resultat:" + res);
                client.debug("(1)");
            }
        }.start();
	}
	
}
