
package jme3test.opencl;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.opencl.*;
import com.jme3.system.AppSettings;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class creates multiple instances of {@link TestVertexBufferSharing}.
 * This is used to test if multiple opencl instances can run in parallel.
 * @author Sebastian Weiss
 */
public class TestMultipleApplications extends SimpleApplication {
    private static final Logger LOG = Logger.getLogger(TestMultipleApplications.class.getName());
    
    private static final Object sync = new Object();
    private static List<? extends Device> availableDevices;
    private static int currentDeviceIndex;
    
    private CommandQueue clQueue;
    private Kernel kernel;
    private Buffer buffer;
    private boolean failed;
    private BitmapText statusText;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final AppSettings settings = new AppSettings(true);
        settings.setOpenCLSupport(true);
        settings.setVSync(true);
        settings.setOpenCLPlatformChooser(CustomPlatformChooser.class);
        settings.setRenderer(AppSettings.LWJGL_OPENGL2);
        for (int i=0; i<2; ++i) {
            new Thread() {
                @Override
                public void run() {
                    if (currentDeviceIndex == -1) {
                        return;
                    }
                    TestMultipleApplications app = new TestMultipleApplications();
                    app.setSettings(settings);
                    app.setShowSettings(false);
                    app.start();
                }
            }.start();
        }
    }
    
    public static class CustomPlatformChooser implements PlatformChooser {

        public CustomPlatformChooser() {}
        
        @Override
        public List<? extends Device> chooseDevices(List<? extends Platform> platforms) {
            synchronized(sync) {
            if (currentDeviceIndex == -1) {
                return Collections.emptyList();
            }

            Platform platform = platforms.get(0);
            availableDevices = platform.getDevices();
            
            Device device = platform.getDevices().get(currentDeviceIndex);
            currentDeviceIndex ++;
            if (currentDeviceIndex >= availableDevices.size()) {
                currentDeviceIndex = -1;
            }
            
            return Collections.singletonList(device);
            }
        }
        
    }
    
    @Override
    public void simpleInitApp() {
        Context clContext = context.getOpenCLContext();
        if (clContext == null) {
            LOG.severe("No OpenCL context found");
            stop();
            return;
        }
        Device device = clContext.getDevices().get(0);
        clQueue = clContext.createQueue(device);
        clQueue.register();
        
        String source = ""
                + "__kernel void Fill(__global float* vb, float v)\n"
                + "{\n"
                + "  int idx = get_global_id(0);\n"
                + "  vb[idx] = v;\n"
                + "}\n";
        Program program = clContext.createProgramFromSourceCode(source);
        program.build();
        program.register();
        kernel = program.createKernel("Fill");
        kernel.register();
        
        buffer = clContext.createBuffer(4);
        buffer.register();
        
        flyCam.setEnabled(false);
        inputManager.setCursorVisible(true);
        
        BitmapFont fnt = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText infoText = new BitmapText(fnt);
        //infoText.setBox(new Rectangle(0, 0, settings.getWidth(), settings.getHeight()));
        infoText.setText("Device: "+clContext.getDevices());
        infoText.setLocalTranslation(0, settings.getHeight(), 0);
        guiNode.attachChild(infoText);
        statusText = new BitmapText(fnt);
        //statusText.setBox(new Rectangle(0, 0, settings.getWidth(), settings.getHeight()));
        statusText.setText("Running");
        statusText.setLocalTranslation(0, settings.getHeight() - infoText.getHeight() - 2, 0);
        guiNode.attachChild(statusText);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //call kernel to test if it is still working
        if (!failed) {
            try {
                kernel.Run1NoEvent(clQueue, new Kernel.WorkSize(1), buffer, 1.0f);
            } catch (OpenCLException ex) {
                LOG.log(Level.SEVERE, "Kernel call not working anymore", ex);
                failed = true;
                statusText.setText("Failed");
            }
        }
    }
}
