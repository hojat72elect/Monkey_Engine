
package com.jme3.opencl;

/**
 * This exception is thrown by {@link Program#build()}
 * when the compilation failed.
 * The error log returned by {@link #getLog() } contains detailed information
 * where the error occurred.
 *
 * @author shaman
 */
public class KernelCompilationException extends OpenCLException {
    private final String log;

    public KernelCompilationException(String msg, int errorCode, String log) {
        super(msg, errorCode);
        this.log = log;
    }

    /**
     * The output of the compiler
     *
     * @return the output text
     */
    public String getLog() {
        return log;
    }
}
