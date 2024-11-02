
package com.jme3.network;


/**
 *  Determines a true or false value for a given input. 
 *
 *  @version   $Revision$
 *  @author    Paul Speed
 */
public interface Filter<T>
{
    /**
     *  Returns true if the specified input is accepted by this
     *  filter. 
     */
    public boolean apply( T input ); 
}


