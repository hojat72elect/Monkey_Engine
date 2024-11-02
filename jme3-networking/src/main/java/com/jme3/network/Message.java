
package com.jme3.network;

import com.jme3.network.serializing.Serializable;

/**
 *  Interface implemented by all network messages.
 *
 *  @version   $Revision$
 *  @author    Paul Speed
 */
@Serializable()
public interface Message
{
    /**
     *  Sets this message to 'reliable' or not and returns this
     *  message.
     */
    public Message setReliable(boolean f);
    
    /**
     *  Indicates which way an outgoing message should be sent
     *  or which way an incoming message was sent.
     */
    public boolean isReliable();
}
