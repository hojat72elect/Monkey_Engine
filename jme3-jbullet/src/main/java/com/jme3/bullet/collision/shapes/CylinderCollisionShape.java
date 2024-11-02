
package com.jme3.bullet.collision.shapes;

import com.bulletphysics.collision.shapes.CylinderShape;
import com.bulletphysics.collision.shapes.CylinderShapeX;
import com.bulletphysics.collision.shapes.CylinderShapeZ;
import com.jme3.bullet.util.Converter;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector3f;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Basic cylinder collision shape
 * @author normenhansen
 */
public class CylinderCollisionShape extends CollisionShape {

    protected Vector3f halfExtents;
    protected int axis;

    protected CylinderCollisionShape() {
    }

    /**
     * creates a cylinder shape from the given halfextents
     * @param halfExtents the halfextents to use
     */
    public CylinderCollisionShape(Vector3f halfExtents) {
        this.halfExtents = halfExtents;
        this.axis = 2;
        createShape();
    }

    /**
     * Creates a cylinder shape around the given axis from the given halfextents
     * @param halfExtents the halfextents to use
     * @param axis (0=X,1=Y,2=Z)
     */
    public CylinderCollisionShape(Vector3f halfExtents, int axis) {
        this.halfExtents = halfExtents;
        this.axis = axis;
        createShape();
    }

    public final Vector3f getHalfExtents() {
        return halfExtents;
    }

    public int getAxis() {
        return axis;
    }

    /**
     * WARNING - CompoundCollisionShape scaling has no effect.
     */
    @Override
    public void setScale(Vector3f scale) {
        if (!scale.equals(Vector3f.UNIT_XYZ)) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "CylinderCollisionShape cannot be scaled");
        }
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(halfExtents, "halfExtents", new Vector3f(0.5f, 0.5f, 0.5f));
        capsule.write(axis, "axis", 1);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        halfExtents = (Vector3f) capsule.readSavable("halfExtents", new Vector3f(0.5f, 0.5f, 0.5f));
        axis = capsule.readInt("axis", 1);
        createShape();
    }

    protected void createShape() {
        switch (axis) {
            case 0:
                cShape = new CylinderShapeX(Converter.convert(halfExtents));
                break;
            case 1:
                cShape = new CylinderShape(Converter.convert(halfExtents));
                break;
            case 2:
                cShape = new CylinderShapeZ(Converter.convert(halfExtents));
                break;
        }
        cShape.setLocalScaling(Converter.convert(getScale()));
        cShape.setMargin(margin);
    }

}
