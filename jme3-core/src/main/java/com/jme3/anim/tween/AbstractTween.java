
package com.jme3.anim.tween;

import com.jme3.util.clone.Cloner;
import com.jme3.util.clone.JmeCloneable;

/**
 * Base implementation of the Tween interface that provides
 * default implementations of the getLength() and interpolate()
 * methods that provide common tween clamping and bounds checking.
 * Subclasses need only override the doInterpolate() method and
 * the rest is handled for them.
 *
 * @author Paul Speed
 */
public abstract class AbstractTween implements JmeCloneable, Tween {

    private double length;

    protected AbstractTween(double length) {
        this.length = length;
    }

    @Override
    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        if (length < 0.0) {
            throw new IllegalArgumentException("length must be greater than or equal to 0");
        }
        this.length = length;
    }

    /**
     * Default implementation clamps the time value, converts
     * it to 0 to 1.0 based on getLength(), and calls doInterpolate().
     */
    @Override
    public boolean interpolate(double t) {
        if (t < 0) {
            return true;
        }

        // Scale t to be between 0 and 1 for our length
        if (length == 0) {
            t = 1;
        } else {
            t = t / length;
        }

        boolean done = false;
        if (t >= 1.0) {
            t = 1.0;
            done = true;
        }
        doInterpolate(t);
        return !done;
    }

    protected abstract void doInterpolate(double t);

    /**
     * Create a shallow clone for the JME cloner.
     *
     * @return a new tween (not null)
     */
    @Override
    public AbstractTween jmeClone() {
        try {
            AbstractTween clone = (AbstractTween) super.clone();
            return clone;
        } catch (CloneNotSupportedException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Callback from {@link com.jme3.util.clone.Cloner} to convert this
     * shallow-cloned tween into a deep-cloned one, using the specified cloner
     * and original to resolve copied fields.
     *
     * @param cloner the cloner that's cloning this tween (not null)
     * @param original the tween from which this tween was shallow-cloned
     * (unused)
     */
    @Override
    public void cloneFields(Cloner cloner, Object original) {
    }
}
