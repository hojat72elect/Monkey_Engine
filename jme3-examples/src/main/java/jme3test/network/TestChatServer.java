
package jme3test.network;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.network.*;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;
import java.io.IOException;

/**
 *  A simple test chat server.  When SM implements a set
 *  of standard chat classes this can become a lot simpler.
 *
 *  @version   $Revision$
 *  @author    Paul Speed
 */
public class TestChatServer {
    // Normally these and the initialized method would
    // be in shared constants or something.

    public static final String NAME = "Test Chat Server";
    public static final int VERSION = 1;
    public static final int PORT = 5110;
    public static final int UDP_PORT = 5110;

    private Server server;
    private boolean isRunning;
    
    public TestChatServer() throws IOException {

        // Use this to test the client/server name version check
        this.server = Network.createServer(NAME, VERSION, PORT, UDP_PORT);

        // Initialize our own messages only after the server has been created.
        // It registers some additional messages with the serializer by default
        // that need to go before custom messages.
        initializeClasses();

        ChatHandler handler = new ChatHandler();
        server.addMessageListener(handler, ChatMessage.class);
        
        server.addConnectionListener(new ChatConnectionListener());
    }

    public boolean isRunning() {
        return isRunning;
    }
    
    public synchronized void start() {
        if( isRunning ) {
            return;
        }
        server.start();
        isRunning = true;
    }
    
    public synchronized void close() {
        if( !isRunning ) {
            return;
        }
        
        // Gracefully let any connections know that the server is
        // going down.  Without this, their connections will simply
        // error out.
        for( HostedConnection conn : server.getConnections() ) {
            conn.close("Server is shutting down.");
        }
        try {
            Thread.sleep(1000); // wait a couple beats to let the messages go out
        } catch( InterruptedException e ) {
            e.printStackTrace();
        }
        
        server.close();        
        isRunning = false;
        notifyAll();
    }

    protected void runCommand( HostedConnection conn, String user, String command ) {
        if( "/shutdown".equals(command) ) {
            server.broadcast(new ChatMessage("server", "Server is shutting down."));
            close();
        } else if( "/help".equals(command) ) {
            StringBuilder sb = new StringBuilder();
            sb.append("Chat commands:\n");
            sb.append("/help - prints this message.\n");
            sb.append("/shutdown - shuts down the server.");
            server.broadcast(new ChatMessage("server", sb.toString()));   
        }
    } 

    public static void initializeClasses() {
        // Doing it here means that the client code only needs to
        // call our initialize. 
        Serializer.registerClass(ChatMessage.class);
    }

    public static void main(String... args) throws Exception {
 
        // Increase the logging level for networking...
        System.out.println("Setting logging to max");
        Logger networkLog = Logger.getLogger("com.jme3.network"); 
        networkLog.setLevel(Level.FINEST);
 
        // And we have to tell JUL's handler also   
        // turn up logging in a very convoluted way
        Logger rootLog = Logger.getLogger("");
        if( rootLog.getHandlers().length > 0 ) {
            rootLog.getHandlers()[0].setLevel(Level.FINEST);
        }        
    
        TestChatServer chatServer = new TestChatServer();
        chatServer.start();
 
        System.out.println("Waiting for connections on port:" + PORT);
                
        // Keep running basically forever
        while( chatServer.isRunning ) {
            synchronized (chatServer) {
                chatServer.wait();
            }
        }
    }

    private class ChatHandler implements MessageListener<HostedConnection> {

        public ChatHandler() {
        }

        @Override
        public void messageReceived(HostedConnection source, Message m) {
            if (m instanceof ChatMessage) {
                // Keep track of the name just in case we 
                // want to know it for some other reason later, and it's
                // a good example of session data.
                ChatMessage cm = (ChatMessage)m;
                source.setAttribute("name", cm.getName());

                // Check for a / command
                if( cm.message.startsWith("/") ) {
                    runCommand(source, cm.name, cm.message);
                    return;
                }

                System.out.println("Broadcasting:" + m + "  reliable:" + m.isReliable());

                // Just rebroadcast... the reliable flag will stay the
                // same so if it came in on UDP it will go out on that too
                source.getServer().broadcast(cm);
            } else {
                System.err.println("Received odd message:" + m);
            }
        }
    }

    private class ChatConnectionListener implements ConnectionListener {

        @Override
        public void connectionAdded( Server server, HostedConnection conn ) {
            System.out.println("connectionAdded(" + conn + ")");
        }

        @Override
        public void connectionRemoved(Server server, HostedConnection conn) {
            System.out.println("connectionRemoved(" + conn + ")");
        }
        
    }
    
    @Serializable
    public static class ChatMessage extends AbstractMessage {

        private String name;
        private String message;

        public ChatMessage() {
        }

        public ChatMessage(String name, String message) {
            setName(name);
            setMessage(message);
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setMessage(String s) {
            this.message = s;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public String toString() {
            return name + ":" + message;
        }
    }
}
