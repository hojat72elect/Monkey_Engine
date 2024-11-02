
package com.jme3.network.base;

import java.nio.ByteBuffer;
import com.jme3.network.Message;

/**
 *  Consolidates the conversion of messages to/from byte buffers
 *  and provides a rolling message buffer.  ByteBuffers can be
 *  pushed in and messages will be extracted, accumulated, and 
 *  available for retrieval.  The MessageBuffers returned are generally
 *  not thread safe and are meant to be used within a single message 
 *  processing thread.  MessageProtocol implementations themselves should
 *  be thread safe.
 *
 *  <p>The specific serialization protocol used is up to the implementation.</p>
 *
 *  @author    Paul Speed
 */ 
public interface MessageProtocol {
    public ByteBuffer toByteBuffer( Message message, ByteBuffer target );
    public Message toMessage( ByteBuffer bytes );
    public MessageBuffer createBuffer();
}



