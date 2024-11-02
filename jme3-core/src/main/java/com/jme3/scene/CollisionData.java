
package com.jme3.scene;

import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResults;
import com.jme3.export.Savable;
import com.jme3.math.Matrix4f;

/**
 * <code>CollisionData</code> is an interface that can be used to 
 * do triangle-accurate collision with bounding volumes and rays.
 *
 * @author Kirill Vainer
 */
public interface CollisionData extends Savable, Cloneable {
    public int collideWith(Collidable other,
                           Matrix4f worldMatrix,
                           BoundingVolume worldBound,
                           CollisionResults results);
}
