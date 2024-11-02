
package com.jme3.network.base;

import com.jme3.network.kernel.Connector;
import java.io.IOException;


/**
 *  Creates Connectors for a specific host.
 *
 *  @version   $Revision$
 *  @author    Paul Speed
 */
public interface ConnectorFactory
{
    public Connector createConnector( int channel, int port ) throws IOException;
}
