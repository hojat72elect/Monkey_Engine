
package com.jme3.opencl.lwjgl;

import com.jme3.opencl.*;
import com.jme3.opencl.lwjgl.info.Info;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.*;
import org.lwjgl.system.MemoryUtil;

/**
 *
 * @author shaman
 */
public class LwjglProgram extends Program {
    private static final Logger LOG = Logger.getLogger(LwjglProgram.class.getName());
    
    private final long program;
    private final LwjglContext context;

    public LwjglProgram(long program, LwjglContext context) {
        super(new ReleaserImpl(program));
        this.program = program;
        this.context = context;
    }

    public long getProgram() {
        return program;
    }

    @Override
    public void build(String args, Device... devices) throws KernelCompilationException {
        PointerBuffer deviceList = null;
        if (devices != null) {
            deviceList = PointerBuffer.allocateDirect(devices.length);
            deviceList.rewind();
            for (Device d : devices) {
                deviceList.put(((LwjglDevice) d).getDevice());
            }
            deviceList.flip();
        }
        int ret = CL10.clBuildProgram(program, deviceList, args, null, 0);
        if (ret != CL10.CL_SUCCESS) {
            String log = Log();
            LOG.log(Level.WARNING, "Unable to compile program:\n{0}", log);
            if (ret == CL10.CL_BUILD_PROGRAM_FAILURE) {
                throw new KernelCompilationException("Failed to build program", ret, log);
            } else {
                Utils.checkError(ret, "clBuildProgram");
            }
        } else {
            LOG.log(Level.INFO, "Program compiled:\n{0}", Log());
        }
    }
    
    private String Log(long device) {
        Utils.pointerBuffers[0].rewind();
        int ret = CL10.clGetProgramBuildInfo(program, device, CL10.CL_PROGRAM_BUILD_LOG, (ByteBuffer) null, Utils.pointerBuffers[0]);
        Utils.checkError(ret, "clGetProgramBuildInfo");
        int count = (int) Utils.pointerBuffers[0].get(0);
        final ByteBuffer buffer = BufferUtils.createByteBuffer(count);
        ret = CL10.clGetProgramBuildInfo(program, device, CL10.CL_PROGRAM_BUILD_LOG, buffer, null);
        Utils.checkError(ret, "clGetProgramBuildInfo");
        return MemoryUtil.memASCII(buffer);
    }
    
    private String Log() {
        StringBuilder str = new StringBuilder();
        for (LwjglDevice device : context.getDevices()) {
            long d = device.getDevice();
            str.append(device.getName()).append(":\n");
            //str.append(Info.clGetProgramBuildInfoStringASCII(program, d, CL10.CL_PROGRAM_BUILD_LOG)); //This throws an IllegalArgumentException in Buffer.limit()
            str.append(Log(d));
            str.append('\n');
        }
        return str.toString();
    }

    @Override
    public Kernel createKernel(String name) {
        long kernel = CL10.clCreateKernel(program, name, Utils.errorBuffer);
        Utils.checkError(Utils.errorBuffer, "clCreateKernel");
        return new LwjglKernel(kernel);
    }

    @Override
    public Kernel[] createAllKernels() {
        Utils.tempBuffers[0].b16i.rewind();
        int ret = CL10.clCreateKernelsInProgram(program, null, Utils.tempBuffers[0].b16i);
        Utils.checkError(ret, "clCreateKernelsInProgram");
        int count = Utils.tempBuffers[0].b16i.get(0);
        PointerBuffer buf = PointerBuffer.allocateDirect(count);
        ret = CL10.clCreateKernelsInProgram(program, buf, (IntBuffer) null);
        Utils.checkError(ret, "clCreateKernelsInProgram");
        Kernel[] kx = new Kernel[count];
        for (int i=0; i<count; ++i) {
            kx[i] = new LwjglKernel(buf.get());
        }
        return kx;
    }

    @Override
    public ByteBuffer getBinary(Device d) {
        //throw new UnsupportedOperationException("Not supported yet, would crash the JVM");
        
        LwjglDevice device = (LwjglDevice) d;
        int numDevices = Info.clGetProgramInfoInt(program, CL10.CL_PROGRAM_NUM_DEVICES);
        
        PointerBuffer devices = PointerBuffer.allocateDirect(numDevices);
        int ret = CL10.clGetProgramInfo(program, CL10.CL_PROGRAM_DEVICES, devices, null);
        Utils.checkError(ret, "clGetProgramInfo: CL_PROGRAM_DEVICES");
        int index = -1;
        for (int i=0; i<numDevices; ++i) {
            if (devices.get(i) == device.getDevice()) {
                index = i;
            }
        }
        if (index == -1) {
             throw new com.jme3.opencl.OpenCLException("Program was not built against the specified device "+device);
        }
        
        PointerBuffer sizes = PointerBuffer.allocateDirect(numDevices);
        ret = CL10.clGetProgramInfo(program, CL10.CL_PROGRAM_BINARY_SIZES, sizes, null);
        Utils.checkError(ret, "clGetProgramInfo: CL_PROGRAM_BINARY_SIZES");
        int size = (int) sizes.get(index);
        
        PointerBuffer binaryPointers = PointerBuffer.allocateDirect(numDevices);
        for (int i=0; i<binaryPointers.capacity(); ++i) {
            binaryPointers.put(0L);
        }
        binaryPointers.rewind();
        ByteBuffer binaries = ByteBuffer.allocateDirect(size);
        binaryPointers.put(index, binaries);
        
        //Fixme: why the hell does this line throw a segfault ?!?
        ret = CL10.clGetProgramInfo(program, CL10.CL_PROGRAM_BINARIES, binaryPointers, null);
        Utils.checkError(ret, "clGetProgramInfo: CL_PROGRAM_BINARIES");
        
        return binaries;
    }

    private static class ReleaserImpl implements ObjectReleaser {
        private long program;
        private ReleaserImpl(long program) {
            this.program = program;
        }
        @Override
        public void release() {
            if (program != 0) {
                int ret = CL10.clReleaseProgram(program);
                program = 0;
                Utils.reportError(ret, "clReleaseProgram");
            }
        }
    }
}
