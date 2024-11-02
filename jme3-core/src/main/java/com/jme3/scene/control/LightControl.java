
package com.jme3.scene.control;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.util.TempVars;
import com.jme3.util.clone.Cloner;

import java.io.IOException;

/**
 * This Control maintains a reference to a Light,
 * which will be synched with the position (worldTranslation)
 * of the current spatial.
 *
 * @author tim
 */
public class LightControl extends AbstractControl {

    private static final String CONTROL_DIR_NAME = "controlDir";
    private static final String LIGHT_NAME = "light";

    public enum ControlDirection {

        /**
         * Means, that the Light's transform is "copied"
         * to the Transform of the Spatial.
         */
        LightToSpatial,
        /**
         * Means, that the Spatial's transform is "copied"
         * to the Transform of the light.
         */
        SpatialToLight
    }

    private Light light;
    private ControlDirection controlDir = ControlDirection.SpatialToLight;

    /**
     * Constructor used for Serialization.
     */
    public LightControl() {
    }

    /**
     * @param light The light to be synced.
     */
    public LightControl(Light light) {
        this.light = light;
    }

    /**
     * @param light The light to be synced.
     * @param controlDir SpatialToLight or LightToSpatial
     */
    public LightControl(Light light, ControlDirection controlDir) {
        this.light = light;
        this.controlDir = controlDir;
    }

    public Light getLight() {
        return light;
    }

    public void setLight(Light light) {
        this.light = light;
    }

    public ControlDirection getControlDir() {
        return controlDir;
    }

    public void setControlDir(ControlDirection controlDir) {
        this.controlDir = controlDir;
    }

    // fields used when inverting ControlDirection:
    @Override
    protected void controlUpdate(float tpf) {
        if (spatial != null && light != null) {
            switch (controlDir) {
                case SpatialToLight:
                    spatialToLight(light);
                    break;
                case LightToSpatial:
                    lightToSpatial(light);
                    break;
            }
        }
    }

    /**
     * Sets the light to adopt the spatial's world transformations.
     *
     * @author Markil 3
     * @author pspeed42
     */
    private void spatialToLight(Light light) {
        TempVars vars = TempVars.get();

        final Vector3f worldTranslation = vars.vect1;
        worldTranslation.set(spatial.getWorldTranslation());
        final Vector3f worldDirection = vars.vect2;
        spatial.getWorldRotation().mult(Vector3f.UNIT_Z, worldDirection).negateLocal();

        if (light instanceof PointLight) {
            ((PointLight) light).setPosition(worldTranslation);
        } else if (light instanceof DirectionalLight) {
            ((DirectionalLight) light).setDirection(worldDirection);
        } else if (light instanceof SpotLight) {
            final SpotLight spotLight = (SpotLight) light;
            spotLight.setPosition(worldTranslation);
            spotLight.setDirection(worldDirection);
        }
        vars.release();
    }

    /**
     * Sets the spatial to adopt the light's world transformations.
     *
     * @author Markil 3
     */
    private void lightToSpatial(Light light) {
        TempVars vars = TempVars.get();
        Vector3f translation = vars.vect1;
        Vector3f direction = vars.vect2;
        Quaternion rotation = vars.quat1;
        boolean rotateSpatial = false, translateSpatial = false;

        if (light instanceof PointLight) {
            PointLight pLight = (PointLight) light;
            translation.set(pLight.getPosition());
            translateSpatial = true;
        } else if (light instanceof DirectionalLight) {
            DirectionalLight dLight = (DirectionalLight) light;
            direction.set(dLight.getDirection()).negateLocal();
            rotateSpatial = true;
        } else if (light instanceof SpotLight) {
            SpotLight sLight = (SpotLight) light;
            translation.set(sLight.getPosition());
            direction.set(sLight.getDirection()).negateLocal();
            translateSpatial = rotateSpatial = true;
        }
        if (spatial.getParent() != null) {
            spatial.getParent().getLocalToWorldMatrix(vars.tempMat4).invertLocal();
            vars.tempMat4.rotateVect(translation);
            vars.tempMat4.translateVect(translation);
            vars.tempMat4.rotateVect(direction);
        }

        if (rotateSpatial) {
            rotation.lookAt(direction, Vector3f.UNIT_Y).normalizeLocal();
            spatial.setLocalRotation(rotation);
        }
        if (translateSpatial) {
            spatial.setLocalTranslation(translation);
        }
        vars.release();
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        // nothing to do
    }

    @Override
    public void cloneFields(final Cloner cloner, final Object original) {
        super.cloneFields(cloner, original);
        light = cloner.clone(light);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        controlDir = ic.readEnum(CONTROL_DIR_NAME, ControlDirection.class, ControlDirection.SpatialToLight);
        light = (Light) ic.readSavable(LIGHT_NAME, null);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(controlDir, CONTROL_DIR_NAME, ControlDirection.SpatialToLight);
        oc.write(light, LIGHT_NAME, null);
    }
}