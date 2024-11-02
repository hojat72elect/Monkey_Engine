
package com.jme3.opencl;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shaman
 */
public class OpenCLObjectManager {
    private static final Logger LOG = Logger.getLogger(OpenCLObjectManager.class.getName());
    private static final Level LOG_LEVEL1 = Level.FINEST;
    private static final Level LOG_LEVEL2 = Level.FINER;
    
    private static final OpenCLObjectManager INSTANCE = new OpenCLObjectManager();
    private OpenCLObjectManager() {}
    
    public static OpenCLObjectManager getInstance() {
        return INSTANCE;
    }
    
    private final ReferenceQueue<Object> refQueue = new ReferenceQueue<>();
    private final HashSet<OpenCLObjectRef> activeObjects = new HashSet<>();
    
    private static class OpenCLObjectRef extends PhantomReference<Object> {
        
        final private OpenCLObject.ObjectReleaser releaser;

        public OpenCLObjectRef(ReferenceQueue<Object> refQueue, OpenCLObject obj){
            super(obj, refQueue);
            releaser = obj.getReleaser();
        }
    }
    
    public void registerObject(OpenCLObject obj) {
        OpenCLObjectRef ref = new OpenCLObjectRef(refQueue, obj);
        activeObjects.add(ref);
        LOG.log(LOG_LEVEL1, "registered OpenCL object: {0}", obj);
    }
    
    private void deleteObject(OpenCLObjectRef ref) {
        LOG.log(LOG_LEVEL1, "deleting OpenCL object by: {0}", ref.releaser);
        ref.releaser.release();
        ref.clear();
        activeObjects.remove(ref);
    }
        
    public void deleteUnusedObjects() {
        if (activeObjects.isEmpty()) {
            LOG.log(LOG_LEVEL2, "no active natives");
            return; //nothing to do
        }
        
        int removed = 0;
        while (true) {
            // Remove objects reclaimed by GC.
            OpenCLObjectRef ref = (OpenCLObjectRef) refQueue.poll();
            if (ref == null) {
                break;
            }
            deleteObject(ref);
            removed++;
        }
        if (removed >= 1) {
            LOG.log(LOG_LEVEL2, "{0} native objects were removed from native", removed);
        }
    }
    
    public void deleteAllObjects() {
        for (OpenCLObjectRef ref : activeObjects) {
            LOG.log(LOG_LEVEL1, "deleting OpenCL object by: {0}", ref.releaser);
            ref.releaser.release();
            ref.clear();
        }
        activeObjects.clear();
    }
}
