
package com.jme3.network;


/**
 *  The source of a received message and the common abstract interface
 *  of client-&gt;server and server-&gt;client objects. 
 *
 *  @version   $Revision$
 *  @author    Paul Speed
 */
public interface MessageConnection
{
    /**
     *  Indicates the default reliable channel that is used
     *  when calling the channel-less send() with a reliable
     *  message.  This channel number can be used in the send(channel, msg)
     *  version of send.
     *
     *  <p>Normally, callers should just call the regular non-channel
     *  send message but these channel numbers are useful for extensions
     *  that allow the user to specify a channel and want to still
     *  support the default channels.</p>
     */
    public static final int CHANNEL_DEFAULT_RELIABLE = -2;
    
    /**
     *  Indicates the default unreliable channel that is used
     *  when calling the channel-less send() with a reliable=false
     *  message.  This channel number can be used in the send(channel, msg)
     *  version of send.
     *
     *  <p>Normally, callers should just call the regular non-channel
     *  send message but these channel numbers are useful for extensions
     *  that allow the user to specify a channel and want to still
     *  support the default channels.</p>
     */
    public static final int CHANNEL_DEFAULT_UNRELIABLE = -1;

    /**
     *  Sends a message to the other end of the connection.
     */   
    public void send( Message message );
    
    /**
     *  Sends a message to the other end of the connection using
     *  the specified alternate channel.
     */   
    public void send( int channel, Message message );
}    

