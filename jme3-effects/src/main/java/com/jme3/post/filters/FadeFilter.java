
package com.jme3.post.filters;

import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import java.io.IOException;

/**
 *
 * Fade Filter allows you to make an animated fade effect on a scene.
 * @author Rémy Bouquet aka Nehon
 * implemented from boxjar implementation
 * @see <a href="http://jmonkeyengine.org/groups/graphics/forum/topic/newbie-question-general-fade-inout-effect/#post-105559">http://jmonkeyengine.org/groups/graphics/forum/topic/newbie-question-general-fade-inout-effect/#post-105559</a>
 */
public class FadeFilter extends Filter {

    private float value = 1;
    private boolean playing = false;
    private float direction = 1;
    private float duration = 1;

    /**
     * Creates a FadeFilter
     */
    public FadeFilter() {
        super("Fade In/Out");
    }

    /**
     * Creates a FadeFilter with the given duration
     *
     * @param duration the desired duration (in seconds, default=1)
     */
    public FadeFilter(float duration) {
        this();
        this.duration = duration;
    }

    @Override
    protected Material getMaterial() {
        material.setFloat("Value", value);
        return material;
    }

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "Common/MatDefs/Post/Fade.j3md");
    }

    @Override
    protected void preFrame(float tpf) {
        if (playing) {
            value += tpf * direction / duration;

            if (direction > 0 && value > 1) {
                value = 1;
                playing = false;
            }
            if (direction < 0 && value < 0) {
                value = 0;
                playing = false;
            }    
        }
    }

    /**
     * returns the duration of the effect 
     * @return the duration (in seconds)
     */
    public float getDuration() {
        return duration;
    }

    /**
     * Sets the duration of the filter default is 1 second
     *
     * @param duration the desired duration (in seconds, default=1)
     */
    public void setDuration(float duration) {
        this.duration = duration;
    }

    /**
     * fades the scene in (black to scene)
     */
    public void fadeIn() {
        setEnabled(true);
        direction = 1;
        playing = true;
    }

    /**
     * fades the scene out (scene to black)
     */
    public void fadeOut() {
        setEnabled(true);
        direction = -1;
        playing = true;

    }

    public void pause() {
        playing = false;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(duration, "duration", 1);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        duration = ic.readFloat("duration", 1);
    }

    /**
     * Returns the current fade value.
     * Can be used to check whether fade is complete (e.g. value=1).
     * @return the fractional progress (&ge;0, &le;1)
     */
    public float getValue() {
        return value;
    }

    /**
     * Sets the fade value.
     * Can be used to force all black or all scene.
     *
     * @param value the desired value (default=1)
     */
    public void setValue(float value) {
        this.value = value;       
        if (material != null) {
            material.setFloat("Value", value);
        }
    }
}
