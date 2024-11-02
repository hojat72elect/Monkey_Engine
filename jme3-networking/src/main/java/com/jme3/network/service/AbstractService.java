

package com.jme3.network.service;


/**
 *  Base class providing some default Service interface implementations
 *  as well as a few convenience methods such as getServiceManager()
 *  and getService(type).  Subclasses must at least override the
 *  onInitialize() method to handle service initialization.
 *
 *  @author    Paul Speed
 */
public abstract class AbstractService<S extends ServiceManager> implements Service<S> {
    
    private S serviceManager;
 
    protected AbstractService() {
    }
    
    /**
     *  Returns the ServiceManager that was passed to
     *  initialize() during service initialization.
     */
    protected S getServiceManager() {
        return serviceManager;
    }
 
    /**
     *  Retrieves the first sibling service of the specified
     *  type.
     */   
    @SuppressWarnings("unchecked")
    protected <T extends Service<S>> T getService( Class<T> type ) {
        return type.cast(serviceManager.getService(type));
    }    
    
    /**
     *  Initializes this service by keeping a reference to
     *  the service manager and calling onInitialize().
     */
    @Override
    public final void initialize( S serviceManager ) {
        this.serviceManager = serviceManager;
        onInitialize(serviceManager);
    }
 
    /**
     *  Called during initialize() for the subclass to perform
     *  implementation specific initialization.
     */   
    protected abstract void onInitialize( S serviceManager );
    
    /**
     *  Default implementation does nothing.  Implementations can
     *  override this to perform custom startup behavior.
     */
    @Override
    public void start() {    
    }
    
    /**
     *  Default implementation does nothing.  Implementations can
     *  override this to perform custom stop behavior.
     */
    @Override
    public void stop() {    
    }
    
    /**
     *  Default implementation does nothing.  Implementations can
     *  override this to perform custom termination behavior.
     */
    @Override
    public void terminate( S serviceManager ) {
    }
    
    @Override
    public String toString() {
        return getClass().getName() + "[serviceManager.class=" + (serviceManager != null ? serviceManager.getClass() : "") + "]";
    }
}
