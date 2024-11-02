
package com.jme3.shader;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Matrix4f;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.shader.bufferobject.BufferObject;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.Texture3D;
import com.jme3.texture.TextureArray;
import com.jme3.texture.TextureCubeMap;

public enum VarType {

    Float("float", float.class, Float.class),
    Vector2("vec2", Vector2f.class),
    Vector3("vec3", Vector3f.class),
    Vector4("vec4", Vector4f.class, ColorRGBA.class),

    IntArray(true, false, "int", int[].class, Integer[].class),
    FloatArray(true, false, "float", float[].class, Float[].class),
    Vector2Array(true, false, "vec2", Vector2f[].class),
    Vector3Array(true, false, "vec3", Vector3f[].class),
    Vector4Array(true, false, "vec4", Vector4f[].class),

    Boolean("bool", Boolean.class, boolean.class),

    Matrix3(true, false, "mat3", Matrix3f.class),
    Matrix4(true, false, "mat4", Matrix4f.class),

    Matrix3Array(true, false, "mat3", Matrix3f[].class),
    Matrix4Array(true, false, "mat4", Matrix4f[].class),

    TextureBuffer(false, true, "sampler1D|sampler1DShadow"),
    Texture2D(false, true, "sampler2D|sampler2DShadow", Texture2D.class, Texture.class),
    Texture3D(false, true, "sampler3D", Texture3D.class, Texture.class),
    TextureArray(false, true, "sampler2DArray|sampler2DArrayShadow", TextureArray.class, Texture.class),
    TextureCubeMap(false, true, "samplerCube", TextureCubeMap.class, Texture.class),
    Int("int", int.class, Integer.class),
    UniformBufferObject(false, false, "custom", BufferObject.class),
    ShaderStorageBufferObject(false, false, "custom", BufferObject.class);

    private boolean usesMultiData = false;
    private boolean textureType = false;
    private final String glslType;
    private Class<?>[] javaTypes;

    VarType(String glslType, Class<?>... javaTypes) {
        this.glslType = glslType;
        if (javaTypes != null) {
            this.javaTypes = javaTypes;
        } else {
            this.javaTypes = new Class<?>[0];
        }
    }

    VarType(boolean multiData, boolean textureType, String glslType, Class<?>... javaTypes) {
        this.usesMultiData = multiData;
        this.textureType = textureType;
        this.glslType = glslType;
        if (javaTypes != null) {
            this.javaTypes = javaTypes;
        } else {
            this.javaTypes = new Class<?>[0];
        }
    }

    /**
     * Check if the passed object is of a type mapped to this VarType
     * 
     * @param o Object to check
     * @return true if the object type is mapped to this VarType
     */
    public boolean isOfType(Object o) {
        for (Class<?> c : javaTypes) {
            if (c.isAssignableFrom(o.getClass())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the java types mapped to this VarType
     * 
     * @return an array of classes mapped to this VarType
     */
    public Class<?>[] getJavaType() {
        return javaTypes;
    }

    public boolean isTextureType() {
        return textureType;
    }

    public boolean usesMultiData() {
        return usesMultiData;
    }

    public String getGlslType() {
        return glslType;
    }

}
