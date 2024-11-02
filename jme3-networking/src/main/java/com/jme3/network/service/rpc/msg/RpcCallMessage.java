

package com.jme3.network.service.rpc.msg;

import com.jme3.network.AbstractMessage;
import com.jme3.network.serializing.Serializable;

 
/**
 *  Used internally to send RPC call information to
 *  the other end of a connection for execution.
 *
 *  @author    Paul Speed
 */
@Serializable
public class RpcCallMessage extends AbstractMessage {

    private long msgId;
    private byte channel;
    private short objId;
    private short procId;
    private Object[] args;

    public RpcCallMessage() {
    }
    
    public RpcCallMessage( long msgId, byte channel, short objId, short procId, Object... args ) {
        this.msgId = msgId;
        this.channel = channel;
        this.objId = objId;
        this.procId = procId;
        this.args = args;
    }
 
    public long getMessageId() {
        return msgId;
    }
    
    public byte getChannel() {
        return channel;
    }
 
    public boolean isAsync() {
        return msgId == -1;
    }
 
    public short getObjectId() {
        return objId;
    }
    
    public short getProcedureId() {
        return procId;
    }
    
    public Object[] getArguments() {
        return args;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[#" + msgId + ", channel=" + channel
                                          + (isAsync() ? ", async" : ", sync") 
                                          + ", objId=" + objId 
                                          + ", procId=" + procId 
                                          + ", args.length=" + (args == null ? 0 : args.length) 
                                          + "]";
    }
}
