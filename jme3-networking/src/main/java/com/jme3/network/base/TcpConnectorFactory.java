
package com.jme3.network.base;

import com.jme3.network.kernel.Connector;
import com.jme3.network.kernel.tcp.SocketConnector;
import java.io.IOException;
import java.net.InetAddress;


/**
 *  Creates TCP connectors to a specific remote address.  
 *
 *  @version   $Revision$
 *  @author    Paul Speed
 */
public class TcpConnectorFactory implements ConnectorFactory
{
    private InetAddress remoteAddress;
    
    public TcpConnectorFactory( InetAddress remoteAddress )
    {
        this.remoteAddress = remoteAddress;
    }

    @Override
    public Connector createConnector( int channel, int port ) throws IOException
    {
        return new SocketConnector( remoteAddress, port );        
    }    
}
