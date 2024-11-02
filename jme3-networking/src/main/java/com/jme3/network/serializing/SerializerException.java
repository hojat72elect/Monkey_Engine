
package com.jme3.network.serializing;

import java.io.IOException;

/**
 *  A general exception from the serialization routines.
 *
 *  @version   $Revision$
 *  @author    Paul Speed
 */
public class SerializerException extends IOException
{
    public SerializerException( String msg, Throwable cause )
    {
        super( msg );
        initCause(cause);
    }

    public SerializerException( String msg )
    {
        super( msg );
    }      
}
