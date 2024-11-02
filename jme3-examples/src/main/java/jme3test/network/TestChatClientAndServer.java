package jme3test.network;


/**
 *  Combines the server instance and a client instance into the
 *  same JVM to show an example of, and to test, a pattern like
 *  self-hosted multiplayer games.
 */
public class TestChatClientAndServer {
    
    public static void main( String... args ) throws Exception {

        System.out.println("Starting chat server...");    
        TestChatServer chatServer = new TestChatServer();
        chatServer.start();
 
        System.out.println("Waiting for connections on port:" + TestChatServer.PORT);
 
        // Now launch a client

        TestChatClient test = new TestChatClient("localhost");
        test.setVisible(true);
        
        // Register a shutdown hook to get a message on the console when the
        // app actually finishes
        Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    System.out.println("Client and server test is terminating.");
                }
            });
                
        // Keep running basically forever or until the server
        // shuts down
        while( chatServer.isRunning() ) {
            synchronized (chatServer) {
                chatServer.wait();
            }
        }    
    }
}
