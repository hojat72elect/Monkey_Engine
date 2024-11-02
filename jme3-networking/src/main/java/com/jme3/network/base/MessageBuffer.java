
 
package com.jme3.network.base;

import java.nio.ByteBuffer;

import com.jme3.network.Message;

/**
 *  Accumulates network data into Message objects.  This allows
 *  random chunks of bytes to be assembled into messages even if
 *  the buffer boundaries don't line up.
 *
 *  @author    Paul Speed
 */
public interface MessageBuffer {

    /**
     *  Returns the next message in the buffer or null if there are no more
     *  messages in the buffer.  
     */
    public Message pollMessage();
    
    /**
     *  Returns true if there is a message waiting in the buffer.
     */
    public boolean hasMessages();
 
    /**
     *  Adds byte data to the message buffer.  Returns true if there is
     *  a message waiting after this call.
     */   
    public boolean addBytes( ByteBuffer buffer );
}

