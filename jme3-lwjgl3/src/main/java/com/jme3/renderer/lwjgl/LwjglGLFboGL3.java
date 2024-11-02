
package com.jme3.renderer.lwjgl;

import com.jme3.renderer.opengl.GLFbo;
import org.lwjgl.opengl.GL30;

import java.nio.IntBuffer;

/**
 * Implements GLFbo via OpenGL3+.
 *
 * @author Kirill Vainer
 */
public class LwjglGLFboGL3 extends LwjglRender implements GLFbo {

    @Override
    public void glBlitFramebufferEXT(final int srcX0, final int srcY0, final int srcX1, final int srcY1,
                                     final int dstX0, final int dstY0, final int dstX1, final int dstY1, final int mask,
                                     final int filter) {
        GL30.glBlitFramebuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
    }

    @Override
    public void glRenderbufferStorageMultisampleEXT(final int target, final int samples, final int internalFormat,
                                                    final int width, final int height) {
        GL30.glRenderbufferStorageMultisample(target, samples, internalFormat, width, height);
    }

    @Override
    public void glBindFramebufferEXT(final int target, final int frameBuffer) {
        GL30.glBindFramebuffer(target, frameBuffer);
    }

    @Override
    public void glBindRenderbufferEXT(final int target, final int renderBuffer) {
        GL30.glBindRenderbuffer(target, renderBuffer);
    }

    @Override
    public int glCheckFramebufferStatusEXT(final int target) {
        return GL30.glCheckFramebufferStatus(target);
    }

    @Override
    public void glDeleteFramebuffersEXT(final IntBuffer frameBuffers) {
        checkLimit(frameBuffers);
        GL30.glDeleteFramebuffers(frameBuffers);
    }

    @Override
    public void glDeleteRenderbuffersEXT(final IntBuffer renderBuffers) {
        checkLimit(renderBuffers);
        GL30.glDeleteRenderbuffers(renderBuffers);
    }

    @Override
    public void glFramebufferRenderbufferEXT(final int target, final int attachment, final int renderBufferTarget,
                                             final int renderBuffer) {
        GL30.glFramebufferRenderbuffer(target, attachment, renderBufferTarget, renderBuffer);
    }

    @Override
    public void glFramebufferTexture2DEXT(final int target, final int attachment, final int texTarget,
                                          final int texture, final int level) {
        GL30.glFramebufferTexture2D(target, attachment, texTarget, texture, level);
    }

    @Override
    public void glGenFramebuffersEXT(final IntBuffer frameBuffers) {
        checkLimit(frameBuffers);
        GL30.glGenFramebuffers(frameBuffers);
    }

    @Override
    public void glGenRenderbuffersEXT(final IntBuffer renderBuffers) {
        checkLimit(renderBuffers);
        GL30.glGenRenderbuffers(renderBuffers);
    }

    @Override
    public void glGenerateMipmapEXT(final int target) {
        GL30.glGenerateMipmap(target);
    }

    @Override
    public void glRenderbufferStorageEXT(final int target, final int internalFormat, final int width,
                                         final int height) {
        GL30.glRenderbufferStorage(target, internalFormat, width, height);
    }

    @Override
    public void glFramebufferTextureLayerEXT(final int target, final int attachment, final int texture, final int level,
                                             final int layer) {
        GL30.glFramebufferTextureLayer(target, attachment, texture, level, layer);
    }
}
