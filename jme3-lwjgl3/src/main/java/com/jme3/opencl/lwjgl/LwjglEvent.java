
package com.jme3.opencl.lwjgl;

import com.jme3.opencl.Event;
import com.jme3.opencl.lwjgl.info.Info;
import java.util.logging.Logger;
import org.lwjgl.opencl.CL10;

/**
 *
 * @author shaman
 */
public class LwjglEvent extends Event {
    private static final Logger LOG = Logger.getLogger(LwjglEvent.class.getName());
    private long event;

    public LwjglEvent(long event) {
        super(new ReleaserImpl(event));
        this.event = event;
    }

    public long getEvent() {
        return event;
    }

    @Override
    public void waitForFinished() {
        CL10.clWaitForEvents(event);
        release(); // shortcut to save resources
    }

    @Override
    public boolean isCompleted() {
        int status = Info.clGetEventInfoInt(event, CL10.CL_EVENT_COMMAND_EXECUTION_STATUS);
        if (status == CL10.CL_SUCCESS) {
            release(); // shortcut to save resources
            return true;
        } else if (status < 0) {
            Utils.checkError(status, "EventStatus");
            return false;
        } else {
            return false;
        }
    }

    private static class ReleaserImpl implements ObjectReleaser {
        private long event;

        private ReleaserImpl(long event) {
            this.event = event;
        }
        
        @Override
        public void release() {
            if (event != 0) {
                int ret = CL10.clReleaseEvent(event);
                event = 0;
                Utils.reportError(ret, "clReleaseEvent");
                LOG.finer("Event deleted");
            }
        }
        
    }
}
