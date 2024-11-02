
package com.jme3.renderer.opengl;

import java.nio.IntBuffer;

/**
 * GL functions and constants only available on vanilla OpenGL ES 3.0.
 *
 * @author Jesus Oliver
 */
public interface GLES_30 extends GL {

    public static final int GL_RGB10_A2 = 0x8059;
    public static final int GL_UNSIGNED_INT_2_10_10_10_REV = 0x8368;
 
    public void glBindVertexArray(int array);

    public void glDeleteVertexArrays(IntBuffer arrays);
   
    public void glGenVertexArrays(IntBuffer arrays);
}
