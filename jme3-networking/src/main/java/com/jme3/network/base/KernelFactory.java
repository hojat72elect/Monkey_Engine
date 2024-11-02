
package com.jme3.network.base;

import com.jme3.network.kernel.Kernel;
import java.io.IOException;


/**
 *  Supplied to the DefaultServer to create any additional
 *  channel kernels that might be required.
 *
 *  @version   $Revision$
 *  @author    Paul Speed
 */
public interface KernelFactory
{
    public static final KernelFactory DEFAULT = new NioKernelFactory();

    public Kernel createKernel( int channel, int port ) throws IOException;
}
