
package com.jme3.renderer.lwjgl;

import com.jme3.renderer.opengl.GLExt;
import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * The LWJGL implementation og {@link GLExt}.
 */
public class LwjglGLExt extends LwjglRender implements GLExt {

    @Override
    public void glBufferData(final int target, final IntBuffer data, final int usage) {
        checkLimit(data);
        GL15.glBufferData(target, data, usage);
    }

    @Override
    public void glBufferSubData(final int target, final long offset, final IntBuffer data) {
        checkLimit(data);
        GL15.glBufferSubData(target, offset, data);
    }

    @Override
    public void glDrawArraysInstancedARB(final int mode, final int first, final int count, final int primCount) {
        ARBDrawInstanced.glDrawArraysInstancedARB(mode, first, count, primCount);
    }

    @Override
    public void glDrawBuffers(final IntBuffer bufs) {
        checkLimit(bufs);
        GL20.glDrawBuffers(bufs);
    }

    @Override
    public void glDrawElementsInstancedARB(final int mode, final int indicesCount, final int type,
                                           final long indicesBufferOffset, final int primCount) {
        ARBDrawInstanced.glDrawElementsInstancedARB(mode, indicesCount, type, indicesBufferOffset, primCount);
    }

    @Override
    public void glGetMultisample(final int pname, final int index, final FloatBuffer val) {
        checkLimit(val);
        ARBTextureMultisample.glGetMultisamplefv(pname, index, val);
    }

    @Override
    public void glTexImage2DMultisample(final int target, final int samples, final int internalFormat, final int width,
                                        final int height, final boolean fixedSampleLocations) {
        ARBTextureMultisample.glTexImage2DMultisample(target, samples, internalFormat, width, height, fixedSampleLocations);
    }

    @Override
    public void glVertexAttribDivisorARB(final int index, final int divisor) {
        ARBInstancedArrays.glVertexAttribDivisorARB(index, divisor);
    }

    @Override
    public Object glFenceSync(final int condition, final int flags) {
        return ARBSync.glFenceSync(condition, flags);
    }
    
    @Override
    public int glClientWaitSync(final Object sync, final int flags, final long timeout) {
        return ARBSync.glClientWaitSync((Long) sync, flags, timeout);
    }

    @Override
    public void glDeleteSync(final Object sync) {
        ARBSync.glDeleteSync((Long) sync);
    }

    @Override
    public void glPushDebugGroup(int source, int id, String message) {
        KHRDebug.glPushDebugGroup(source, id, message);
    }

    @Override
    public void glPopDebugGroup() {
        KHRDebug.glPopDebugGroup();
    }

    @Override
    public void glObjectLabel(int identifier, int id, String label) {
        assert label != null;
        KHRDebug.glObjectLabel(identifier, id, label);
    }
}
