

package com.jme3.network.service;

import com.jme3.network.Client;

/**
 *  Convenient base class for ClientServices providing some default ClientService 
 *  interface implementations as well as a few convenience methods 
 *  such as getServiceManager() and getService(type).  Subclasses 
 *  must at least override the onInitialize() method to handle 
 *  service initialization.
 *
 *  @author    Paul Speed
 */
public abstract class AbstractClientService extends AbstractService<ClientServiceManager> 
                                            implements ClientService { 
    
    protected AbstractClientService() {
    }
   
    /**
     *  Returns the client for this client service or null if
     *  the service is not yet attached.
     */   
    protected Client getClient() {
        ClientServiceManager csm = getServiceManager();
        return csm == null ? null : csm.getClient();
    }

}
