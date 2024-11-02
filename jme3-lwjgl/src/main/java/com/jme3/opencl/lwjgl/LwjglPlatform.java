
package com.jme3.opencl.lwjgl;

import com.jme3.opencl.Platform;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.lwjgl.opencl.CL10;
import org.lwjgl.opencl.CLDevice;
import org.lwjgl.opencl.CLPlatform;

/**
 *
 * @author shaman
 */
public final class LwjglPlatform implements Platform {
    
    final CLPlatform platform;
    List<LwjglDevice> devices;
    
    public LwjglPlatform(CLPlatform platform) {
        this.platform = platform;
    }

    public CLPlatform getPlatform() {
        return platform;
    }
    
    @Override
    public List<LwjglDevice> getDevices() {
        if (devices == null) {
            devices = new ArrayList<>();
            for (CLDevice d : platform.getDevices(CL10.CL_DEVICE_TYPE_ALL)) {
                devices.add(new LwjglDevice(d, this));
            }
        }
        return devices;
    }

    @Override
    public String getProfile() {
        return platform.getInfoString(CL10.CL_PLATFORM_PROFILE);
    }

    @Override
    public boolean isFullProfile() {
        return getProfile().contains("FULL_PROFILE");
    }

    @Override
    public boolean isEmbeddedProfile() {
        return getProfile().contains("EMBEDDED_PROFILE");
    }

    @Override
    public String getVersion() {
        return platform.getInfoString(CL10.CL_PLATFORM_VERSION);
    }

    @Override
    public int getVersionMajor() {
        return Utils.getMajorVersion(getVersion(), "OpenCL ");
    }

    @Override
    public int getVersionMinor() {
        return Utils.getMinorVersion(getVersion(), "OpenCL ");
    }

    @Override
    public String getName() {
        return platform.getInfoString(CL10.CL_PLATFORM_NAME);
    }

    @Override
    public String getVendor() {
        return platform.getInfoString(CL10.CL_PLATFORM_VENDOR);
    }

    @Override
    public boolean hasExtension(String extension) {
        return getExtensions().contains(extension);
    }

    @Override
    public boolean hasOpenGLInterop() {
        return hasExtension("cl_khr_gl_sharing");
    }

    @Override
    public Collection<? extends String> getExtensions() {
        return Arrays.asList(platform.getInfoString(CL10.CL_PLATFORM_EXTENSIONS).split(" "));
    }

    @Override
    public String toString() {
        return getName();
    }

}
