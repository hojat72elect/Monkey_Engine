
package com.jme3.light;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.export.*;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;
import java.io.IOException;

/**
 * Abstract class for representing a light source.
 * <p>
 * All light source types have a color.
 */
public abstract class Light implements Savable, Cloneable {

    /**
     * Describes the light type.
     */
    public enum Type {

        /**
         * Directional light
         * 
         * @see DirectionalLight
         */
        Directional(0),
        
        /**
         * Point light
         * 
         * @see PointLight
         */
        Point(1),
        
        /**
         * Spot light.
         * 
         * @see SpotLight
         */
        Spot(2),
        
        /**
         * Ambient light
         * 
         * @see AmbientLight
         */
        Ambient(3),
        
        /**
         * Light probe
         * @see LightProbe
         */
        Probe(4);
                

        private final int typeId;

        Type(int type){
            this.typeId = type;
        }

        /**
         * Returns an index for the light type
         * @return an index for the light type
         */
        public int getId(){
            return typeId;
        }
    }

    protected ColorRGBA color = new ColorRGBA(ColorRGBA.White);
    
    /**
     * Used in LightList for caching the distance 
     * to the owner spatial. Should be reset after the sorting.
     */
    protected transient float lastDistance = -1;

    protected boolean enabled = true;

    /** 
     * The light name. 
     */
    protected String name;
    
    boolean frustumCheckNeeded = true;
    boolean intersectsFrustum  = false;

    protected Light() {
    }

    protected Light(ColorRGBA color) {
        setColor(color);
    }

    /**
     * Returns the color of the light.
     * 
     * @return The color of the light.
     */
    public ColorRGBA getColor() {
        return color;
    }

    /**
     * This method sets the light name.
     * 
     * @param name the light name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Return the light name.
     * 
     * @return the light name
     */
    public String getName() {
        return name;
    }

    /*
    public void setLastDistance(float lastDistance){
        this.lastDistance = lastDistance;
    }

    public float getLastDistance(){
        return lastDistance;
    }
    */

    /**
     * Sets the light color.
     * 
     * @param color the light color.
     */
    public void setColor(ColorRGBA color){
        this.color.set(color);
    }


    /**
     * Returns true if this light is enabled.
     * @return true if enabled, otherwise false.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Set to false in order to disable a light and have it filtered out from being included in rendering.
     *
     * @param enabled true to enable and false to disable the light.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isFrustumCheckNeeded() {
      return frustumCheckNeeded;
    }

    public void setFrustumCheckNeeded(boolean frustumCheckNeeded) {
      this.frustumCheckNeeded = frustumCheckNeeded;
    }

    public boolean isIntersectsFrustum() {
      return intersectsFrustum;
    }

    public void setIntersectsFrustum(boolean intersectsFrustum) {
      this.intersectsFrustum = intersectsFrustum;
    }
    
    /**
     * Determines if the light intersects with the given bounding box.
     * <p>
     * For non-local lights, such as {@link DirectionalLight directional lights},
     * {@link AmbientLight ambient lights}, or {@link PointLight point lights}
     * without influence radius, this method should always return true.
     * 
     * @param box The box to check intersection against.
     * @param vars TempVars in case it is needed.
     * 
     * @return True if the light intersects the box, false otherwise.
     */
    public abstract boolean intersectsBox(BoundingBox box, TempVars vars);
    
    /**
     * Determines if the light intersects with the given bounding sphere.
     * <p>
     * For non-local lights, such as {@link DirectionalLight directional lights},
     * {@link AmbientLight ambient lights}, or {@link PointLight point lights}
     * without influence radius, this method should always return true.
     * 
     * @param sphere The sphere to check intersection against.
     * @param vars TempVars in case it is needed.
     * 
     * @return True if the light intersects the sphere, false otherwise.
     */
    public abstract boolean intersectsSphere(BoundingSphere sphere, TempVars vars);

    /**
     * Determines if the light intersects with the given camera frustum.
     * 
     * For non-local lights, such as {@link DirectionalLight directional lights},
     * {@link AmbientLight ambient lights}, or {@link PointLight point lights}
     * without influence radius, this method should always return true.
     * 
     * @param camera The camera frustum to check intersection against.
     * @param vars TempVars in case it is needed.
     * @return True if the light intersects the frustum, false otherwise.
     */
    public abstract boolean intersectsFrustum(Camera camera, TempVars vars);
    
    @Override
    public Light clone(){
        try {
            Light l = (Light) super.clone();
            l.color = color.clone();
            return l;
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(color, "color", null);
        oc.write(enabled, "enabled", true);
        oc.write(name, "name", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        color = (ColorRGBA) ic.readSavable("color", null);
        enabled = ic.readBoolean("enabled", true);
        name = ic.readString("name", null);
    }

    /**
     * Used internally to compute the last distance value.
     *
     * @param owner the Spatial whose distance is to be determined
     */
    protected abstract void computeLastDistance(Spatial owner);
    
    /**
     * Returns the light type
     * 
     * @return the light type
     * 
     * @see Type
     */
    public abstract Type getType();

}
