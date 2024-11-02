
package com.jme3.network.rmi;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * Contains the return value for a remote method invocation, sent as a response
 * to a {@link RemoteMethodCallMessage} with a non-zero invocationID.
 *
 * @author Kirill Vainer.
 */
@Serializable
public class RemoteMethodReturnMessage extends AbstractMessage {

    public RemoteMethodReturnMessage(){
        super(true);
    }

    /**
     * Invocation ID that was set in the {@link RemoteMethodCallMessage}.
     */
    public short invocationID;

    /**
     * The return value, could be null.
     */
    public Object retVal;


    @Override
    public String toString(){
        return "RemoteMethodReturnMessage[ID="+invocationID+", Value="+retVal.toString()+"]";
    }
}
