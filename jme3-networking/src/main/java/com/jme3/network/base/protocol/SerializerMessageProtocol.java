
package com.jme3.network.base.protocol;

import java.io.IOException;
import java.nio.ByteBuffer;
import com.jme3.network.Message;
import com.jme3.network.base.MessageBuffer;
import com.jme3.network.base.MessageProtocol;
import com.jme3.network.serializing.Serializer;

/**
 *  Implements a MessageProtocol providing message serializer/deserialization
 *  based on the built-in Serializer code. 
 *
 *  <p>The protocol is based on a simple length + data format
 *  where two bytes represent the (short) length of the data
 *  and the rest is the raw data for the Serializers class.</p>
 *
 *  @version   $Revision$
 *  @author    Paul Speed
 */ 
public class SerializerMessageProtocol implements MessageProtocol {
 
    public SerializerMessageProtocol() {
    }
 
    /**
     *  Converts a message to a ByteBuffer using the com.jme3.network.serializing.Serializer
     *  and the (short length) + data protocol.  If target is null
     *  then a 32k byte buffer will be created and filled.
     */
    @Override
    public ByteBuffer toByteBuffer( Message message, ByteBuffer target ) {
    
        // Could let the caller pass their own in       
        ByteBuffer buffer = target == null ? ByteBuffer.allocate(32767 + 2) : target;
        
        try {
            buffer.position(2);
            Serializer.writeClassAndObject(buffer, message);
            buffer.flip();
            short dataLength = (short)(buffer.remaining() - 2);
            buffer.putShort(dataLength);
            buffer.position(0);
            
            return buffer;
        } catch( IOException e ) {
            throw new RuntimeException("Error serializing message", e);
        }
    }

    /**
     *  Creates and returns a message from the properly sized byte buffer
     *  using com.jme3.network.serializing.Serializer.
     */   
    @Override
    public Message toMessage( ByteBuffer bytes ) {
        try {
            return (Message)Serializer.readClassAndObject(bytes);
        } catch( IOException e ) {
            throw new RuntimeException("Error deserializing object, class ID:" + bytes.getShort(0), e);   
        }         
    }
      
    @Override
    public MessageBuffer createBuffer() {
        // Defaulting to LazyMessageBuffer
        return new LazyMessageBuffer(this);
    }
     
}



