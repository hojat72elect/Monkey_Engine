
package com.jme3.network.kernel.tcp;

import com.jme3.network.kernel.Endpoint;
import com.jme3.network.kernel.Kernel;
import com.jme3.network.kernel.KernelException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 *  Endpoint implementation that encapsulates the
 *  channel IO based connection information and keeps
 *  track of the outbound data queue for the channel.
 *
 *  @version   $Revision$
 *  @author    Paul Speed
 */
public class NioEndpoint implements Endpoint
{
    protected static final ByteBuffer CLOSE_MARKER = ByteBuffer.allocate(0);

    private long id;
    private SocketChannel socket;
    private SelectorKernel kernel;
    private ConcurrentLinkedQueue<ByteBuffer> outbound = new ConcurrentLinkedQueue<>();
    private boolean closing = false;

    public NioEndpoint( SelectorKernel kernel, long id, SocketChannel socket )
    {
        this.id = id;
        this.socket = socket;
        this.kernel = kernel;
    }

    @Override
    public Kernel getKernel()
    {
        return kernel;
    }

    @Override
    public void close()
    {
        close(false);
    }

    @Override
    public void close( boolean flushData )
    {
        if( flushData ) {
            closing = true;
            
            // Enqueue a close marker message to let the server
            // know we should close
            send( CLOSE_MARKER, false, true );
            
            return;
        }
    
        try {
            // Note: even though we may be disconnected from the socket.isConnected()
            // standpoint, it's still safest to tell the kernel so that it can be sure
            // to stop managing us gracefully.
            kernel.closeEndpoint(this);
        } catch( IOException e ) {
            throw new KernelException( "Error closing endpoint for socket:" + socket, e );
        }
    }

    @Override
    public long getId()
    {
        return id;
    }

    @Override
    public String getAddress()
    {
        return String.valueOf(socket.socket().getRemoteSocketAddress()); 
    }     

    @Override
    public boolean isConnected()
    {
        return socket.isConnected();
    }

    /**
     *  The wakeup option is used internally when the kernel is
     *  broadcasting out to a bunch of endpoints and doesn't want to
     *  necessarily wakeup right away.
     */
    protected void send( ByteBuffer data, boolean copy, boolean wakeup )
    {
        // We create a ByteBuffer per endpoint since we
        // use it to track the data sent to each endpoint
        // separately.
        ByteBuffer buffer;
        if( !copy ) {
            buffer = data;
        } else {
            // Copy the buffer
            buffer = ByteBuffer.allocate(data.remaining());
            buffer.put(data);
            buffer.flip();
        }

        // Queue it up
        outbound.add(buffer);

        if( wakeup )
            kernel.wakeupSelector();
    }

    /**
     *  Called by the SelectorKernel to get the current top
     *  buffer for writing.
     */
    protected ByteBuffer peekPending()
    {
        return outbound.peek();
    }

    /**
     *  Called by the SelectorKernel when the top buffer
     *  has been exhausted.
     */
    protected ByteBuffer removePending()
    {
        return outbound.poll();
    }

    protected boolean hasPending()
    {
        return !outbound.isEmpty();
    }

    @Override
    public void send( ByteBuffer data )
    {   
        if( data == null ) {
            throw new IllegalArgumentException( "Data cannot be null." );
        }
        if( closing ) {
            throw new KernelException( "Endpoint has been closed:" + socket );
        }
        send( data, true, true );
    }

    @Override
    public String toString()
    {
        return "NioEndpoint[" + id + ", " + socket + "]";
    }
}
