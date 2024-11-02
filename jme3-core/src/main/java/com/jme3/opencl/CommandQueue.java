
package com.jme3.opencl;

/**
 * Wrapper for an OpenCL command queue.
 * The command queue serializes every GPU function call: By passing the same
 * queue to OpenCL function (buffer, image operations, kernel calls), it is
 * ensured that they are executed in the order in which they are passed.
 * <br>
 * Each command queue is associated with exactly one device: that device
 * is specified on creation ({@link Context#createQueue(com.jme3.opencl.Device) })
 * and all commands are sent to this device.
 *
 * @author shaman
 */
public abstract class CommandQueue extends AbstractOpenCLObject {
    protected Device device;

    protected CommandQueue(ObjectReleaser releaser, Device device) {
        super(releaser);
        this.device = device;
    }

    @Override
    public CommandQueue register() {
        super.register();
        return this;
    }

    /**
     * Returns the device associated with this command queue.
     * It can be used to query properties of the device that is used to execute
     * the commands issued to this command queue.
     *
     * @return the associated device
     */
    public Device getDevice() {
        return device;
    }

    /**
     * Issues all previously queued OpenCL commands in command_queue to the
     * device associated with command queue. Flush only guarantees that all
     * queued commands to command_queue will eventually be submitted to the
     * appropriate device. There is no guarantee that they will be complete
     * after flush returns.
     */
    public abstract void flush();

    /**
     * Blocks until all previously queued OpenCL commands in command queue are
     * issued to the associated device and have completed. Finish does not
     * return until all previously queued commands in command queue have been
     * processed and completed. Finish is also a synchronization point.
     */
    public abstract void finish();
}
