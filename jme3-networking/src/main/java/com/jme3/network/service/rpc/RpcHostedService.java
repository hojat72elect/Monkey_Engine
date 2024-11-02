

package com.jme3.network.service.rpc;

import com.jme3.network.HostedConnection;
import com.jme3.network.Server;
import com.jme3.network.serializing.Serializer;
import com.jme3.network.service.AbstractHostedConnectionService;
import com.jme3.network.util.SessionDataDelegator;
import com.jme3.network.service.HostedServiceManager;
import com.jme3.network.service.rpc.msg.RpcCallMessage;
import com.jme3.network.service.rpc.msg.RpcResponseMessage;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *  RPC service that can be added to a network Server to
 *  add RPC send/receive capabilities.  For a particular
 *  HostedConnection, Remote procedure calls can be made to the 
 *  associated Client and responses retrieved.  Any remote procedure 
 *  calls that the Client performs for this connection will be 
 *  received by this service and delegated to the appropriate RpcHandlers.
 *
 *  Note: it can be dangerous for a server to perform synchronous
 *  RPC calls to a client but especially so if not done as part
 *  of the response to some other message.  ie: iterating over all
 *  or some HostedConnections to perform synchronous RPC calls
 *  will be slow and potentially block the server's threads in ways
 *  that can cause deadlocks or odd contention. 
 *
 *  @author    Paul Speed
 */
public class RpcHostedService extends AbstractHostedConnectionService {

    private static final String ATTRIBUTE_NAME = "rpcSession";

    private static final Logger log = Logger.getLogger(RpcHostedService.class.getName());

    private SessionDataDelegator delegator;

    /**
     *  Creates a new RPC host service that can be registered
     *  with the Network server and will automatically 'host'
     *  RPC services and each new network connection.
     */
    public RpcHostedService() {
        this(true);
    }
    
    /**
     *  Creates a new RPC host service that can be registered
     *  with the Network server and will optionally 'host'
     *  RPC services and each new network connection depending
     *  on the specified 'autoHost' flag.
     */
    public RpcHostedService( boolean autoHost ) {
        super(autoHost);
        
        // This works for me... has to be different in
        // the general case
        Serializer.registerClasses(RpcCallMessage.class, RpcResponseMessage.class);
    }

    /**
     *  Used internally to set up the message delegator that will
     *  handle HostedConnection specific messages and forward them
     *  to that connection's RpcConnection.
     */
    @Override
    protected void onInitialize( HostedServiceManager serviceManager ) {
        Server server = serviceManager.getServer();
         
        // A general listener for forwarding the messages
        // to the client-specific handler
        this.delegator = new SessionDataDelegator(RpcConnection.class, 
                                                  ATTRIBUTE_NAME,
                                                  true);
        server.addMessageListener(delegator, delegator.getMessageTypes());

        if( log.isLoggable(Level.FINEST) ) {
            log.log(Level.FINEST, "Registered delegator for message types:{0}", Arrays.asList(delegator.getMessageTypes()));
        }
    }

    /**
     *  Retrieves the RpcConnection for the specified HostedConnection
     *  if that HostedConnection has had RPC services started using
     *  startHostingOnConnection() (or via autohosting).  Returns null
     *  if the connection currently doesn't have RPC hosting services
     *  attached.
     */
    public RpcConnection getRpcConnection( HostedConnection hc ) {
        return hc.getAttribute(ATTRIBUTE_NAME);
    }

    /**
     *  Sets up RPC hosting services for the hosted connection allowing
     *  getRpcConnection() to return a valid RPC connection object.
     *  This method is called automatically for all new connections if
     *  autohost is set to true.
     */
    @Override
    public void startHostingOnConnection( HostedConnection hc ) {
        if( log.isLoggable(Level.FINEST) ) {
            log.log(Level.FINEST, "startHostingOnConnection:{0}", hc);
        }
        hc.setAttribute(ATTRIBUTE_NAME, new RpcConnection(hc));
    }

    /**
     *  Removes any RPC hosting services associated with the specified
     *  connection.  Calls to getRpcConnection() will return null for
     *  this connection.  The connection's RpcConnection is also closed,
     *  releasing any waiting synchronous calls with a "Connection closing"
     *  error.
     *  This method is called automatically for all leaving connections if
     *  autohost is set to true.
     */
    @Override
    public void stopHostingOnConnection( HostedConnection hc ) {
        RpcConnection rpc = hc.getAttribute(ATTRIBUTE_NAME);
        if( rpc == null ) {
            return;
        }
        if( log.isLoggable(Level.FINEST) ) {
            log.log(Level.FINEST, "stopHostingOnConnection:{0}", hc);
        }
        hc.setAttribute(ATTRIBUTE_NAME, null);
        rpc.close();
    }

    /**
     *  Used internally to remove the message delegator from the
     *  server.
     */
    @Override
    public void terminate(HostedServiceManager serviceManager) {
        Server server = serviceManager.getServer();
        server.removeMessageListener(delegator, delegator.getMessageTypes());
    }

}

