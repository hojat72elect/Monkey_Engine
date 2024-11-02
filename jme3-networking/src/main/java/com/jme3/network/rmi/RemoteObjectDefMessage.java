
package com.jme3.network.rmi;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

/**
 * Sent to expose RMI interfaces on the local client to other clients.
 * @author Kirill Vainer
 */
@Serializable
public class RemoteObjectDefMessage extends AbstractMessage {

    public ObjectDef[] objects;
    
    public RemoteObjectDefMessage(){
        super(true);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("RemoteObjectDefMessage[\n");
        for (ObjectDef def : objects){
            sb.append("\t").append(def).append("\n");
        }
        sb.append("]");
        return sb.toString();
    }

}
