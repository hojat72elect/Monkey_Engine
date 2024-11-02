

package jme3test.network;

import com.jme3.network.*;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;
import java.io.IOException;

public class TestMessages {

    @Serializable
    public static class PingMessage extends AbstractMessage {
    }

    @Serializable
    public static class PongMessage extends AbstractMessage {
    }

    private static class ServerPingResponder implements MessageListener<HostedConnection> {
        @Override
        public void messageReceived(HostedConnection source, com.jme3.network.Message message) {
            if (message instanceof PingMessage){
                System.out.println("Server: Received ping message!");
                source.send(new PongMessage());
            }
        }
    }

    private static class ClientPingResponder implements MessageListener<Client> {
        @Override
        public void messageReceived(Client source, com.jme3.network.Message message) {
            if (message instanceof PongMessage){
                System.out.println("Client: Received pong message!");
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException{
        Serializer.registerClass(PingMessage.class);
        Serializer.registerClass(PongMessage.class);

        Server server = Network.createServer(5110);
        server.start();

        Client client = Network.connectToServer("localhost", 5110);
        client.start();

        server.addMessageListener(new ServerPingResponder(), PingMessage.class);
        client.addMessageListener(new ClientPingResponder(), PongMessage.class);

        System.out.println("Client: Sending ping message..");
        client.send(new PingMessage());
        
        Object obj = new Object();
        synchronized (obj){
            obj.wait();
        }
    }
}
