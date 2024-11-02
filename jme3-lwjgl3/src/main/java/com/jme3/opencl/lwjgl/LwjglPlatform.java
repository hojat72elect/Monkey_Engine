
package com.jme3.opencl.lwjgl;

import com.jme3.opencl.Platform;
import com.jme3.opencl.lwjgl.info.Info;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.CL10;

/**
 *
 * @author shaman
 */
public final class LwjglPlatform implements Platform {
    
    final long platform;
    List<LwjglDevice> devices;
    
    public LwjglPlatform(long platform) {
        this.platform = platform;
    }

    public long getPlatform() {
        return platform;
    }
    
    @Override
    public List<LwjglDevice> getDevices() {
        if (devices == null) {
            devices = new ArrayList<>();
            for (long d : getDevices(CL10.CL_DEVICE_TYPE_ALL)) {
                devices.add(new LwjglDevice(d, this));
            }
        }
        return devices;
    }
    
    /**
     * Returns a list of the available devices on this platform that match the
     * specified type, filtered by the specified filter.
     * 
     * Copied from the old release.
     *
     * @param deviceType the device type
     * @return the available devices
     */
    private long[] getDevices(int deviceType) {
        int[] count = new int[1];
        int errcode = CL10.clGetDeviceIDs(platform, deviceType, null, count);
        if (errcode == CL10.CL_DEVICE_NOT_FOUND) {
            return new long[0];
        }
        Utils.checkError(errcode, "clGetDeviceIDs");

        int num_devices = count[0];
        if (num_devices == 0) {
            return new long[0];
        }

        PointerBuffer devices = PointerBuffer.allocateDirect(num_devices);

        errcode = CL10.clGetDeviceIDs(platform, deviceType, devices, (IntBuffer) null);
        Utils.checkError(errcode, "clGetDeviceIDs");

        long[] deviceIDs = new long[num_devices];
        devices.rewind();
        for (int i = 0; i < num_devices; i++) {
            deviceIDs[i] = devices.get();
        }

        return deviceIDs;
    }

    @Override
    public String getProfile() {
        return Info.clGetPlatformInfoStringASCII(platform, CL10.CL_PLATFORM_PROFILE);
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
        return Info.clGetPlatformInfoStringASCII(platform, CL10.CL_PLATFORM_VERSION);
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
        return Info.clGetPlatformInfoStringASCII(platform, CL10.CL_PLATFORM_NAME);
    }

    @Override
    public String getVendor() {
        return Info.clGetPlatformInfoStringASCII(platform, CL10.CL_PLATFORM_VENDOR);
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
        return Arrays.asList(Info.clGetPlatformInfoStringASCII(platform, CL10.CL_PLATFORM_EXTENSIONS).split(" "));
    }

    @Override
    public String toString() {
        return getName();
    }

}
