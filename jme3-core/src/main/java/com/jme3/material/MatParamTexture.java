
package com.jme3.material;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.shader.VarType;
import com.jme3.texture.Texture;
import com.jme3.texture.image.ColorSpace;
import java.io.IOException;

/**
 * A material parameter that holds a reference to a texture and its required color space.
 * This class extends {@link MatParam} to provide texture specific functionalities.
 */
public class MatParamTexture extends MatParam {

    private ColorSpace colorSpace;

    /**
     * Constructs a new MatParamTexture instance with the specified type, name,
     * texture, and color space.
     *
     * @param type       the type of the material parameter
     * @param name       the name of the parameter
     * @param texture    the texture associated with this parameter
     * @param colorSpace the required color space for the texture
     */
    public MatParamTexture(VarType type, String name, Texture texture, ColorSpace colorSpace) {
        super(type, name, texture);
        this.colorSpace = colorSpace;
    }

    /**
     * Serialization only. Do not use.
     */
    public MatParamTexture() {
    }

    /**
     * Retrieves the texture associated with this material parameter.
     *
     * @return the texture object
     */
    public Texture getTextureValue() {
        return (Texture) getValue();
    }

    /**
     * Sets the texture associated with this material parameter.
     *
     * @param value the texture object to set
     * @throws RuntimeException if the provided value is not a {@link Texture}
     */
    public void setTextureValue(Texture value) {
        setValue(value);
    }

    /**
     * Gets the required color space for this texture parameter.
     * 
     * @return the required color space ({@link ColorSpace})
     */
    public ColorSpace getColorSpace() {
        return colorSpace;
    }

    /**
     * Set to {@link ColorSpace#Linear} if the texture color space has to be forced
     * to linear instead of sRGB.
     * 
     * @param colorSpace the desired color space
     */
    public void setColorSpace(ColorSpace colorSpace) {
        this.colorSpace = colorSpace;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(colorSpace, "colorSpace", null);
        // For backwards compatibility
        oc.write(0, "texture_unit", -1);
        oc.write((Texture) value, "texture", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        colorSpace = ic.readEnum("colorSpace", ColorSpace.class, null);
    }
    
}
