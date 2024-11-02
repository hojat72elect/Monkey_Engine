
package com.jme3.network;


/**
 *  Notified when errors happen on a connection.
 *
 *  @version   $Revision$
 *  @author    Paul Speed
 */
public interface ErrorListener<S>
{
    public void handleError( S source, Throwable t );
}
