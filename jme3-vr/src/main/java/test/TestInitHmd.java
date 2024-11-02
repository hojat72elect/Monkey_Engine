
package test;

import com.jme3.app.VREnvironment;
import com.jme3.input.vr.openvr.OpenVR;
import com.jme3.system.AppSettings;

/**
 * Testing OpenVR environment.
 * @author Rickard (neph1 @ github)
 */
public class TestInitHmd {
    
    public static void main(String... args){
        testInitHmd();
    }
    
    public static void testInitHmd(){
        VREnvironment environment = new VREnvironment(new AppSettings(true));
        environment.initialize();
        OpenVR openVr = (OpenVR) environment.getVRHardware();
        System.out.println(openVr.getName());
        
        openVr.updatePose();
        
        openVr.destroy();
    
    }
}