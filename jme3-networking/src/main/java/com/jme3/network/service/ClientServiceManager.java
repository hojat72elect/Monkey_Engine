

package com.jme3.network.service;

import com.jme3.network.Client;


/**
 *  Manages ClientServices on behalf of a network Client object.
 *
 *  @author    Paul Speed
 */
public class ClientServiceManager extends ServiceManager<ClientServiceManager> {
    
    private Client client;
 
    /**
     *  Creates a new ClientServiceManager for the specified network Client.
     */   
    public ClientServiceManager( Client client ) {
        this.client = client;
    }
 
    /**
     *  Returns the network Client associated with this ClientServiceManager.
     */   
    public Client getClient() {
        return client;
    }

    /**
     *  Returns 'this' and is what is passed to ClientService.initialize()
     *  and ClientService.terminate();
     */
    @Override
    protected final ClientServiceManager getParent() {
        return this;
    }
    
    /**
     *  Adds the specified ClientService and initializes it.  If the service manager
     *  has already been started then the service will also be started.
     */   
    public void addService( ClientService s ) {
        super.addService(s);
    }

    /**
     *  Adds the specified services and initializes them.  If the service manager
     *  has already been started then the services will also be started.
     *  This is a convenience method that delegates to addService(), thus each
     *  service will be initialized (and possibly started) in sequence rather
     *  than doing them all at the end.
     */   
    public void addServices( ClientService... services ) {
        for( ClientService s : services ) {
            super.addService(s);
        }
    }

    /**
     *  Removes the specified ClientService from this service manager, stopping
     *  and terminating it as required.  If this service manager is in a
     *  started state then the service will be stopped.  After removal,
     *  the service will be terminated.
     */
    public void removeService( ClientService s ) {
        super.removeService(s);
    }
}
