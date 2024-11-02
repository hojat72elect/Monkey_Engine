
package com.jme3.network.kernel.udp;

import com.jme3.network.kernel.Endpoint;
import com.jme3.network.kernel.Kernel;
import com.jme3.network.kernel.KernelException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;


/**
 *  Endpoint implementation that encapsulates the
 *  UDP connection information for return messaging,
 *  identification of envelope sources, etc.
 *
 *  @version   $Revision$
 *  @author    Paul Speed
 */
public class UdpEndpoint implements Endpoint
{
    private long id;    
    private SocketAddress address;
    private DatagramSocket socket;
    private UdpKernel kernel;
    private boolean connected = true; // it's connectionless but we track logical state

    public UdpEndpoint( UdpKernel kernel, long id, SocketAddress address, DatagramSocket socket )
    {
        this.id = id;
        this.address = address;
        this.socket = socket;
        this.kernel = kernel;
    }

    @Override
    public Kernel getKernel()
    {
        return kernel;
    }

    protected SocketAddress getRemoteAddress()
    {
        return address;
    }

    @Override
    public void close()
    {
        close( false );
    }

    @Override
    public void close( boolean flush )
    {
        // No real reason to flush UDP traffic yet... especially
        // when considering that the outbound UDP isn't even
        // queued.
    
        try {
            kernel.closeEndpoint(this);
            connected = false;
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
        return String.valueOf(address); 
    }     

    @Override
    public boolean isConnected()
    {
        // The socket is always unconnected anyway, so we track our
        // own logical state for the kernel's benefit.
        return connected;
    }

    @Override
    public void send( ByteBuffer data )
    {
        if( !isConnected() ) {
            throw new KernelException( "Endpoint is not connected:" + this );
        }
        
        
        try {
            DatagramPacket p = new DatagramPacket( data.array(), data.position(), 
                                                   data.remaining(), address );
                                                   
            // Just queue it up for the kernel threads to write
            // out
            kernel.enqueueWrite( this, p );
                                                               
            //socket.send(p);
        } catch (Exception e) {
            if (e instanceof SocketException) {
                throw new KernelException("Error sending datagram to:" + address, e);
            } else if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String toString()
    {
        return "UdpEndpoint[" + id + ", " + address + "]";
    }
}
