
package com.jme3.opencl;

/**
 * Abstract implementation of {@link OpenCLObject} providing the release 
 * mechanisms.
 * @author Sebastian Weiss
 */
public abstract class AbstractOpenCLObject implements OpenCLObject {
    
    protected final ObjectReleaser releaser;
    protected AbstractOpenCLObject(ObjectReleaser releaser) {
        this.releaser = releaser;
    }
    @Override
    public AbstractOpenCLObject register() {
        OpenCLObjectManager.getInstance().registerObject(this);
        return this;
    }
    @Override
    public void release() {
        releaser.release();
    }
    @Override
    @SuppressWarnings("FinalizeDeclaration")
    protected void finalize() throws Throwable {
        release();
    }
    @Override
    public ObjectReleaser getReleaser() {
        return releaser;
    }
}
