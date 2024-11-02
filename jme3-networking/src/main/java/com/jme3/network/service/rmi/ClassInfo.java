

package com.jme3.network.service.rmi;

import com.jme3.network.serializing.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 *  Internal information about a shared class.  This is the information
 *  that is sent over the wire for shared types.
 *
 *  @author    Paul Speed
 */
@Serializable
public final class ClassInfo {
    private String name;
    private short typeId;
    private MethodInfo[] methods;
 
    /**
     *  For serialization only.
     */   
    public ClassInfo() {
    }
    
    public ClassInfo( short typeId, Class type ) {
        this.typeId = typeId;
        this.name = type.getName();
        this.methods = toMethodInfo(type, type.getMethods());
    }
 
    public String getName() {
        return name;
    }
 
    public Class getType() {
        try {
            return Class.forName(name);
        } catch( ClassNotFoundException e ) {
            throw new RuntimeException("Error finding class for:" + this, e);   
        }
    }
 
    public short getId() {
        return typeId;
    }
 
    public MethodInfo getMethod( short id ) {
        return methods[id];
    }
 
    public MethodInfo getMethod( Method m ) {        
        for( MethodInfo mi : methods ) {
            if( mi.matches(m) ) {
                return mi;
            }
        }
        return null; 
    }
    
    private MethodInfo[] toMethodInfo( Class type, Method[] methods ) {
        List<MethodInfo> result = new ArrayList<>();
        short methodId = 0;
        for( Method m : methods ) {
            // Simple... add all methods exposed through the interface
            result.add(new MethodInfo(methodId++, m));
        }
        return result.toArray(new MethodInfo[result.size()]);
    }
 
    public MethodInfo[] getMethods() {
        return methods;
    }
    
    @Override
    public String toString() {
        return "ClassInfo[" + name + "]";
    }
}
