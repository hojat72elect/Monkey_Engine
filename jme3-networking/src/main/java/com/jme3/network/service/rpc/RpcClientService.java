

package com.jme3.network.service.rpc;

import com.jme3.network.Client;
import com.jme3.network.util.ObjectMessageDelegator;
import com.jme3.network.service.AbstractClientService;
import com.jme3.network.service.ClientServiceManager;


/**
 *  RPC service that can be added to a network Client to
 *  add RPC send/receive capabilities.  Remote procedure
 *  calls can be made to the server and responses retrieved.
 *  Any remote procedure calls that the server performs for
 *  this connection will be received by this service and delegated
 *  to the appropriate RpcHandlers. 
 *
 *  @author    Paul Speed
 */
public class RpcClientService extends AbstractClientService {

    private RpcConnection rpc;
    private ObjectMessageDelegator delegator;

    /**
     *  Creates a new RpcClientService that can be registered
     *  with the network Client object.
     */
    public RpcClientService() {
    }

    /**
     *  Returns the underlying RPC connection for use by other
     *  services that may require a more generic non-client/server
     *  specific RPC object with which to interact.
     */
    public RpcConnection getRpcConnection() {
        return rpc;
    }

    /**
     *  Used internally to set up the RpcConnection and MessageDelegator.
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void onInitialize( ClientServiceManager serviceManager ) {        
        Client client = serviceManager.getClient();
        this.rpc = new RpcConnection(client);
        
        delegator = new ObjectMessageDelegator(rpc, true);       
        client.addMessageListener(delegator, delegator.getMessageTypes());                   
    }

    /**
     *  Used internally to unregister the RPC MessageDelegator that
     *  was previously added to the network Client.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void terminate( ClientServiceManager serviceManager ) {
        Client client = serviceManager.getClient();
        client.removeMessageListener(delegator, delegator.getMessageTypes());                   
    }
 
    /**
     *  Performs a synchronous call on the server against the specified
     *  object using the specified procedure ID.  Both inbound and outbound
     *  communication is done on the specified channel.
     */
    public Object callAndWait( byte channel, short objId, short procId, Object... args ) {
        return rpc.callAndWait(channel, objId, procId, args);
    }

    /**
     *  Performs an asynchronous call on the server against the specified
     *  object using the specified procedure ID.  Communication is done
     *  over the specified channel.  No responses are received and none
     *  are waited for.
     */
    public void callAsync( byte channel, short objId, short procId, Object... args ) {
        rpc.callAsync(channel, objId, procId, args);
    }
 
    /** 
     *  Register a handler that will be called when the server
     *  performs a remove procedure call against this client. 
     *  Only one handler per object ID can be registered at any given time,
     *  though the same handler can be registered for multiple object
     *  IDs.
     */    
    public void registerHandler( short objId, RpcHandler handler ) {
        rpc.registerHandler(objId, handler);
    }
 
    /**
     *  Removes a previously registered handler for the specified
     *  object ID.  
     */
    public void removeHandler( short objId, RpcHandler handler ) {
        rpc.removeHandler(objId, handler);
    }

}
