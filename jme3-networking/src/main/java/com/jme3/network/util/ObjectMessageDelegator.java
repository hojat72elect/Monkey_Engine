

package com.jme3.network.util;

import com.jme3.network.MessageConnection;


/**
 *  A MessageListener implementation that will forward messages to methods
 *  of a specified delegate object.  These methods can be automapped or manually
 *  specified.  
 *
 *  @author    Paul Speed
 */
public class ObjectMessageDelegator<S extends MessageConnection> extends AbstractMessageDelegator<S> {
 
    private Object delegate;
    
    /**
     *  Creates a MessageListener that will forward mapped message types
     *  to methods of the specified object.
     *  If automap is true then all methods with the proper signature will
     *  be mapped.
     *  <p>Methods of the following signatures are allowed:
     *  <ul>
     *  <li>void someName(S conn, SomeMessage msg)
     *  <li>void someName(Message msg)
     *  </ul>
     *  Where S is the type of MessageConnection and SomeMessage is some
     *  specific concrete Message subclass.
     */   
    public ObjectMessageDelegator( Object delegate, boolean automap ) {
        super(delegate.getClass(), automap);
        this.delegate = delegate;
    }
 
    @Override
    protected Object getSourceDelegate( MessageConnection source ) {
        return delegate;
    }
}

