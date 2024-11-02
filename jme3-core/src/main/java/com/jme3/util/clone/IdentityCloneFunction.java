

package com.jme3.util.clone;


/**
 *  A CloneFunction implementation that simply returns
 *  the passed object without cloning it.  This is useful for
 *  forcing some object types (like Meshes) to be shared between
 *  the original and cloned object graph.
 *
 *  @author    Paul Speed
 */
public class IdentityCloneFunction<T> implements CloneFunction<T> {

    /**
     *  Returns the object directly.
     */
    @Override
    public T cloneObject(Cloner cloner, T object) {
        return object;
    }

    /**
     *  Does nothing.
     */
    @Override
    public void cloneFields(Cloner cloner, T clone, T object) {
    }
}
