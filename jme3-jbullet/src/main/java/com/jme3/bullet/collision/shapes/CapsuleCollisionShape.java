
package com.jme3.bullet.collision.shapes;

import com.bulletphysics.collision.shapes.CapsuleShape;
import com.bulletphysics.collision.shapes.CapsuleShapeX;
import com.bulletphysics.collision.shapes.CapsuleShapeZ;
import com.jme3.bullet.util.Converter;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import java.io.IOException;

/**
 * Basic capsule collision shape
 * @author normenhansen
 */
public class CapsuleCollisionShape extends CollisionShape{
    protected float radius,height;
    protected int axis;

    protected CapsuleCollisionShape() {
    }

    /**
     * Creates a new CapsuleCollisionShape with the given radius and height.
     * The capsule is oriented along the Y axis (1).
     * @param radius the radius of the capsule
     * @param height the height of the capsule
     */
    public CapsuleCollisionShape(float radius, float height) {
        this.radius=radius;
        this.height=height;
        this.axis=1;
        CapsuleShape capShape=new CapsuleShape(radius,height);
        cShape=capShape;
    }

    /**
     * Creates a capsule shape around the given axis (0=X,1=Y,2=Z).
     *
     * @param radius the desired unscaled radius
     * @param height the desired unscaled height of the cylindrical portion
     * @param axis which local axis for the height: 0&rarr;X, 1&rarr;Y,
     * 2&rarr;Z
     */
    public CapsuleCollisionShape(float radius, float height, int axis) {
        this.radius=radius;
        this.height=height;
        this.axis=axis;
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
        capsule.write(height, "height", 1);
        capsule.write(axis, "axis", 1);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        radius = capsule.readFloat("radius", 0.5f);
        height = capsule.readFloat("height", 0.5f);
        axis = capsule.readInt("axis", 1);
        createShape();
    }

    protected void createShape(){
        switch(axis){
            case 0:
                cShape=new CapsuleShapeX(radius,height);
            break;
            case 1:
                cShape=new CapsuleShape(radius,height);
            break;
            case 2:
                cShape=new CapsuleShapeZ(radius,height);
            break;
        }
        cShape.setLocalScaling(Converter.convert(getScale()));
        cShape.setMargin(margin);
    }

}
