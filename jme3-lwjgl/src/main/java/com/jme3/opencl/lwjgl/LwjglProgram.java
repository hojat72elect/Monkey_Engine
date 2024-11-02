
package com.jme3.opencl.lwjgl;

import com.jme3.opencl.*;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opencl.*;

/**
 *
 * @author shaman
 */
public class LwjglProgram extends Program {
    private static final Logger LOG = Logger.getLogger(LwjglProgram.class.getName());
    
    private final CLProgram program;
    private final LwjglContext context;

    public LwjglProgram(CLProgram program, LwjglContext context) {
        super(new ReleaserImpl(program));
        this.program = program;
        this.context = context;
    }

    public CLProgram getProgram() {
        return program;
    }

    @Override
    public void build(String args, Device... devices) throws KernelCompilationException {
        PointerBuffer deviceList = null;
        if (devices != null) {
            deviceList = PointerBuffer.allocateDirect(devices.length);
            deviceList.rewind();
            for (Device d : devices) {
                deviceList.put(((LwjglDevice) d).device.getPointer());
            }
            deviceList.flip();
        }
        int ret = CL10.clBuildProgram(program, deviceList, args, null);
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
    
    private String Log() {
        StringBuilder str = new StringBuilder();
        for (LwjglDevice device : context.getDevices()) {
            CLDevice d = device.getDevice();
            str.append(device.getName()).append(":\n");
            str.append(program.getBuildInfoString(d, CL10.CL_PROGRAM_BUILD_LOG));
            str.append('\n');
        }
        return str.toString();
    }

    @Override
    public Kernel createKernel(String name) {
        CLKernel kernel = CL10.clCreateKernel(program, name, Utils.errorBuffer);
        Utils.checkError(Utils.errorBuffer, "clCreateKernel");
        return new LwjglKernel(kernel);
    }

    @Override
    public Kernel[] createAllKernels() {
        CLKernel[] kernels = program.createKernelsInProgram();
        Kernel[] kx = new Kernel[kernels.length];
        for (int i=0; i<kernels.length; ++i) {
            kx[i] = new LwjglKernel(kernels[i]);
        }
        return kx;
    }

    @Override
    public ByteBuffer getBinary(Device device) {
        ByteBuffer[] binaries = program.getInfoBinaries((ByteBuffer[]) null);
        CLDevice[] devices = program.getInfoDevices();
        //find the requested one
        assert (binaries.length == devices.length);
        for (int i=0; i<devices.length; ++i) {
            if (((LwjglDevice) device).device == devices[i]) {
                return binaries[i];
            }
        }
        throw new com.jme3.opencl.OpenCLException("Program was not built against the specified device "+device);
    }

    private static class ReleaserImpl implements ObjectReleaser {
        private CLProgram program;
        private ReleaserImpl(CLProgram program) {
            this.program = program;
        }
        @Override
        public void release() {
            //LWJGL Bug: releasing a program also released every! kernel associated with that program
            /*
            if (program != null) {
                int ret = CL10.clReleaseProgram(program);
                program = null;
                Utils.reportError(ret, "clReleaseProgram");
            }
            */
        }
    }
}
