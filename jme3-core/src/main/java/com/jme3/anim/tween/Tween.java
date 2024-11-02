
package com.jme3.anim.tween;

/**
 * Represents some action that interpolates across input between 0
 * and some length value.  (For example, movement, rotation, fading.)
 * It's also possible to have zero length 'instant' tweens.
 *
 * @author Paul Speed
 */
public interface Tween extends Cloneable {

    /**
     * Returns the length of the tween.  If 't' represents time in
     * seconds then this is the notional time in seconds that the tween
     * will run.  Note: all the caveats are because tweens may be
     * externally scaled in such a way that 't' no longer represents
     * actual time.
     *
     * @return the duration (in de-scaled seconds)
     */
    public double getLength();

    /**
     * Sets the implementation specific interpolation to the
     * specified 'tween' value as a value in the range from 0 to
     * getLength().  If the value is greater or equal to getLength()
     * then it is internally clamped and the method returns false.
     * If 't' is still in the tween's range then this method returns
     * true.
     *
     * @param t animation time (in de-scaled seconds)
     * @return true if t&gt;length(), otherwise false
     */
    public boolean interpolate(double t);

}

