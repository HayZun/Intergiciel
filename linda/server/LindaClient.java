package linda.server;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collection;
import java.util.concurrent.Semaphore;

import linda.Callback;
import linda.Linda;
import linda.Tuple;

/** Client part of a client/server implementation of Linda.
 * It implements the Linda interface and propagates everything to the server it is connected to.
 * */
public class LindaClient implements Linda {

    private RemoteCentralizedLinda server;
    private Semaphore semaphore;

    /** Initializes the Linda implementation.
     *  @param serverURI the URI of the server, e.g. "rmi://localhost:4000/LindaServer" or "//localhost:4000/LindaServer".
     */
    public LindaClient(String serverURI) throws RemoteException, NotBoundException {
        // Parse the server URI
        String[] serverURIParts = serverURI.split("/", 4);
        //get the name of the server with the port
        String[] serverHost = (serverURIParts[2]).split(":", 2);
        //get the name of host
        String hostName = serverHost[0];
        //get the port
        Integer serverPort = Integer.parseInt(serverHost[1]);
        //get the name of the server
        String serverObjectName = serverURIParts[3];

        // Get the registry and the remote server object from the server URI
        Registry registry = LocateRegistry.getRegistry(hostName, serverPort);
        server = (RemoteCentralizedLinda) registry.lookup(serverObjectName);
        semaphore = new Semaphore(2); // init with 1 permit
    }

    @Override
    public void write(Tuple t)  {
        try {
            semaphore.acquire(); // wait for permit
            server.write(t);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            semaphore.release(); // release permit
        }
    }

    @Override
    public Tuple take(Tuple template) {
        try {
            semaphore.acquire(); // wait for permit
            Tuple tuple = server.take(template);
            if (tuple != null) {
                // Si un tuple correspondant au template a été trouvé, on le retourne
                return tuple;
            } else {
                // Sinon, on retourne null
                return null;
            }
        } catch (RemoteException e) {
            // Gérer les erreurs de communication avec le serveur
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } finally {
            semaphore.release(); // release permit
        }
    }

    @Override
    public Tuple read(Tuple template) {
            Tuple tuple;
            try {
                tuple = server.read(template);
                if (tuple != null) {
                    // Si un tuple correspondant au template a été trouvé, on le retourne
                    return tuple;
                }
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // Sinon, on retourne null
            return null;
    }

    @Override
    public Tuple tryTake(Tuple template) {
        try {
            semaphore.acquire(); // wait for permit
            Tuple tuple = server.tryTake(template);
            if (tuple != null) {
                // Si un tuple correspondant au template a été trouvé, on le retourne
                return tuple;
            } else {
                // Sinon, on retourne null
                return null;
            }
        } catch (RemoteException e) {
            // Gérer les erreurs de communication avec le serveur
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } finally {
            semaphore.release(); // release permit
        }
    }

    @Override
    public Tuple tryRead(Tuple template) {
        try {
            semaphore.acquire(); // wait for permit
            Tuple tuple = server.tryRead(template);
            if (tuple != null) {
                // Si un tuple correspondant au template a été trouvé, on le retourne
                return tuple;
            } else {
                // Sinon, on retourne null
                return null;
            }
        } catch (RemoteException e) {
            // Gérer les erreurs de communication avec le serveur
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } finally {
            semaphore.release(); // release permit
        }
    }

    @Override
    public Collection<Tuple> takeAll(Tuple template) {
        try {
            semaphore.acquire(); // wait for permit
            Collection<Tuple> tuples = server.takeAll(template);
            if (tuples != null) {
                // Si un tuple correspondant au template a été trouvé, on le retourne
                return tuples;
            } else {
                // Sinon, on retourne null
                return null;
            }
        } catch (RemoteException e) {
            // Gérer les erreurs de communication avec le serveur
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } finally {
            semaphore.release(); // release permit
        }
    }

    @Override
    public Collection<Tuple> readAll(Tuple template) {
        try {
            semaphore.acquire(); // wait for permit
            Collection<Tuple> tuples = server.readAll(template);
            if (tuples != null) {
                // Si un tuple correspondant au template a été trouvé, on le retourne
                return tuples;
            } else {
                // Sinon, on retourne null
                return null;
            }
        } catch (RemoteException e) {
            // Gérer les erreurs de communication avec le serveur
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } finally {
            semaphore.release(); // release permit
        }
    }

    @Override
    public void eventRegister(eventMode mode, eventTiming timing, Tuple template, Callback callback) {
        try {
            semaphore.acquire(); // wait for permit
            //serializable object callback
            MySerializableCallback serializableCallback = new MySerializableCallback(callback);
            server.eventRegister(mode, timing, template, serializableCallback);
        } catch (RemoteException e) {
            // Gérer les erreurs de communication avec le serveur
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            semaphore.release(); // release permit
        }
    }

    @Override
    public void debug(String prefix) {
        try {
            semaphore.acquire(); // wait for permit
            System.out.println(prefix + " " + server.getTuples().toString());
            //sleeep
            Thread.sleep(1000);
        } catch (RemoteException e) {
            // Gérer les erreurs de communication avec le serveur
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            semaphore.release(); // release permit
        }
    }
}
