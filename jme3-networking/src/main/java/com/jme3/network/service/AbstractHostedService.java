

package com.jme3.network.service;

import com.jme3.network.HostedConnection;
import com.jme3.network.Server;


/**
 *  Convenient base class for HostedServices providing some default HostedService 
 *  interface implementations as well as a few convenience methods 
 *  such as getServiceManager() and getService(type).  Subclasses 
 *  must at least override the onInitialize() method to handle 
 *  service initialization.
 *
 *  @author    Paul Speed
 */
public abstract class AbstractHostedService extends AbstractService<HostedServiceManager> 
                                            implements HostedService { 
    
    protected AbstractHostedService() {
    }
 
    /**
     *  Returns the server for this hosted service or null if
     *  the service is not yet attached.
     */   
    protected Server getServer() {
        HostedServiceManager hsm = getServiceManager();
        return hsm == null ? null : hsm.getServer();
    }

    /**
     *  Default implementation does nothing.  Implementations can
     *  override this to perform custom new connection behavior.
     */
    @Override
    public void connectionAdded(Server server, HostedConnection hc) {
    }

    /**
     *  Default implementation does nothing.  Implementations can
     *  override this to perform custom leaving connection behavior.
     */
    @Override
    public void connectionRemoved(Server server, HostedConnection hc) {
    }
    
}
