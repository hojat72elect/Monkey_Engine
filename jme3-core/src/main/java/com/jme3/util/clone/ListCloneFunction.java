

package com.jme3.util.clone;

import java.util.List;

/**
 *  A CloneFunction implementation that deep clones a list by
 *  creating a new list and cloning its values using the cloner.
 *
 *  @author    Paul Speed
 */
public class ListCloneFunction<T extends List> implements CloneFunction<T> {

    @Override
    public T cloneObject(Cloner cloner, T object) {
        try {
            T clone = cloner.javaClone(object);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new IllegalArgumentException("Clone not supported for type:" + object.getClass(), e);
        }
    }

    /**
     *  Clones the elements of the list.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void cloneFields(Cloner cloner, T clone, T object) {
        for (int i = 0; i < clone.size(); i++) {
            // Need to clone the clones... because T might
            // have done something special in its clone method that
            // we will have to adhere to.  For example, clone may have nulled
            // out some things or whatever that might be implementation specific.
            // At any rate, if it's a proper clone then the clone will already
            // have shallow versions of the elements that we can clone.
            clone.set(i, cloner.clone(clone.get(i)));
        }
    }
}

