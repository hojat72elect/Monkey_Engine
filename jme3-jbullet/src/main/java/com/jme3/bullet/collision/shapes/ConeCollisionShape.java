
package com.jme3.bullet.collision.shapes;

import com.bulletphysics.collision.shapes.ConeShape;
import com.bulletphysics.collision.shapes.ConeShapeX;
import com.bulletphysics.collision.shapes.ConeShapeZ;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.util.Converter;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import java.io.IOException;

/**
 * Cone collision shape represents a 3D cone with a radius, height, and axis (X, Y or Z).
 *
 * @author normenhansen
 */
public class ConeCollisionShape extends CollisionShape {

    protected float radius;
    protected float height;
    protected int axis;

    /**
     * Serialization only, do not use.
     */
    protected ConeCollisionShape() {
    }

    /**
     * Creates a new cone collision shape with the given height, radius, and axis.
     *
     * @param radius The radius of the cone in world units.
     * @param height The height of the cone in world units.
     * @param axis The axis towards which the cone faces, see the PhysicsSpace.AXIS_* constants.
     */
    public ConeCollisionShape(float radius, float height, int axis) {
        this.radius = radius;
        this.height = height;
        this.axis = axis;
        if (axis < PhysicsSpace.AXIS_X || axis > PhysicsSpace.AXIS_Z) {
            throw new UnsupportedOperationException("axis must be one of the PhysicsSpace.AXIS_* constants!");
        }
        createShape();
    }

    /**
     * Creates a new cone collision shape with the given height, radius and default Y axis.
     *
     * @param radius The radius of the cone in world units.
     * @param height The height of the cone in world units.
     */
    public ConeCollisionShape(float radius, float height) {
        this.radius = radius;
        this.height = height;
        this.axis = PhysicsSpace.AXIS_Y;
        createShape();
    }

    public float getRadius() {
        return radius;
    }
    
    public float getHeight() {
        return height;
    }
    
    public int getAxis() {
        return axis;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(radius, "radius", 0.5f);
        capsule.write(height, "height", 0.5f);
        capsule.write(axis, "axis", PhysicsSpace.AXIS_Y);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        radius = capsule.readFloat("radius", 0.5f);
        height = capsule.readFloat("height", 0.5f);
        axis = capsule.readInt("axis", PhysicsSpace.AXIS_Y);
        createShape();
    }

    protected void createShape() {
        if (axis == PhysicsSpace.AXIS_X) {
            cShape = new ConeShapeX(radius, height);
        } else if (axis == PhysicsSpace.AXIS_Y) {
            cShape = new ConeShape(radius, height);
        } else if (axis == PhysicsSpace.AXIS_Z) {
            cShape = new ConeShapeZ(radius, height);
        } else {
            throw new UnsupportedOperationException("Unexpected axis: " + axis);
        }
        cShape.setLocalScaling(Converter.convert(getScale()));
        cShape.setMargin(margin);
    }
}
