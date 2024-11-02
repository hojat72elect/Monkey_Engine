
package com.jme3.opencl;

/**
 * Generic OpenCL exception, can be thrown in every method of this package.
 * The error code and its name is reported in the message string as well as the OpenCL call that
 * causes this exception. Please refer to the official OpenCL specification
 * to see what might cause this exception.
 *
 * @author shaman
 */
public class OpenCLException extends RuntimeException {
    private static final long serialVersionUID = 8471229972153694848L;

    private final int errorCode;

    /**
     * Creates a new instance of <code>OpenCLException</code> without detail
     * message.
     */
    public OpenCLException() {
        errorCode = 0;
    }

    /**
     * Constructs an instance of <code>OpenCLException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public OpenCLException(String msg) {
        super(msg);
        errorCode = 0;
    }

    public OpenCLException(String msg, int errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

    /**
     * @return the error code
     */
    public int getErrorCode() {
        return errorCode;
    }
}
