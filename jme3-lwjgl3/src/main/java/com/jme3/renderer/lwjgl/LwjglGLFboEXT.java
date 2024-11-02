
package com.jme3.renderer.lwjgl;

import com.jme3.renderer.opengl.GLFbo;
import org.lwjgl.opengl.EXTFramebufferBlit;
import org.lwjgl.opengl.EXTFramebufferMultisample;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.EXTTextureArray;

import java.nio.IntBuffer;

/**
 * Implements GLFbo via GL_EXT_framebuffer_object.
 *
 * @author Kirill Vainer
 */
public class LwjglGLFboEXT extends LwjglRender implements GLFbo {

    @Override
    public void glBlitFramebufferEXT(final int srcX0, final int srcY0, final int srcX1, final int srcY1,
                                     final int dstX0, final int dstY0, final int dstX1, final int dstY1, final int mask,
                                     final int filter) {
        EXTFramebufferBlit.glBlitFramebufferEXT(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
    }

    @Override
    public void glRenderbufferStorageMultisampleEXT(final int target, final int samples, final int internalFormat,
                                                    final int width, final int height) {
        EXTFramebufferMultisample.glRenderbufferStorageMultisampleEXT(target, samples, internalFormat, width, height);
    }

    @Override
    public void glBindFramebufferEXT(final int target, final int frameBuffer) {
        EXTFramebufferObject.glBindFramebufferEXT(target, frameBuffer);
    }

    @Override
    public void glBindRenderbufferEXT(final int target, final int renderBuffer) {
        EXTFramebufferObject.glBindRenderbufferEXT(target, renderBuffer);
    }

    @Override
    public int glCheckFramebufferStatusEXT(final int target) {
        return EXTFramebufferObject.glCheckFramebufferStatusEXT(target);
    }

    @Override
    public void glDeleteFramebuffersEXT(final IntBuffer frameBuffers) {
        checkLimit(frameBuffers);
        EXTFramebufferObject.glDeleteFramebuffersEXT(frameBuffers);
    }

    @Override
    public void glDeleteRenderbuffersEXT(final IntBuffer renderBuffers) {
        checkLimit(renderBuffers);
        EXTFramebufferObject.glDeleteRenderbuffersEXT(renderBuffers);
    }

    @Override
    public void glFramebufferRenderbufferEXT(final int target, final int attachment, final int renderBufferTarget,
                                             final int renderBuffer) {
        EXTFramebufferObject.glFramebufferRenderbufferEXT(target, attachment, renderBufferTarget, renderBuffer);
    }

    @Override
    public void glFramebufferTexture2DEXT(final int target, final int attachment, final int texTarget,
                                          final int texture, final int level) {
        EXTFramebufferObject.glFramebufferTexture2DEXT(target, attachment, texTarget, texture, level);
    }

    @Override
    public void glGenFramebuffersEXT(final IntBuffer frameBuffers) {
        checkLimit(frameBuffers);
        EXTFramebufferObject.glGenFramebuffersEXT(frameBuffers);
    }

    @Override
    public void glGenRenderbuffersEXT(final IntBuffer renderBuffers) {
        checkLimit(renderBuffers);
        EXTFramebufferObject.glGenRenderbuffersEXT(renderBuffers);
    }

    @Override
    public void glGenerateMipmapEXT(final int target) {
        EXTFramebufferObject.glGenerateMipmapEXT(target);
    }

    @Override
    public void glRenderbufferStorageEXT(final int target, final int internalFormat, final int width,
                                         final int height) {
        EXTFramebufferObject.glRenderbufferStorageEXT(target, internalFormat, width, height);
    }

    @Override
    public void glFramebufferTextureLayerEXT(final int target, final int attachment, final int texture, final int level,
                                             final int layer) {
        EXTTextureArray.glFramebufferTextureLayerEXT(target, attachment, texture, level, layer);
    }
}
