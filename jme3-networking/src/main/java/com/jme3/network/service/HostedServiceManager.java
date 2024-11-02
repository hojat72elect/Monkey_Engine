

package com.jme3.network.service;

import com.jme3.network.ConnectionListener;
import com.jme3.network.HostedConnection;
import com.jme3.network.Server;


/**
 *  Manages HostedServices on behalf of a network Server object.
 *  All HostedServices are automatically informed about new and 
 *  leaving connections.
 *
 *  @author    Paul Speed
 */
public class HostedServiceManager extends ServiceManager<HostedServiceManager> {
    
    private Server server;
    private ConnectionObserver connectionObserver;

    /**
     *  Creates a HostedServiceManager for the specified network Server.
     */    
    public HostedServiceManager( Server server ) {
        this.server = server;
        this.connectionObserver = new ConnectionObserver();
        server.addConnectionListener(connectionObserver);
    }

    /**
     *  Returns the network Server associated with this HostedServiceManager.
     */
    public Server getServer() {
        return server;
    }

    /**
     *  Returns 'this' and is what is passed to HostedService.initialize()
     *  and HostedService.terminate();
     */
    @Override
    protected final HostedServiceManager getParent() {
        return this;
    }
 
    /**
     *  Adds the specified HostedService and initializes it.  If the service manager
     *  has already been started then the service will also be started.
     */   
    public void addService( HostedService s ) {
        super.addService(s);
    }

    /**
     *  Adds the specified services and initializes them.  If the service manager
     *  has already been started then the services will also be started.
     *  This is a convenience method that delegates to addService(), thus each
     *  service will be initialized (and possibly started) in sequence rather
     *  than doing them all at the end.
     */   
    public void addServices( HostedService... services ) {
        for( HostedService s : services ) {
            super.addService(s);
        }
    }

    /**
     *  Removes the specified HostedService from this service manager, stopping
     *  and terminating it as required.  If this service manager is in a
     *  started state then the service will be stopped.  After removal,
     *  the service will be terminated.
     */
    public void removeService( HostedService s ) {
        super.removeService(s);
    }
    
    /**
     *  Called internally when a new connection has been added so that the
     *  services can be notified.
     */
    protected void addConnection( HostedConnection hc ) {
        for( Service s : getServices() ) {
            ((HostedService)s).connectionAdded(server, hc);
        }
    }
 
    /**
     *  Called internally when a connection has been removed so that the
     *  services can be notified.
     */   
    protected void removeConnection( HostedConnection hc ) {
        for( Service s : getServices() ) {
            ((HostedService)s).connectionRemoved(server, hc);
        }
    }
 
    protected class ConnectionObserver implements ConnectionListener {

        @Override
        public void connectionAdded(Server server, HostedConnection hc) {
            addConnection(hc);
        }

        @Override
        public void connectionRemoved(Server server, HostedConnection hc) {
            removeConnection(hc);
        }
    }
}
