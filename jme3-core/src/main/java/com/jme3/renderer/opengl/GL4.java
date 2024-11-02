
package com.jme3.renderer.opengl;

/**
 * GL functions only available on vanilla desktop OpenGL 4.0.
 * 
 * @author Kirill Vainer
 */
public interface GL4 extends GL3 {

    public static final int GL_TESS_CONTROL_SHADER = 0x8E88;
    public static final int GL_TESS_EVALUATION_SHADER = 0x8E87;
    public static final int GL_PATCHES = 0xE;

    /**
     * Accepted by the {@code target} parameter of BindBufferBase and BindBufferRange.
     */
    public static final int GL_ATOMIC_COUNTER_BUFFER = 0x92C0;

    /**
     * Accepted by the {@code target} parameters of BindBuffer, BufferData, BufferSubData, MapBuffer, UnmapBuffer, GetBufferSubData, and GetBufferPointerv.
     */
    public static final int GL_SHADER_STORAGE_BUFFER = 0x90D2;
    public static final int GL_SHADER_STORAGE_BLOCK = 0x92E6;

    /**
     *  Accepted by the &lt;pname&gt; parameter of GetIntegerv, GetBooleanv,
     *  GetInteger64v, GetFloatv, and GetDoublev:
     */
    public static final int GL_MAX_VERTEX_SHADER_STORAGE_BLOCKS = 0x90D6;
    public static final int GL_MAX_GEOMETRY_SHADER_STORAGE_BLOCKS = 0x90D7;
    public static final int GL_MAX_TESS_CONTROL_SHADER_STORAGE_BLOCKS = 0x90D8;
    public static final int GL_MAX_TESS_EVALUATION_SHADER_STORAGE_BLOCKS = 0x90D9;
    public static final int GL_MAX_FRAGMENT_SHADER_STORAGE_BLOCKS = 0x90DA;
    public static final int GL_MAX_COMPUTE_SHADER_STORAGE_BLOCKS = 0x90DB;
    public static final int GL_MAX_COMBINED_SHADER_STORAGE_BLOCKS = 0x90DC;
    public static final int GL_MAX_SHADER_STORAGE_BUFFER_BINDINGS = 0x90DD;
    public static final int GL_MAX_SHADER_STORAGE_BLOCK_SIZE = 0x90DE;
    public static final int GL_SHADER_STORAGE_BUFFER_OFFSET_ALIGNMENT = 0x90DF;

    /**
     * <p><a target="_blank" href="http://docs.gl/gl4/glPatchParameteri">Reference Page</a></p>
     * <p>
     * Specifies the integer value of the specified parameter for patch primitives.
     *
     * @param count the new value for the parameter given by {@code pname}
     */
    public void glPatchParameter(int count);

    /**
     * Returns the unsigned integer index assigned to a resource named name in the interface type programInterface of
     * program object program.
     *
     * @param program          the name of a program object whose resources to query.
     * @param programInterface a token identifying the interface within program containing the resource named name.
     * @param name             the name of the resource to query the index of.
     * @return the index of a named resource within a program.
     */
    public int glGetProgramResourceIndex(int program, int programInterface, String name);

    /**
     * Changes the active shader storage block with an assigned index of storageBlockIndex in program object program.
     * storageBlockIndex must be an active shader storage block index in program. storageBlockBinding must be less
     * than the value of {@code #GL_MAX_SHADER_STORAGE_BUFFER_BINDINGS}. If successful, glShaderStorageBlockBinding specifies
     * that program will use the data store of the buffer object bound to the binding point storageBlockBinding to
     * read and write the values of the buffer variables in the shader storage block identified by storageBlockIndex.
     *
     * @param program             the name of a program object whose resources to query.
     * @param storageBlockIndex   The index storage block within the program.
     * @param storageBlockBinding The index storage block binding to associate with the specified storage block.
     */
    public void glShaderStorageBlockBinding(int program, int storageBlockIndex, int storageBlockBinding);
}
