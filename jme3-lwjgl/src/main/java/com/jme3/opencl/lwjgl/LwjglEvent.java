
package com.jme3.opencl.lwjgl;

import com.jme3.opencl.Event;
import java.util.logging.Logger;
import org.lwjgl.opencl.CL10;
import org.lwjgl.opencl.CLEvent;

/**
 *
 * @author shaman
 */
public class LwjglEvent extends Event {
    private static final Logger LOG = Logger.getLogger(LwjglEvent.class.getName());
    private CLEvent event;

    public LwjglEvent(CLEvent event) {
        super(new ReleaserImpl(event));
        this.event = event;
    }

    public CLEvent getEvent() {
        return event;
    }

    @Override
    public void waitForFinished() {
        if (event==null) {
            return;
        }
        CL10.clWaitForEvents(event);
        release(); // shortcut to save resources
    }

    @Override
    public boolean isCompleted() {
        if (event==null) {
            return true;
        }
        int status = event.getInfoInt(CL10.CL_EVENT_COMMAND_EXECUTION_STATUS);
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
        private CLEvent event;

        private ReleaserImpl(CLEvent event) {
            this.event = event;
        }
        
        @Override
        public void release() {
            if (event != null && event.isValid()) {
                int ret = CL10.clReleaseEvent(event);
                event = null;
                Utils.reportError(ret, "clReleaseEvent");
                LOG.finer("Event deleted");
            }
        }
        
    }
}
