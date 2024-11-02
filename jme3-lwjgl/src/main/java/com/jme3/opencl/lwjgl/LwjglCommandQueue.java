
package com.jme3.opencl.lwjgl;

import com.jme3.opencl.CommandQueue;
import com.jme3.opencl.Device;
import org.lwjgl.opencl.CL10;
import org.lwjgl.opencl.CLCommandQueue;

/**
 *
 * @author shaman
 */
public class LwjglCommandQueue extends CommandQueue {

    private final CLCommandQueue queue;

    public LwjglCommandQueue(CLCommandQueue queue, Device device) {
        super(new ReleaserImpl(queue), device);
        this.queue = queue;
    }
    
    public CLCommandQueue getQueue() {
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
        private CLCommandQueue queue;
        private ReleaserImpl(CLCommandQueue queue) {
            this.queue = queue;
        }
        @Override
        public void release() {
            if (queue != null) {
                int ret = CL10.clReleaseCommandQueue(queue);
                queue = null;
                Utils.reportError(ret, "clReleaseCommandQueue");
            }
        }
    }
}
