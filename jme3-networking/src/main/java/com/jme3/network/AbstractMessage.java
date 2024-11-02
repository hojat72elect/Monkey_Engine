
package com.jme3.network;

import com.jme3.network.serializing.Serializable;

/**
 *  Interface implemented by all network messages.
 *
 *  @version   $Revision$
 *  @author    Paul Speed
 */
@Serializable()
public abstract class AbstractMessage implements Message
{
    private transient boolean reliable = true;

    protected AbstractMessage()
    {
    }

    protected AbstractMessage( boolean reliable )
    {
        this.reliable = reliable; 
    }
    
    /**
     *  Sets this message to 'reliable' or not and returns this
     *  message.
     */
    @Override
    public Message setReliable(boolean f)
    {
        this.reliable = f;
        return this;
    }
    
    /**
     *  Indicates which way an outgoing message should be sent
     *  or which way an incoming message was sent.
     */
    @Override
    public boolean isReliable()
    {
        return reliable;
    }
}
