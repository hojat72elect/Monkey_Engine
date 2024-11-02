

package com.jme3.network.service.rmi;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 *  Used internally to remotely invoke methods on RMI shared objects.
 *
 *  @author    Paul Speed
 */
public class RemoteObjectHandler implements InvocationHandler {

    private final RmiRegistry rmi;
    private final byte channel;
    private final short objectId;
    private final ClassInfo typeInfo;
    private final Map<Method, MethodInfo> methodIndex = new ConcurrentHashMap<>();

    public RemoteObjectHandler( RmiRegistry rmi, byte channel, short objectId, ClassInfo typeInfo ) {
        this.rmi = rmi;
        this.channel = channel;
        this.objectId = objectId;
        this.typeInfo = typeInfo;
    } 

    protected MethodInfo getMethodInfo( Method method ) {
        MethodInfo mi = methodIndex.get(method);
        if( mi == null ) {
            mi = typeInfo.getMethod(method);
            if( mi == null ) {
                mi = MethodInfo.NULL_INFO;
            }                      
            methodIndex.put(method, mi);
        }
        return mi == MethodInfo.NULL_INFO ? null : mi;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] os) throws Throwable {
        MethodInfo mi = getMethodInfo(method);
        if( mi == null ) {
            // Try to invoke locally
            return method.invoke(this, os);
        }
        return rmi.invokeRemote(channel, objectId, mi.getId(), mi.getCallType(), os);
    }
 
    @Override
    public String toString() {
        return "RemoteObject[#" + objectId + ", " + typeInfo.getName() + "]";
    }   
}
