
package com.jme3.shader;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import java.io.IOException;

/**
 * A shader node variable
 *
 * @author Nehon
 */
public class ShaderNodeVariable implements Savable, Cloneable {

    private String prefix = "";
    private String name;
    private String type;
    private String nameSpace;
    private String condition;
    private String multiplicity;
    private String defaultValue;

    private boolean shaderOutput = false;

    /**
     * creates a ShaderNodeVariable
     *
     * @param type the glsl type of the variable
     * @param name the name of the variable
     */
    public ShaderNodeVariable(String type, String name) {
        this.name = name;
        this.type = type;
    }

    /**
     * creates a ShaderNodeVariable
     *
     * @param type the glsl type of the variable
     * @param nameSpace the nameSpace (can be the name of the shaderNode or
     * Global,Attr,MatParam,WorldParam)
     * @param name the name of the variable
     * @param multiplicity the number of element if this variable is an array. Can be an Int of a declared material parameter
     */
    public ShaderNodeVariable(String type, String nameSpace, String name, String multiplicity) {
        this.name = name;
        this.nameSpace = nameSpace;
        this.type = type;
        this.multiplicity = multiplicity;
    }


    /**
     * creates a ShaderNodeVariable
     *
     * @param type         the glsl type of the variable
     * @param nameSpace    the nameSpace (can be the name of the shaderNode or
     *                     Global,Attr,MatParam,WorldParam)
     * @param name         the name of the variable
     * @param multiplicity the number of element if this variable is an array. Can be an Int of a declared material parameter
     * @param prefix       the variable prefix to append at generation times. This is mostly to add the g_ and m_ for uniforms
     */
    public ShaderNodeVariable(String type, String nameSpace, String name, String multiplicity, String prefix) {
        this(type, nameSpace, name, multiplicity);
        this.prefix = prefix;
    }

    /**
     * creates a ShaderNodeVariable
     *
     * @param type the glsl type of the variable
     * @param nameSpace the nameSpace (can be the name of the shaderNode or
     * Global,Attr,MatParam,WorldParam)
     * @param name the name of the variable
     */
    public ShaderNodeVariable(String type, String nameSpace, String name) {
        this.name = name;
        this.nameSpace = nameSpace;
        this.type = type;
    }

    /**
     * returns the name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * sets the name
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return the glsl type
     */
    public String getType() {
        return type;
    }

    /**
     * sets the glsl type
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return the name space (can be the name of the shaderNode or
     * Global,Attr,MatParam,WorldParam)
     */
    public String getNameSpace() {
        return nameSpace;
    }

    /**
     * @return the variable prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets the variable prefix (m_ or g_)
     *
     * @param prefix the desired prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * sets the nameSpace (can be the name of the shaderNode or
     * Global,Attr,MatParam,WorldParam)
     *
     * @param nameSpace the desired nameSpace
     */
    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    /**
     * Gets the default value of this variable.
     *
     * @return the default value of this variable.
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Sets the default value of this variable.
     *
     * @param defaultValue the default value of this variable.
     */
    public void setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (name != null?name.hashCode():0);
        hash = 29 * hash + (type != null?type.hashCode():0);
        hash = 29 * hash + (prefix != null ? prefix.hashCode() : 0);
        hash = 29 * hash + (nameSpace != null?nameSpace.hashCode():0);
        hash = 29 * hash + (condition != null?condition.hashCode():0);
        hash = 29 * hash + (multiplicity != null?multiplicity.hashCode():0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ShaderNodeVariable other = (ShaderNodeVariable) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.type == null) ? (other.type != null) : !this.type.equals(other.type)) {
            return false;
        }
        if ((this.prefix == null) ? (other.prefix != null) : !this.prefix.equals(other.prefix)) {
            return false;
        }
        if ((this.nameSpace == null) ? (other.nameSpace != null) : !this.nameSpace.equals(other.nameSpace)) {
            return false;
        }
        if ((this.condition == null) ? (other.condition != null) : !this.condition.equals(other.condition)) {
            return false;
        }
        if ((this.multiplicity == null) ? (other.multiplicity != null) : !this.multiplicity.equals(other.multiplicity)) {
            return false;
        }
        return true;
    }

    /**
     * jme serialization (not used)
     *
     * @param ex the exporter
     * @throws IOException from the exporter
     */
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(name, "name", "");
        oc.write(type, "type", "");
        oc.write(prefix, "prefix", "");
        oc.write(nameSpace, "nameSpace", "");
        oc.write(condition, "condition", null);
        oc.write(shaderOutput, "shaderOutput", false);
        oc.write(multiplicity, "multiplicity", null);
        oc.write(defaultValue, "defaultValue", null);
    }

    /**
     * jme serialization (not used)
     *
     * @param im the importer
     * @throws IOException from the importer
     */
    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        name = ic.readString("name", "");
        type = ic.readString("type", "");
        prefix = ic.readString("prefix", "");
        nameSpace = ic.readString("nameSpace", "");
        condition = ic.readString("condition", null);
        shaderOutput = ic.readBoolean("shaderOutput", false);
        multiplicity = ic.readString("multiplicity", null);
        defaultValue = ic.readString("defaultValue", null);
    }

    /**
     *
     * @return the condition for this variable to be declared
     */
    public String getCondition() {
        return condition;
    }

    /**
     * sets the condition
     *
     * @param condition the condition
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return type + ' ' + (nameSpace != null ? (nameSpace + '.') : "") + name;
    }

    /**
     *
     * @return true if this variable is a shader output
     */
    public boolean isShaderOutput() {
        return shaderOutput;
    }

    /**
     * sets to true if this variable is a shader output
     *
     * @param shaderOutput true if this variable is a shader output
     */
    public void setShaderOutput(boolean shaderOutput) {
        this.shaderOutput = shaderOutput;
    }

    /**
     * 
     * @return the number of elements if this variable is an array
     */
    public String getMultiplicity() {
        return multiplicity;
    }

    /**
     * sets the number of elements of this variable making it an array
     * this value can be a number of can be a define
     *
     * @param multiplicity the desired expression
     */
    public void setMultiplicity(String multiplicity) {
        this.multiplicity = multiplicity;
    }

    @Override
    public ShaderNodeVariable clone() throws CloneNotSupportedException {
        return (ShaderNodeVariable) super.clone();
    }
}
