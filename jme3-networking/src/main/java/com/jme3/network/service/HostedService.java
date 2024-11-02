

package com.jme3.network.service;

import com.jme3.network.ConnectionListener;


/**
 *  Interface implemented by Server-side services that augment
 *  a network Server's functionality. 
 *
 *  @author    Paul Speed
 */
public interface HostedService extends Service<HostedServiceManager>, ConnectionListener {
    
    /**
     *  Called when the service is first attached to the service
     *  manager.
     */
    @Override
    public void initialize( HostedServiceManager serviceManager );
    
    /**
     *  Called when the service manager is started or if the 
     *  service is added to an already started service manager.
     */
    @Override 
    public void start();
    
    /**
     *  Called when the service is shutting down.  All services
     *  are stopped and any service manager resources are closed
     *  before the services are terminated.
     */
    @Override
    public void stop();
    
    /**
     *  The service manager is fully shutting down.  All services
     *  have been stopped and related connections closed.
     */
    @Override
    public void terminate( HostedServiceManager serviceManager ); 
}
