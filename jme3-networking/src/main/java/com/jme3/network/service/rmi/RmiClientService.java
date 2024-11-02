

package com.jme3.network.service.rmi;

import com.jme3.network.MessageConnection;
import com.jme3.network.service.AbstractClientService;
import com.jme3.network.service.ClientServiceManager;
import com.jme3.network.service.rpc.RpcClientService;
import java.util.ArrayList;
import java.util.List;


/**
 *  A service that can be added to the client to support a simple
 *  shared objects protocol.
 *
 *  <p>Objects are shared by adding them to the RmiRegistry with one of the
 *  share() methods.  Shared objects must have a separate interface and implementation.
 *  The interface is what the other end of the connection will use to interact
 *  with the object and that interface class must be available on both ends of
 *  the connection.  The implementing class need only be on the sharing end.</p>
 *
 *  <p>Shared objects can be accessed on the other end of the connection by
 *  using one of the RmiRegistry's getRemoteObject() methods.  These can be
 *  used to lookup an object by class if it is a shared singleton or by name
 *  if it was registered with a name.</p>
 * 
 *  <p>Note: This RMI implementation is not as advanced as Java's regular
 *  RMI as it won't marshall shared references, ie: you can't pass
 *  a shared objects as an argument to another shared object's method.</p>
 *
 *  @author    Paul Speed
 */
public class RmiClientService extends AbstractClientService {

    private RpcClientService rpc;
    private byte defaultChannel;
    private short rmiObjectId;
    private RmiRegistry rmi;
    private volatile boolean isStarted = false;
    
    private final List<ObjectInfo> pending = new ArrayList<>();   
    
    public RmiClientService() {
        this((short)-1, (byte)MessageConnection.CHANNEL_DEFAULT_RELIABLE);
    }
    
    public RmiClientService( short rmiObjectId, byte defaultChannel ) {
        this.defaultChannel = defaultChannel;
        this.rmiObjectId = rmiObjectId;
    }

    /**
     *  Shares the specified object with the server and associates it with the 
     *  specified type.  Objects shared in this way are available in the connection-specific
     *  RMI registry on the server and are not available to other connections.
     */
    public <T> void share( T object, Class<? super T> type ) {
        share(defaultChannel, object, type);       
    }
    
    /**
     *  Shares the specified object with the server and associates it with the 
     *  specified type.  Objects shared in this way are available in the connection-specific
     *  RMI registry on the server and are not available to other connections.
     *  All object related communication will be done over the specified connection
     *  channel.
     */
    public <T> void share( byte channel, T object, Class<? super T> type ) {
        share(channel, type.getName(), object, type);
    } 
    
    /**
     *  Shares the specified object with the server and associates it with the 
     *  specified name.  Objects shared in this way are available in the connection-specific
     *  RMI registry on the server and are not available to other connections.
     */
    public <T> void share( String name, T object, Class<? super T> type ) {
        share(defaultChannel, name, object, type);
    }
    
    /**
     *  Shares the specified object with the server and associates it with the 
     *  specified name.  Objects shared in this way are available in the connection-specific
     *  RMI registry on the server and are not available to other connections.
     *  All object related communication will be done over the specified connection
     *  channel.
     */
    public <T> void share( byte channel, String name, T object, Class<? super T> type ) {
        if( !isStarted ) {
            synchronized(pending) {
                if( !isStarted ) {
                    pending.add(new ObjectInfo(channel, name, object, type));
                    return;
                }
            }   
        }
        
        // Else we can add it directly.
        rmi.share(channel, name, object, type);
    }

    /**
     *  Looks up a remote object on the server by type and returns a local proxy to the 
     *  remote object that was shared on the other end of the network connection.  
     */
    public <T> T getRemoteObject( Class<T> type ) {
        return rmi.getRemoteObject(type);
    }
        
    /**
     *  Looks up a remote object on the server by name and returns a local proxy to the 
     *  remote object that was shared on the other end of the network connection.  
     */
    public <T> T getRemoteObject( String name, Class<T> type ) {
        return rmi.getRemoteObject(name, type);
    }    

    @Override
    protected void onInitialize( ClientServiceManager s ) {
        rpc = getService(RpcClientService.class);
        if( rpc == null ) {
            throw new RuntimeException("RmiClientService requires RpcClientService");
        }
        
        // Register it now so that it is available when the
        // server starts to send us stuff.  Waiting until start()
        // is too late in this case.
        rmi = new RmiRegistry(rpc.getRpcConnection(), rmiObjectId, defaultChannel);        
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void start() {
        super.start();
        
        // Register all of the classes that have been waiting.
        synchronized(pending) {
            for( ObjectInfo info : pending ) {
                rmi.share(info.channel, info.name, info.object, info.type);
            }
            pending.clear();
            isStarted = true;
        }
    }
    
    private class ObjectInfo {
        byte channel;
        String name;
        Object object;
        Class type;
        
        public ObjectInfo( byte channel, String name, Object object, Class type ) {
            this.channel = channel;
            this.name = name;
            this.object = object;
            this.type = type;
        }
        
        @Override
        public String toString() {
            return "ObjectInfo[" + channel + ", " + name + ", " + object + ", " + type + "]";
        }
    }   
}

