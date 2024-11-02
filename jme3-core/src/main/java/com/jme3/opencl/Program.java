
package com.jme3.opencl;

import java.nio.ByteBuffer;

/**
 * A wrapper for an OpenCL program. A program is created from kernel source code,
 * manages the build process and creates the kernels.
 * <p>
 * Warning: Creating the same kernel more than one leads to undefined behaviour,
 * this is especially important for {@link #createAllKernels() }
 *
 * @see Context#createProgramFromSourceCode(java.lang.String)
 * @see #createKernel(java.lang.String)
 * @author shaman
 */
public abstract class Program extends AbstractOpenCLObject {

    protected Program(ObjectReleaser releaser) {
        super(releaser);
    }

    @Override
    public Program register() {
        super.register();
        return this;
    }

    /**
     * Builds this program with the specified argument string on the specified
     * devices.
     * Please see the official OpenCL specification for a definition of
     * all supported arguments.
     * The list of devices specify on which device the compiled program
     * can then be executed. It must be a subset of {@link Context#getDevices() }.
     * If {@code null} is passed, the program is built on all available devices.
     *
     * @param args the compilation arguments
     * @param devices a list of devices on which the program is build.
     * @throws KernelCompilationException if the compilation fails
     * @see #build()
     */
    public abstract void build(String args, Device... devices) throws KernelCompilationException;

    /**
     * Builds this program without additional arguments
     *
     * @throws KernelCompilationException if the compilation fails
     */
    public void build() throws KernelCompilationException {
        build("", (Device[]) null);
    }

    /**
     * Creates the kernel with the specified name.
     *
     * @param name the name of the kernel as defined in the source code
     * @return the kernel object
     * @throws OpenCLException if the kernel was not found or some other error
     * occurred
     */
    public abstract Kernel createKernel(String name);

    /**
     * Creates all available kernels in this program.
     * The names of the kernels can then be queried by {@link Kernel#getName() }.
     *
     * @return an array of all kernels
     */
    public abstract Kernel[] createAllKernels();

    /**
     * Queries a compiled binary representation of this program for a particular
     * device. This binary can then be used e.g. in the next application launch
     * to create the program from the binaries and not from the sources.
     * This saves time.
     *
     * @param device the device from which the binaries are taken
     * @return the binaries
     * @see Context#createProgramFromBinary(java.nio.ByteBuffer, com.jme3.opencl.Device)
     */
    public abstract ByteBuffer getBinary(Device device);
}
