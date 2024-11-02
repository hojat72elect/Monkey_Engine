
package com.jme3.renderer;

/**
 * Allows querying the limits of certain features in
 * {@link Renderer}.
 *
 * <p>For example, maximum texture sizes or number of samples.
 *
 * @author Kirill Vainer
 */
public enum Limits {
    /**
     * Maximum number of vertex texture units, or number of textures that can be
     * used in the vertex shader.
     */
    VertexTextureUnits,
    /**
     * Maximum number of fragment texture units, or number of textures that can
     * be used in the fragment shader.
     */
    FragmentTextureUnits,
    /**
     * Maximum number of fragment uniform vectors.
     */
    FragmentUniformVectors,
    /**
     * Maximum number of vertex uniform vectors.
     */
    VertexUniformVectors,
    /**
     * Maximum number of vertex attributes.
     */
    VertexAttributes,
    /**
     * Maximum number of FrameBuffer samples.
     */
    FrameBufferSamples,
    /**
     * Maximum number of FrameBuffer attachments.
     */
    FrameBufferAttachments,
    /**
     * Maximum number of FrameBuffer MRT attachments.
     */
    FrameBufferMrtAttachments,
    /**
     * Maximum render buffer size.
     */
    RenderBufferSize,
    /**
     * Maximum texture size.
     */
    TextureSize,
    /**
     * Maximum cubemap size.
     */
    CubemapSize,
    /**
     * Maximum number of color texture samples.
     */
    ColorTextureSamples,
    /**
     * Maximum number of depth texture samples.
     */
    DepthTextureSamples,
    /**
     * Maximum degree of texture anisotropy.
     */
    TextureAnisotropy,

    // UBO
    /**
     * Maximum number of UBOs that may be accessed by a vertex shader.
     */
    UniformBufferObjectMaxVertexBlocks,
    /**
     * Maximum number of UBOs that may be accessed by a fragment shader.
     */
    UniformBufferObjectMaxFragmentBlocks,
    /**
     * Maximum number of UBOs that may be accessed by a geometry shader.
     */
    UniformBufferObjectMaxGeometryBlocks,
    /**
     * Maximum block size of a UBO.
     */
    UniformBufferObjectMaxBlockSize,

    // SSBO
    /**
     * Maximum size of an SSBO.
     */
    ShaderStorageBufferObjectMaxBlockSize,
    /**
     * Maximum number of active SSBOs that may be accessed by a vertex shader.
     */
    ShaderStorageBufferObjectMaxVertexBlocks,
    /**
     * Maximum number of active SSBOs that may be accessed by a fragment shader.
     */
    ShaderStorageBufferObjectMaxFragmentBlocks,
    /**
     * Maximum number of active SSBOs that may be accessed by a geometry shader.
     */
    ShaderStorageBufferObjectMaxGeometryBlocks,
    /**
     * Maximum number of active SSBOs that may be accessed by a tessellation control shader.
     */
    ShaderStorageBufferObjectMaxTessControlBlocks,
    /**
     * Maximum number of active SSBOs that may be accessed by a tessellation evaluation shader.
     */
    ShaderStorageBufferObjectMaxTessEvaluationBlocks,
    /**
     * Not implemented yet.
     */
    ShaderStorageBufferObjectMaxComputeBlocks,
    /**
     * Maximum number shader storage blocks across all active programs.
     */
    ShaderStorageBufferObjectMaxCombineBlocks,
}
