

package com.jme3.network.service.rpc;


/**
 *  Implementations of this interface can be registered with
 *  the RpcClientService or RpcHostService to handle the 
 *  remote procedure calls for a given object or objects.
 *
 *  @author    Paul Speed
 */
public interface RpcHandler {

    /**
     *  Called when a remote procedure call request is received for a particular
     *  object from the other end of the network connection.
     */
    public Object call( RpcConnection conn, short objectId, short procId, Object... args );
}


