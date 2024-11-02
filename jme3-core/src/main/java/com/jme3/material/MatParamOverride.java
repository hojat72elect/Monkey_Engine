
package com.jme3.material;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.scene.Spatial;
import com.jme3.shader.VarType;
import java.io.IOException;

/**
 * <code>MatParamOverride</code> is a mechanism by which
 * {@link MatParam material parameters} can be overridden on the scene graph.
 * <p>
 * A scene branch which has a <code>MatParamOverride</code> applied to it will
 * cause all material parameters with the same name and type to have their value
 * replaced with the value set on the <code>MatParamOverride</code>. If those
 * parameters are mapped to a define, then the define will be overridden as well
 * using the same rules as the ones used for regular material parameters.
 * <p>
 * <code>MatParamOverrides</code> are applied to a {@link Spatial} via the
 * {@link Spatial#addMatParamOverride(com.jme3.material.MatParamOverride)}
 * method. They are propagated to child <code>Spatials</code> via
 * {@link Spatial#updateGeometricState()} similar to how lights are propagated.
 * <p>
 * Example:<br>
 * <pre>
 * {@code
 *
 * Geometry box = new Geometry("Box", new Box(1,1,1));
 * Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
 * mat.setColor("Color", ColorRGBA.Blue);
 * box.setMaterial(mat);
 * rootNode.attachChild(box);
 *
 * // ... later ...
 * MatParamOverride override = new MatParamOverride(Type.Vector4, "Color", ColorRGBA.Red);
 * rootNode.addMatParamOverride(override);
 *
 * // After adding the override to the root node, the box becomes red.
 * }
 * </pre>
 *
 * @author Kirill Vainer
 * @see Spatial#addMatParamOverride(com.jme3.material.MatParamOverride)
 * @see Spatial#getWorldMatParamOverrides()
 */
public final class MatParamOverride extends MatParam {

    private boolean enabled = true;

    /**
     * Serialization only. Do not use.
     */
    protected MatParamOverride() {
        super();
    }

    /**
     * Create a new <code>MatParamOverride</code>.
     *
     * Overrides are created enabled by default.
     *
     * @param type The type of parameter.
     * @param name The name of the parameter.
     * @param value The value to set the material parameter to.
     */
    public MatParamOverride(VarType type, String name, Object value) {
        super(type, name, value);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) && this.enabled == ((MatParamOverride) obj).enabled;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 59 * hash + (enabled ? 1 : 0);
        return hash;
    }

    /**
     * Determine if the <code>MatParamOverride</code> is enabled or disabled.
     *
     * @return true if enabled, false if disabled.
     * @see #setEnabled(boolean)
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Enable or disable this <code>MatParamOverride</code>.
     *
     * When disabled, the override will continue to propagate through the scene
     * graph like before, but it will have no effect on materials. Overrides are
     * enabled by default.
     *
     * @param enabled Whether to enable or disable this override.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(enabled, "enabled", true);
        if (value == null) {
            oc.write(true, "isNull", false);
        }
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        enabled = ic.readBoolean("enabled", true);
        boolean isNull = ic.readBoolean("isNull", false);
        if (isNull) {
            setValue(null);
        }
    }
}
