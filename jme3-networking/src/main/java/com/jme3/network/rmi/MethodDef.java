
package com.jme3.network.rmi;


/**
 * Method definition is used to map methods on an RMI interface
 * to an implementation on a remote machine.
 *
 * @author Kirill Vainer
 */
public class MethodDef {

    /**
     * Method name
     */
    public String name;

    /**
     * Return type
     */
    public Class<?> retType;

    /**
     * Parameter types
     */
    public Class<?>[] paramTypes;
}
