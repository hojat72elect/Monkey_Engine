
 
package com.jme3.network.base.protocol;

import java.nio.ByteBuffer;
import java.util.LinkedList;

import com.jme3.network.Message;
import com.jme3.network.base.MessageBuffer;
import com.jme3.network.base.MessageProtocol;


/**
 *  A MessageBuffer implementation that will aggressively create
 *  all messages as byte data comes in.  In other words, if there
 *  are four messages in the ByteBuffer passed to addBuffer() then
 *  all of the messages will be deserialized during that call and
 *  queued up for later return.  The downside is that, if any of
 *  those messages was going to alter the MessageProtocol serialization
 *  behavior in a way that affects later messages, then problems occur
 *  when those messages are all in one block.
 *
 *  @author    Paul Speed
 */
public class GreedyMessageBuffer implements MessageBuffer {

    private MessageProtocol protocol;
    private final LinkedList<Message> messages = new LinkedList<>();
    private ByteBuffer current;
    private int size;
    private Byte carry;
    
    public GreedyMessageBuffer( MessageProtocol protocol ) {
        this.protocol = protocol;
    }
    
    /**
     *  Returns the next message in the buffer or null if there are no more
     *  messages in the buffer.  
     */
    @Override
    public Message pollMessage() {
        if( messages.isEmpty() ) {
            return null;
        }                
        return messages.removeFirst();
    }
    
    /**
     *  Returns true if there is a message waiting in the buffer.
     */
    @Override
    public boolean hasMessages() {
        return !messages.isEmpty();
    }
 
    /**
     *  Adds byte data to the message buffer.  Returns true if there is
     *  a message waiting after this call.
     */   
    @Override
    public boolean addBytes( ByteBuffer buffer ) {
        // push the data from the buffer into as
        // many messages as we can
        while( buffer.remaining() > 0 ) {

            if( current == null ) {

                // If we have a left-over carry,
                // extra processing is needed to get the short value.
                if( carry != null ) {
                    byte high = carry;
                    byte low = buffer.get();
                    
                    size = (high & 0xff) << 8 | (low & 0xff);
                    carry = null;
                }
                else if( buffer.remaining() < 2 ) {
                    // It's possible that the supplied buffer only has one
                    // byte in it... and in that case we will get an underflow
                    // when attempting to read the short below.
                    
                    // It has to be 1, or we'd never get here... but one
                    // isn't enough, so we stash it away.
                    carry = buffer.get();
                    break;
                } else {
                    // We are not currently reading an object so
                    // grab the size.
                    // Note: this is somewhat limiting... int would
                    // be better.
                    size = buffer.getShort();
                }               
 
                // Allocate the buffer into which we'll feed the
                // data as we get it               
                current = ByteBuffer.allocate(size);
            } 

            if( current.remaining() <= buffer.remaining() ) {
                // We have at least one complete object so
                // copy what we can into current, create a message,
                // and then continue pulling from buffer.
                    
                // Artificially set the limit so we don't overflow
                int extra = buffer.remaining() - current.remaining();
                buffer.limit(buffer.position() + current.remaining());
 
                // Now copy the data                   
                current.put(buffer);
                current.flip();
                    
                // Now set the limit back to a good value
                buffer.limit(buffer.position() + extra);
 
                messages.add(protocol.toMessage(current));
 
                current = null;                    
            } else {                
                // Not yet a complete object so just copy what we have
                current.put(buffer); 
            }            
        }            
        
        return hasMessages();        
    }
}


