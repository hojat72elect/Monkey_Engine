
package com.jme3.material;

import java.io.IOException;
import java.util.Arrays;

import com.jme3.asset.TextureKey;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Matrix4f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.shader.VarType;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

/**
 * Describes a material parameter. This is used for both defining a name and type
 * as well as a material parameter value.
 *
 * @author Kirill Vainer
 */
public class MatParam implements Savable, Cloneable {

    protected VarType type;
    protected String name;
    protected String prefixedName;
    protected Object value;
    protected boolean typeCheck = true;

    /**
     * Create a new material parameter. For internal use only.
     *
     * @param type  the type of the parameter
     * @param name  the desired parameter name
     * @param value the desired parameter value (alias created)
     */
    public MatParam(VarType type, String name, Object value) {
        this.type = type;
        this.name = name;
        this.prefixedName = "m_" + name;
        this.value = value;
    }

    /**
     * Serialization only. Do not use.
     */
    protected MatParam() {
    }

    public boolean isTypeCheckEnabled() {
        return typeCheck;
    }

    /**
     * Enable type check for this param.
     * When type check is enabled a RuntimeException is thrown if 
     * an object of the wrong type is passed to setValue.
     * 
     * @param typeCheck (default = true)
     */
    public void setTypeCheckEnabled(boolean typeCheck) {
        this.typeCheck = typeCheck;
    }

    /**
     * Returns the material parameter type.
     *
     * @return the material parameter type.
     */
    public VarType getVarType() {
        return type;
    }

    /**
     * Returns the name of the material parameter.
     * 
     * @return the name of the material parameter.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the name with "m_" prefixed to it.
     *
     * @return the name with "m_" prefixed to it
     */
    public String getPrefixedName() {
        return prefixedName;
    }

    /**
     * Used internally
     * @param name
     */
    void setName(String name) {
        this.name = name;
        this.prefixedName = "m_" + name;
    }

    /**
     * Returns the value of this material parameter.
     * <p>
     * Material parameters that are used for material definitions
     * will not have a value, unless there's a default value declared
     * in the definition.
     *
     * @return the value of this material parameter.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value of this material parameter.
     * <p>
     * It is assumed the value is of the same {@link MatParam#getVarType() type}
     * as this material parameter.
     *
     * @param value the value of this material parameter.
     */
    public void setValue(Object value) {
        if (isTypeCheckEnabled()) {
            if (value != null && this.type != null && this.type.getJavaType().length != 0) {
                boolean valid = false;
                for (Class<?> jtype : this.type.getJavaType()) {
                    if (jtype.isAssignableFrom(value.getClass())) {
                        valid = true;
                        break;
                    }
                }
                if (!valid) {
                    throw new RuntimeException("Trying to assign a value of type " + value.getClass() 
                            + " to " + this.getName() 
                            + " of type " + type.name() 
                            + ". Valid types are " + Arrays.deepToString(type.getJavaType()));
                }
            }
        }
        this.value = value;
    }

    /**
     * Returns the material parameter value as it would appear in a J3M
     * file. E.g.
     * <pre>
     * MaterialParameters {
     *     ABC : 1 2 3 4
     * }
     * </pre>
     * Assuming "ABC" is a Vector4 parameter, then the value
     * "1 2 3 4" would be returned by this method.
     *
     * @return material parameter value as it would appear in a J3M file.
     */
    public String getValueAsString() {
        switch (type) {
            case Boolean:
            case Float:
            case Int:
                return value.toString();
            case Vector2:
                Vector2f v2 = (Vector2f) value;
                return v2.getX() + " " + v2.getY();
/*
This may get used at a later point of time
When arrays can be inserted in J3M files

            case Vector2Array:
                Vector2f[] v2Arr = (Vector2f[]) value;
                String v2str = "";
                for (int i = 0; i < v2Arr.length ; i++) {
                    v2str += v2Arr[i].getX() + " " + v2Arr[i].getY() + "\n";
                }
                return v2str;
*/
            case Vector3:
                Vector3f v3 = (Vector3f) value;
                return v3.getX() + " " + v3.getY() + " " + v3.getZ();
/*
            case Vector3Array:
                Vector3f[] v3Arr = (Vector3f[]) value;
                String v3str = "";
                for (int i = 0; i < v3Arr.length ; i++) {
                    v3str += v3Arr[i].getX() + " "
                            + v3Arr[i].getY() + " "
                            + v3Arr[i].getZ() + "\n";
                }
                return v3str;
            case Vector4Array:
                // can be either ColorRGBA, Vector4f or Quaternion
                if (value instanceof Vector4f) {
                    Vector4f[] v4arr = (Vector4f[]) value;
                    String v4str = "";
                    for (int i = 0; i < v4arr.length ; i++) {
                        v4str += v4arr[i].getX() + " "
                                + v4arr[i].getY() + " "
                                + v4arr[i].getZ() + " "
                                + v4arr[i].getW() + "\n";
                    }
                    return v4str;
                } else if (value instanceof ColorRGBA) {
                    ColorRGBA[] colorArr = (ColorRGBA[]) value;
                    String colStr = "";
                    for (int i = 0; i < colorArr.length ; i++) {
                        colStr += colorArr[i].getRed() + " "
                                + colorArr[i].getGreen() + " "
                                + colorArr[i].getBlue() + " "
                                + colorArr[i].getAlpha() + "\n";
                    }
                    return colStr;
                } else if (value instanceof Quaternion) {
                    Quaternion[] quatArr = (Quaternion[]) value;
                    String quatStr = "";
                    for (int i = 0; i < quatArr.length ; i++) {
                        quatStr += quatArr[i].getX() + " "
                                + quatArr[i].getY() + " "
                                + quatArr[i].getZ() + " "
                                + quatArr[i].getW() + "\n";
                    }
                    return quatStr;
                } else {
                    throw new UnsupportedOperationException("Unexpected Vector4Array type: " + value);
                }
*/
            case Vector4:
                // can be either ColorRGBA, Vector4f or Quaternion
                if (value instanceof Vector4f) {
                    Vector4f v4 = (Vector4f) value;
                    return v4.getX() + " " + v4.getY() + " "
                            + v4.getZ() + " " + v4.getW();
                } else if (value instanceof ColorRGBA) {
                    ColorRGBA color = (ColorRGBA) value;
                    return color.getRed() + " " + color.getGreen() + " "
                            + color.getBlue() + " " + color.getAlpha();
                } else if (value instanceof Quaternion) {
                    Quaternion quat = (Quaternion) value;
                    return quat.getX() + " " + quat.getY() + " "
                            + quat.getZ() + " " + quat.getW();
                } else {
                    throw new UnsupportedOperationException("Unexpected Vector4 type: " + value);
                }
            case Texture2D:
            case Texture3D:
            case TextureArray:
            case TextureBuffer:
            case TextureCubeMap:
                Texture texVal = (Texture) value;
                TextureKey texKey = (TextureKey) texVal.getKey();
                if (texKey == null) {
                    // throw new UnsupportedOperationException("The specified MatParam cannot be represented in J3M");
                    // this is used in toString and the above line causes blender materials to throw this exception.
                    // toStrings should be very robust IMO as even debuggers often invoke toString and logging code
                    // often does as well, even implicitly.
                    return texVal + ":returned null key";
                }

                String ret = "";
                if (texKey.isFlipY()) {
                    ret += "Flip ";
                }

                // Wrap mode
                ret += getWrapMode(texVal, Texture.WrapAxis.S);
                ret += getWrapMode(texVal, Texture.WrapAxis.T);
                ret += getWrapMode(texVal, Texture.WrapAxis.R);

                // Min and Mag filter
                Texture.MinFilter def = Texture.MinFilter.BilinearNoMipMaps;
                if (texVal.getImage().hasMipmaps() || texKey.isGenerateMips()) {
                    def = Texture.MinFilter.Trilinear;
                }
                if (texVal.getMinFilter() != def) {
                    ret += "Min" + texVal.getMinFilter().name() + " ";
                }

                if (texVal.getMagFilter() != Texture.MagFilter.Bilinear) {
                    ret += "Mag" + texVal.getMagFilter().name() + " ";
                }

                return ret + "\"" + texKey.getName() + "\"";
            default:
                return null; // parameter type not supported in J3M
        }
    }

    private String getWrapMode(Texture texVal, Texture.WrapAxis axis) {
        WrapMode mode = WrapMode.EdgeClamp;
        try {
            mode = texVal.getWrap(axis);
        } catch (IllegalArgumentException ex) {
            // this axis doesn't exist on the texture
            return "";
        }
        if (mode != WrapMode.EdgeClamp) {
            return "Wrap" + mode.name() + "_" + axis.name() + " ";
        }
        return "";
    }

    @Override
    public MatParam clone() {
        try {
            MatParam param = (MatParam) super.clone();
            return param;
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(type, "varType", null);
        oc.write(name, "name", null);

        if (value == null) {
            return;
        }

        if (value instanceof Savable) {
            oc.write((Savable) value, "value_savable", null);
        } else if (value instanceof Float) {
            oc.write((Float) value, "value_float", 0f);
        } else if (value instanceof Integer) {
            oc.write((Integer) value, "value_int", 0);
        } else if (value instanceof Boolean) {
            oc.write((Boolean) value, "value_bool", false);
        } else if (value.getClass().isArray() && value instanceof Savable[]) {
            oc.write((Savable[]) value, "value_savable_array", null);
        }

        oc.write(typeCheck, "typeCheck", true);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        type = ic.readEnum("varType", VarType.class, null);
        name = ic.readString("name", null);
        prefixedName = "m_" + name;
        switch (getVarType()) {
            case Boolean:
                value = ic.readBoolean("value_bool", false);
                break;
            case Float:
                value = ic.readFloat("value_float", 0f);
                break;
            case Int:
                value = ic.readInt("value_int", 0);
                break;
            case Vector2Array:
                Savable[] savableArray = ic.readSavableArray("value_savable_array", null);
                if (savableArray != null) {
                    value = new Vector2f[savableArray.length];
                    System.arraycopy(savableArray, 0, value, 0, savableArray.length);
                }
                break;
            case Vector3Array:
                savableArray = ic.readSavableArray("value_savable_array", null);
                if (savableArray != null) {
                    value = new Vector3f[savableArray.length];
                    System.arraycopy(savableArray, 0, value, 0, savableArray.length);
                }
                break;
            case Vector4Array:
                savableArray = ic.readSavableArray("value_savable_array", null);
                if (savableArray != null) {
                    value = new Vector4f[savableArray.length];
                    System.arraycopy(savableArray, 0, value, 0, savableArray.length);
                }
                break;
            case Matrix3Array:
                savableArray = ic.readSavableArray("value_savable_array", null);
                if (savableArray != null) {
                    value = new Matrix3f[savableArray.length];
                    System.arraycopy(savableArray, 0, value, 0, savableArray.length);
                }
                break;
            case Matrix4Array:
                savableArray = ic.readSavableArray("value_savable_array", null);
                if (savableArray != null) {
                    value = new Matrix4f[savableArray.length];
                    System.arraycopy(savableArray, 0, value, 0, savableArray.length);
                }
                break;
            default:
                value = ic.readSavable("value_savable", null);
                break;
        }

        typeCheck = ic.readBoolean("typeCheck", true);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MatParam other = (MatParam) obj;
        if (this.type != other.type) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 59 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 59 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        if (value != null) {
            return type.name() + " " + name + " : " + getValueAsString();
        } else {
            return type.name() + " " + name;
        }
    }
}
