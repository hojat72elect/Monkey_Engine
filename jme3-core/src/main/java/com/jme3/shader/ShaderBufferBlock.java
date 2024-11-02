
package com.jme3.shader;

import java.lang.ref.WeakReference;

import com.jme3.shader.bufferobject.BufferObject;

/**
 * Implementation of shader's buffer block.
 *
 * @author JavaSaBr, Riccardo Balbo
 */
public class ShaderBufferBlock extends ShaderVariable {
    
    public static enum BufferType {
        UniformBufferObject, ShaderStorageBufferObject
    }

    /**
     * Current used buffer object.
     */
    protected BufferObject bufferObject;
    protected WeakReference<BufferObject> bufferObjectRef;
    protected BufferType type;

    /**
     * Set the new buffer object.
     *
     * @param bufferObject
     *            the new buffer object.
     */
    public void setBufferObject(BufferType type, BufferObject bufferObject) {
        if (bufferObject == null) {
            throw new IllegalArgumentException("for storage block " + name + ": storageData cannot be null");
        }
        if (bufferObject == this.bufferObject && type == this.type) return;
        this.bufferObject = bufferObject;
        this.bufferObjectRef = new WeakReference<BufferObject>(bufferObject);
        this.type = type;
        updateNeeded = true;
    }

    public BufferType getType() {
        return type;
    }

    /**
     * Return true if need to update this storage block.
     *
     * @return true if need to update this storage block.
     */
    public boolean isUpdateNeeded(){
        return updateNeeded;
    }

    /**
     * Clear the flag {@link #isUpdateNeeded()}.
     */
    public void clearUpdateNeeded(){
        updateNeeded = false;
    }

    /**
     * Reset this storage block.
     */
    public void reset() {
        location = -1;
        updateNeeded = true;
    }

    /**
     * Get the current storage data.
     *
     * @return the current storage data.
     */
    public BufferObject getBufferObject() {
        return bufferObject;
    }

    public WeakReference<BufferObject> getBufferObjectRef() {
        return bufferObjectRef;
    }

    public void setBufferObjectRef(WeakReference<BufferObject> bufferObjectRef) {
        this.bufferObjectRef = bufferObjectRef;
    }

}
