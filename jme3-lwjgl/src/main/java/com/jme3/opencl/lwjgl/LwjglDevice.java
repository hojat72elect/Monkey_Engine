
package com.jme3.opencl.lwjgl;

import com.jme3.opencl.Device;
import java.util.Arrays;
import java.util.Collection;
import org.lwjgl.opencl.CL10;
import org.lwjgl.opencl.CL11;
import org.lwjgl.opencl.CLDevice;

/**
 *
 * @author shaman
 */
public final class LwjglDevice implements Device {

    final CLDevice device;
    final LwjglPlatform platform;

    public LwjglDevice(CLDevice device, LwjglPlatform platform) {
        this.device = device;
        this.platform = platform;
    }
    
    public CLDevice getDevice() {
        return device;
    }
    
    @Override
    public LwjglPlatform getPlatform() {
        return platform;
    }

    @Override
    public DeviceType getDeviceType() {
        int type = device.getInfoInt(CL10.CL_DEVICE_TYPE);
        switch (type) {
            case CL10.CL_DEVICE_TYPE_ACCELERATOR: return DeviceType.ACCELEARTOR;
            case CL10.CL_DEVICE_TYPE_CPU: return DeviceType.CPU;
            case CL10.CL_DEVICE_TYPE_GPU: return DeviceType.GPU;
            default: return DeviceType.DEFAULT;
        }
    }

    @Override
    public int getVendorId() {
        return device.getInfoInt(CL10.CL_DEVICE_VENDOR_ID);
    }

    @Override
    public boolean isAvailable() {
        return device.getInfoBoolean(CL10.CL_DEVICE_AVAILABLE);
    }

    @Override
    public boolean hasCompiler() {
        return device.getInfoBoolean(CL10.CL_DEVICE_COMPILER_AVAILABLE);
    }

    @Override
    public boolean hasDouble() {
        return hasExtension("cl_khr_fp64");
    }

    @Override
    public boolean hasHalfFloat() {
        return hasExtension("cl_khr_fp16");
    }

    @Override
    public boolean hasErrorCorrectingMemory() {
        return device.getInfoBoolean(CL10.CL_DEVICE_ERROR_CORRECTION_SUPPORT);
    }

    @Override
    public boolean hasUnifiedMemory() {
        return device.getInfoBoolean(CL11.CL_DEVICE_HOST_UNIFIED_MEMORY);
    }

    @Override
    public boolean hasImageSupport() {
        return device.getInfoBoolean(CL10.CL_DEVICE_IMAGE_SUPPORT);
    }
    
    @Override
    public boolean hasWritableImage3D() {
        return hasExtension("cl_khr_3d_image_writes");
    }

    @Override
    public boolean hasOpenGLInterop() {
        return hasExtension("cl_khr_gl_sharing");
    }
    
    @Override
    public boolean hasExtension(String extension) {
        return getExtensions().contains(extension);
    }

    @Override
    public Collection<? extends String> getExtensions() {
        return Arrays.asList(device.getInfoString(CL10.CL_DEVICE_EXTENSIONS).split(" "));
    }

    @Override
    public int getComputeUnits() {
        return device.getInfoInt(CL10.CL_DEVICE_MAX_COMPUTE_UNITS);
    }

    @Override
    public int getClockFrequency() {
        return device.getInfoInt(CL10.CL_DEVICE_MAX_CLOCK_FREQUENCY);
    }

    @Override
    public int getAddressBits() {
        return device.getInfoInt(CL10.CL_DEVICE_ADDRESS_BITS);
    }

    @Override
    public boolean isLittleEndian() {
        return device.getInfoBoolean(CL10.CL_DEVICE_ENDIAN_LITTLE);
    }

    @Override
    public long getMaximumWorkItemDimensions() {
        return device.getInfoSize(CL10.CL_DEVICE_MAX_WORK_ITEM_DIMENSIONS);
    }

    @Override
    public long[] getMaximumWorkItemSizes() {
        return device.getInfoSizeArray(CL10.CL_DEVICE_MAX_WORK_ITEM_SIZES);
    }

    @Override
    public long getMaxiumWorkItemsPerGroup() {
        return device.getInfoSize(CL10.CL_DEVICE_MAX_WORK_GROUP_SIZE);
    }

    @Override
    public int getMaximumSamplers() {
        return device.getInfoInt(CL10.CL_DEVICE_MAX_SAMPLERS);
    }

    @Override
    public int getMaximumReadImages() {
        return device.getInfoInt(CL10.CL_DEVICE_MAX_READ_IMAGE_ARGS);
    }

    @Override
    public int getMaximumWriteImages() {
        return device.getInfoInt(CL10.CL_DEVICE_MAX_WRITE_IMAGE_ARGS);
    }

    @Override
    public long[] getMaximumImage2DSize() {
        return new long[] {
            device.getInfoSize(CL10.CL_DEVICE_IMAGE2D_MAX_WIDTH),
            device.getInfoSize(CL10.CL_DEVICE_IMAGE2D_MAX_HEIGHT)
        };
    }

    @Override
    public long[] getMaximumImage3DSize() {
        return new long[] {
            device.getInfoSize(CL10.CL_DEVICE_IMAGE3D_MAX_WIDTH),
            device.getInfoSize(CL10.CL_DEVICE_IMAGE3D_MAX_HEIGHT),
            device.getInfoSize(CL10.CL_DEVICE_IMAGE3D_MAX_DEPTH)
        };
    }
    
    @Override
    public long getMaximumAllocationSize() {
        return device.getInfoLong(CL10.CL_DEVICE_MAX_MEM_ALLOC_SIZE);
    }
    
    @Override
    public long getGlobalMemorySize() {
        return device.getInfoLong(CL10.CL_DEVICE_GLOBAL_MEM_SIZE);
    }
    
    @Override
    public long getLocalMemorySize() {
        return device.getInfoLong(CL10.CL_DEVICE_LOCAL_MEM_SIZE);
    }
    
    @Override
    public long getMaximumConstantBufferSize() {
        return device.getInfoLong(CL10.CL_DEVICE_MAX_CONSTANT_BUFFER_SIZE);
    }
    
    @Override
    public int getMaximumConstantArguments() {
        return device.getInfoInt(CL10.CL_DEVICE_MAX_CONSTANT_ARGS);
    }

    @Override
    public String getProfile() {
        return device.getInfoString(CL10.CL_DEVICE_PROFILE);
    }

    @Override
    public String getVersion() {
        return device.getInfoString(CL10.CL_DEVICE_VERSION);
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
    public String getCompilerVersion() {
        return device.getInfoString(CL11.CL_DEVICE_OPENCL_C_VERSION);
    }

    @Override
    public int getCompilerVersionMajor() {
        return Utils.getMajorVersion(getCompilerVersion(), "OpenCL C ");
    }

    @Override
    public int getCompilerVersionMinor() {
        return Utils.getMinorVersion(getCompilerVersion(), "OpenCL C ");
    }

    @Override
    public String getDriverVersion() {
        return device.getInfoString(CL10.CL_DRIVER_VERSION);
    }

    @Override
    public int getDriverVersionMajor() {
        return Utils.getMajorVersion(getDriverVersion(), "");
    }

    @Override
    public int getDriverVersionMinor() {
        return Utils.getMinorVersion(getDriverVersion(), "");
    }

    @Override
    public String getName() {
        return device.getInfoString(CL10.CL_DEVICE_NAME);
    }

    @Override
    public String getVendor() {
        return device.getInfoString(CL10.CL_DEVICE_VENDOR);
    }

    @Override
    public String toString() {
        return getName();
    }
    
}
