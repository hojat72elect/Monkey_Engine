
package com.jme3.network.base;

import com.jme3.network.kernel.Kernel;
import com.jme3.network.kernel.tcp.SelectorKernel;
import java.io.IOException;


/**
 *  KernelFactory implementation for creating TCP kernels
 *  using the NIO selector model.
 *
 *  @version   $Revision$
 *  @author    Paul Speed
 */
public class NioKernelFactory implements KernelFactory
{
    @Override
    public Kernel createKernel( int channel, int port ) throws IOException
    {
        return new SelectorKernel(port);
    }
}
