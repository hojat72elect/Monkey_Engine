
package com.jme3.opencl.lwjgl;

import com.jme3.opencl.CommandQueue;
import com.jme3.opencl.Device;
import org.lwjgl.opencl.CL10;

/**
 *
 * @author shaman
 */
public class LwjglCommandQueue extends CommandQueue {

    private final long queue;

    public LwjglCommandQueue(long queue, Device device) {
        super(new ReleaserImpl(queue), device);
        this.queue = queue;
    }
    
    public long getQueue() {
        return queue;
    }
    
    @Override
    public void flush() {
        int ret = CL10.clFlush(queue);
        Utils.checkError(ret, "clFlush");
    }

    @Override
    public void finish() {
        int ret = CL10.clFinish(queue);
        Utils.checkError(ret, "clFinish");
    }
    
    private static class ReleaserImpl implements ObjectReleaser {
        private long queue;
        private ReleaserImpl(long queue) {
            this.queue = queue;
        }
        @Override
        public void release() {
            if (queue != 0) {
                int ret = CL10.clReleaseCommandQueue(queue);
                queue = 0;
                Utils.reportError(ret, "clReleaseCommandQueue");
            }
        }
    }
}
