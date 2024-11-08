
package com.jme3.bullet.collision;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.export.*;
import java.io.IOException;

/**
 * Base class for collision objects (PhysicsRigidBody, PhysicsGhostObject)
 * @author normenhansen
 */
public abstract class PhysicsCollisionObject implements Savable {

    protected CollisionShape collisionShape;
    public static final int COLLISION_GROUP_NONE = 0x00000000;
    public static final int COLLISION_GROUP_01 = 0x00000001;
    public static final int COLLISION_GROUP_02 = 0x00000002;
    public static final int COLLISION_GROUP_03 = 0x00000004;
    public static final int COLLISION_GROUP_04 = 0x00000008;
    public static final int COLLISION_GROUP_05 = 0x00000010;
    public static final int COLLISION_GROUP_06 = 0x00000020;
    public static final int COLLISION_GROUP_07 = 0x00000040;
    public static final int COLLISION_GROUP_08 = 0x00000080;
    public static final int COLLISION_GROUP_09 = 0x00000100;
    public static final int COLLISION_GROUP_10 = 0x00000200;
    public static final int COLLISION_GROUP_11 = 0x00000400;
    public static final int COLLISION_GROUP_12 = 0x00000800;
    public static final int COLLISION_GROUP_13 = 0x00001000;
    public static final int COLLISION_GROUP_14 = 0x00002000;
    public static final int COLLISION_GROUP_15 = 0x00004000;
    public static final int COLLISION_GROUP_16 = 0x00008000;
    protected int collisionGroup = 0x00000001;
    protected int collisionGroupsMask = 0x00000001;
    private Object userObject;

    /**
     * Sets a CollisionShape to this physics object, note that the object should
     * not be in the physics space when adding a new collision shape as it is rebuilt
     * on the physics side.
     * @param collisionShape the CollisionShape to set
     */
    public void setCollisionShape(CollisionShape collisionShape) {
        this.collisionShape = collisionShape;
    }

    /**
     * @return the CollisionShape of this PhysicsNode, to be able to reuse it with
     * other physics nodes (increases performance)
     */
    public CollisionShape getCollisionShape() {
        return collisionShape;
    }

    /**
     * Returns the collision group for this collision shape
     * @return a bitmask with 1 bit set
     */
    public int getCollisionGroup() {
        return collisionGroup;
    }

    /**
     * Sets the collision group number for this physics object. <br>
     * The groups are integer bit masks and some pre-made variables are available in CollisionObject.
     * All physics objects are by default in COLLISION_GROUP_01.<br>
     * Two object will collide when <b>one</b> of the parties has the
     * collisionGroup of the other in its collideWithGroups set.
     * @param collisionGroup the collisionGroup to set
     */
    public void setCollisionGroup(int collisionGroup) {
        this.collisionGroup = collisionGroup;
    }

    /**
     * Add a group that this object will collide with.<br>
     * Two object will collide when <b>one</b> of the parties has the
     * collisionGroup of the other in its collideWithGroups set.<br>
     *
     * @param collisionGroup the groups to add, ORed together (bitmask)
     */
    public void addCollideWithGroup(int collisionGroup) {
        this.collisionGroupsMask = this.collisionGroupsMask | collisionGroup;
    }

    /**
     * Remove a group from the list this object collides with.
     *
     * @param collisionGroup the groups to remove, ORed together (bitmask)
     */
    public void removeCollideWithGroup(int collisionGroup) {
        this.collisionGroupsMask = this.collisionGroupsMask & ~collisionGroup;
    }

    /**
     * Directly set the bitmask for collision groups that this object collides with.
     *
     * @param collisionGroups the desired groups, ORed together (bitmask,
     * default=COLLISION_GROUP_01)
     */
    public void setCollideWithGroups(int collisionGroups) {
        this.collisionGroupsMask = collisionGroups;
    }

    /**
     * Gets the bitmask of collision groups that this object collides with.
     * @return a bitmask
     */
    public int getCollideWithGroups() {
        return collisionGroupsMask;
    }

    /**
     * @return the userObject
     */
    public Object getUserObject() {
        return userObject;
    }

    /**
     * @param userObject the userObject to set
     */
    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    @Override
    public void write(JmeExporter e) throws IOException {
        OutputCapsule capsule = e.getCapsule(this);
        capsule.write(collisionGroup, "collisionGroup", 0x00000001);
        capsule.write(collisionGroupsMask, "collisionGroupsMask", 0x00000001);
        capsule.write(collisionShape, "collisionShape", null);
    }

    @Override
    public void read(JmeImporter importer) throws IOException {
        InputCapsule capsule = importer.getCapsule(this);
        collisionGroup = capsule.readInt("collisionGroup", 0x00000001);
        collisionGroupsMask = capsule.readInt("collisionGroupsMask", 0x00000001);
        CollisionShape shape = (CollisionShape) capsule.readSavable("collisionShape", null);
        collisionShape = shape;
    }
}
