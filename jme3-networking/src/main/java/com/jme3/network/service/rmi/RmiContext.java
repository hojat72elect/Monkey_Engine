

package com.jme3.network.service.rmi;

import com.jme3.network.HostedConnection;


/**
 *  Keeps track of the current connection performing a particular
 *  RMI call.  RMI-based services can use this to find out which
 *  connection is calling a particular method without having to
 *  pass additional problematic data on the method calls.
 *
 *  @author    Paul Speed
 */
public class RmiContext {
    private static final ThreadLocal<HostedConnection> connection = new ThreadLocal<HostedConnection>();
 
    /**
     * A private constructor to inhibit instantiation of this class.
     */
    private RmiContext() {
    }

    /**
     *  Returns the HostedConnection that is responsible for any
     *  RMI-related calls on this thread.
     */   
    public static HostedConnection getRmiConnection() {
        return connection.get();
    }
    
    static void setRmiConnection( HostedConnection conn ) {
        connection.set(conn);
    }
}
