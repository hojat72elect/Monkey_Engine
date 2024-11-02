
package com.jme3.network.kernel;


/**
 *  Represents a client-side connection error, usually encapsulating
 *  an IOException as its cause.
 *
 *  @version   $Revision$
 *  @author    Paul Speed
 */
public class ConnectorException extends RuntimeException
{
    public ConnectorException( String message, Throwable cause )
    {
        super( message, cause );
    }
    
    public ConnectorException( String message )
    {
        super( message );
    }
}
