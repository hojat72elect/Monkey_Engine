

package jme3test.network;

import com.jme3.app.SimpleApplication;
import com.jme3.export.Savable;
import com.jme3.network.Client;
import com.jme3.network.Network;
import com.jme3.network.Server;
import com.jme3.network.rmi.ObjectStore;
import com.jme3.network.serializing.Serializer;
import com.jme3.network.serializing.serializers.SavableSerializer;
import com.jme3.scene.Spatial;
import java.io.IOException;
import java.util.concurrent.Callable;

public class TestRemoteCall {

    private static SimpleApplication serverApp;

    /**
     * Interface implemented by the server, exposing
     * RMI calls that clients can use.
     */
    public static interface ServerAccess {
        /**
         * Attaches the model with the given name to the server's scene.
         * 
         * @param model The model name
         * 
         * @return True if the model was attached.
         * 
         * @throws RuntimeException If some error occurs.
         */
        public boolean attachChild(String model);
    }

    public static class ServerAccessImpl implements ServerAccess {
        @Override
        public boolean attachChild(String model) {
            if (model == null)
                throw new RuntimeException("Cannot be null. .. etc");

            final String finalModel = model;
            serverApp.enqueue(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    Spatial spatial = serverApp.getAssetManager().loadModel(finalModel);
                    serverApp.getRootNode().attachChild(spatial);
                    return null;
                }
            });
            return true;
        }
    }

    public static void createServer(){
        serverApp = new SimpleApplication() {
            @Override
            public void simpleInitApp() {
            }
        };
        serverApp.start();

        try {
            Server server = Network.createServer(5110);
            server.start();

            ObjectStore store = new ObjectStore(server);
            store.exposeObject("access", new ServerAccessImpl());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException{
        Serializer.registerClass(Savable.class, new SavableSerializer());

        createServer();

        Client client = Network.connectToServer("localhost", 5110);
        client.start();

        ObjectStore store = new ObjectStore(client);
        ServerAccess access = store.getExposedObject("access", ServerAccess.class, true);
        boolean result = access.attachChild("Models/Oto/Oto.mesh.xml");
        System.out.println(result);
    }
}
