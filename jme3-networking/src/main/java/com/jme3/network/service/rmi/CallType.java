

package com.jme3.network.service.rmi;


/**
 *  Internal type denoting the type of call to make when remotely
 *  invoking methods.
 *
 *  @author    Paul Speed
 */
public enum CallType {
    /**
     *  Caller will block until a response is received and returned.
     */
    Synchronous,
    
    /**
     *  Caller does not block or wait for a response.  The other end
     *  of the connection will also not send one.
     */ 
    Asynchronous,
    
    /**
     *  Similar to asynchronous in that no response is expected or sent
     *  but differs in that the call will be sent over UDP and so may
     *  not make it to the other end.
     */ 
    Unreliable
}
