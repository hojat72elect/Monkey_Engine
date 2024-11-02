

package com.jme3.network.service.rmi;

import com.jme3.network.serializing.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 *  Internal information about shared methods.  This is part of the data that
 *  is passed over the wire when an object is shared. 
 *
 *  @author    Paul Speed
 */
@Serializable
public final class MethodInfo {
    
    public static final MethodInfo NULL_INFO = new MethodInfo();
    
    private String representation;
    private short id;
    private CallType callType;
    private transient Method method;
    
    /** 
     *  For serialization only.
     */
    public MethodInfo() {
    }
    
    public MethodInfo( short id, Method m ) {
        this.id = id;
        this.method = m;
        this.representation = methodToString(m);
        this.callType = getCallType(m);
    }
 
    public Object invoke( Object target, Object... parms ) {
        try {
            return method.invoke(target, parms);
        } catch (IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException e) {
            throw new RuntimeException("Error invoking:" + method + " on:" + target, e);
        }
    }
 
    public short getId() {
        return id;
    }
 
    public CallType getCallType() {
        return callType;
    }
 
    public boolean matches( Method m ) {
        return representation.equals(methodToString(m));
    }
 
    public static String methodToString( Method m ) {
        StringBuilder sb = new StringBuilder();
        for( Class t : m.getParameterTypes() ) {
            if( sb.length() > 0 ) 
                sb.append(", ");
            sb.append(t.getName());
        }
        return m.getReturnType().getName() + " " + m.getName() + "(" + sb + ")";
    }
 
    public static CallType getCallType( Method m ) {
        if( m.getReturnType() != Void.TYPE ) {
            return CallType.Synchronous;
        }
        if( m.getAnnotation(Asynchronous.class) == null ) {
            return CallType.Synchronous;
        }
        for (Annotation annotation : m.getAnnotations()) {
            Class<? extends Annotation> type = annotation.annotationType();
            if (type.getName().equals("javax.jws.Oneway")) {
                return CallType.Asynchronous;
            }
        }
            
        Asynchronous async = m.getAnnotation(Asynchronous.class);             
        return async.reliable() ? CallType.Asynchronous : CallType.Unreliable;         
    } 

    @Override
    public int hashCode() {
        return representation.hashCode();
    }
    
    @Override
    public boolean equals( Object o ) {
        if( o == this ) {
            return true;
        }
        if( o == null || o.getClass() != getClass() ) {
            return false;            
        }
        MethodInfo other = (MethodInfo)o;
        return representation.equals(other.representation);
    }
    
    @Override
    public String toString() {
        return "MethodInfo[#" + getId() + ", callType=" + callType + ", " + representation + "]";
    }
}
