

package com.jme3.util.clone;


/**
 *  Provides custom cloning for a particular object type.  Once
 *  registered with the Cloner, this function object will be called twice
 *  for any cloned object that matches the class for which it was registered.
 *  It will first call cloneObject() to shallow clone the object and then call
 *  cloneFields()  to deep clone the object's values.
 *
 *  <p>This two step process is important because this is what allows
 *  circular references in the cloned object graph.</p>
 *
 *  @author    Paul Speed
 */
public interface CloneFunction<T> {

    /**
     *  Performs a shallow clone of the specified object.  This is similar
     *  to the JmeCloneable.clone() method in semantics and is the first part
     *  of a two part cloning process.  Once the shallow clone is created, it
     *  is cached and CloneFunction.cloneFields() is called.  In this way,
     *  the CloneFunction interface can completely take over the JmeCloneable
     *  style cloning for an object that doesn't otherwise implement that interface.
     *
     *  @param cloner The cloner performing the cloning operation.
     *  @param original The original object that needs to be cloned.
     *  @return a new instance
     */
    public T cloneObject(Cloner cloner, T original);


    /**
     *  Performs a deep clone of the specified clone's fields.  This is similar
     *  to the JmeCloneable.cloneFields() method in semantics and is the second part
     *  of a two part cloning process.  Once the shallow clone is created, it
     *  is cached and CloneFunction.cloneFields() is called.  In this way,
     *  the CloneFunction interface can completely take over the JmeCloneable
     *  style cloning for an object that doesn't otherwise implement that interface.
     *
     *  @param cloner The cloner performing the cloning operation.
     *  @param clone The clone previously returned from cloneObject().
     *  @param original The original object that was cloned.  This is provided for
     *       the very special case where field cloning needs to refer to
     *       the original object.  Mostly the necessary fields should already
     *       be on the clone.
     */
    public void cloneFields(Cloner cloner, T clone, T original);
}
