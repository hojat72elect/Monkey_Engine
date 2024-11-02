

package com.jme3.app;

import android.os.Build;
import com.jme3.profile.*;

import static com.jme3.profile.AppStep.BeginFrame;
import static com.jme3.profile.AppStep.EndFrame;
import static com.jme3.profile.AppStep.ProcessAudio;
import static com.jme3.profile.AppStep.ProcessInput;
import static com.jme3.profile.AppStep.QueuedTasks;
import static com.jme3.profile.AppStep.RenderFrame;
import static com.jme3.profile.AppStep.RenderMainViewPorts;
import static com.jme3.profile.AppStep.RenderPostViewPorts;
import static com.jme3.profile.AppStep.RenderPreviewViewPorts;
import static com.jme3.profile.AppStep.SpatialUpdate;
import static com.jme3.profile.AppStep.StateManagerRender;
import static com.jme3.profile.AppStep.StateManagerUpdate;
import static com.jme3.profile.VpStep.BeginRender;
import static com.jme3.profile.VpStep.EndRender;
import static com.jme3.profile.VpStep.FlushQueue;
import static com.jme3.profile.VpStep.PostFrame;
import static com.jme3.profile.VpStep.PostQueue;
import static com.jme3.profile.VpStep.RenderScene;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;

/**
 *  An AppProfiler implementation that integrates the
 *  per-frame application-wide timings for update versus
 *  render into the Android systrace utility.
 *
 *  <p>This profiler uses the Android Trace class which is only supported
 *  on Android SDK rev 18 and higher (ver 4.3 and higher).  If the
 *  device is running a version less than rev 18, the logging will
 *  be skipped.</p>
 *
 *  <p>In the MainActivity class, add the following:</p>
 *  <pre><code>
 *  {@literal @}Override
 *  public void onCreate(Bundle savedInstanceState) {
 *      super.onCreate(savedInstanceState);
 *      app.setAppProfiler(new DefaultAndroidProfiler());
 *  }
 *  </code></pre>
 *  Start the Android systrace utility and run the application to
 *  see the detailed timings of the application.
 *
 * @author iwgeric
 */
public class DefaultAndroidProfiler implements AppProfiler {
    private int androidApiLevel = Build.VERSION.SDK_INT;

    @Override
    public void appStep(AppStep appStep) {
        if (androidApiLevel >= 18) {
            switch(appStep) {
                case BeginFrame:
                    android.os.Trace.beginSection("Frame");
                    break;
                case QueuedTasks:
                    android.os.Trace.beginSection("QueuedTask");
                    break;
                case ProcessInput:
                    android.os.Trace.endSection();
                    android.os.Trace.beginSection("ProcessInput");
                    break;
                case ProcessAudio:
                    android.os.Trace.endSection();
                    android.os.Trace.beginSection("ProcessAudio");
                    break;
                case StateManagerUpdate:
                    android.os.Trace.endSection();
                    android.os.Trace.beginSection("StateManagerUpdate");
                    break;
                case SpatialUpdate:
                    android.os.Trace.endSection();
                    android.os.Trace.beginSection("SpatialUpdate");
                    break;
                case StateManagerRender:
                    android.os.Trace.endSection();
                    android.os.Trace.beginSection("StateManagerRender");
                    break;
                case RenderFrame:
                    android.os.Trace.endSection();
                    android.os.Trace.beginSection("RenderFrame");
                    break;
                case RenderPreviewViewPorts:
                    android.os.Trace.beginSection("RenderPreviewViewPorts");
                    break;
                case RenderMainViewPorts:
                    android.os.Trace.endSection();
                    android.os.Trace.beginSection("RenderMainViewPorts");
                    break;
                case RenderPostViewPorts:
                    android.os.Trace.endSection();
                    android.os.Trace.beginSection("RenderPostViewPorts");
                    break;
                case EndFrame:
                    android.os.Trace.endSection();
                    android.os.Trace.endSection();
                    android.os.Trace.endSection();
                    break;
            }
        }
    }

    @Override
    public void appSubStep(String... additionalInfo) {

    }

    @Override
    public void vpStep(VpStep vpStep, ViewPort vp, RenderQueue.Bucket bucket) {
        if (androidApiLevel >= 18) {
            switch (vpStep) {
                case BeginRender:
                    android.os.Trace.beginSection("Render: " + vp.getName());
                    break;
                case RenderScene:
                    android.os.Trace.beginSection("RenderScene: " + vp.getName());
                    break;
                case PostQueue:
                    android.os.Trace.endSection();
                    android.os.Trace.beginSection("PostQueue: " + vp.getName());
                    break;
                case FlushQueue:
                    android.os.Trace.endSection();
                    android.os.Trace.beginSection("FlushQueue: " + vp.getName());
                    break;
                case PostFrame:
                    android.os.Trace.endSection();
                    android.os.Trace.beginSection("PostFrame: " + vp.getName());
                    break;
                case EndRender:
                    android.os.Trace.endSection();
                    android.os.Trace.endSection();
                    break;
            }
        }
    }

    @Override
    public void spStep(SpStep step, String... additionalInfo) {

    }

}
