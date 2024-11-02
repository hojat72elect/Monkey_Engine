

package com.jme3.network.service.rpc.msg;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;
import com.jme3.network.serializing.Serializer;
import java.io.PrintWriter;
import java.io.StringWriter;

 
/**
 *  Used internally to send an RPC call's response back to
 *  the caller.
 *
 *  @author    Paul Speed
 */
@Serializable
public class RpcResponseMessage extends AbstractMessage {

    private long msgId;
    private Object result;
    private String error;
    private Object exception; // if it was serializable

    public RpcResponseMessage() {
    }
    
    public RpcResponseMessage( long msgId, Object result ) {
        this.msgId = msgId;
        this.result = result;
    }

    public RpcResponseMessage( long msgId, Throwable t ) {
        this.msgId = msgId;
 
        // See if the exception is serializable
        if( isSerializable(t) ) {
            // Can send the exception itself
            this.exception = t;
        } else {
            // We'll compose all of the info into a string           
            StringWriter sOut = new StringWriter();
            PrintWriter out = new PrintWriter(sOut);
            t.printStackTrace(out);
            out.close();
            this.error = sOut.toString();
        }
    }
 
    public static boolean isSerializable( Throwable error ) {
        if( error == null ) {
            return false;
        }
        for( Throwable t = error; t != null; t = t.getCause() ) {
            if( Serializer.getExactSerializerRegistration(t.getClass()) == null ) {
                return false;
            }
        }
        return true; 
    }
 
    public long getMessageId() {
        return msgId;
    }
    
    public Object getResult() {
        return result;
    }
        
    public String getError() {
        return error;
    }
    
    public Throwable getThrowable() {
        return (Throwable)exception;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[#" + msgId + ", result=" + result
                                          + (error != null ? ", error=" + error : "")
                                          + (exception != null ? ", exception=" + exception : "")
                                          + "]";
    }
}
