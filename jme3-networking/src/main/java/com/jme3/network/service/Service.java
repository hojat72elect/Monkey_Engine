

package com.jme3.network.service;


/**
 *  The base interface for managed services.
 *
 *  @author    Paul Speed
 */
public interface Service<S> {
    
    /**
     *  Called when the service is first attached to the service
     *  manager.
     */
    public void initialize( S serviceManager );
    
    /**
     *  Called when the service manager is started or if the 
     *  service is added to an already started service manager.
     */
    public void start();
    
    /**
     *  Called when the service manager is shutting down.  All services
     *  are stopped and any service manager resources are closed
     *  before the services are terminated.
     */
    public void stop();
    
    /**
     *  The service manager is fully shutting down.  All services
     *  have been stopped and related connections closed.
     */
    public void terminate( S serviceManager ); 
}
