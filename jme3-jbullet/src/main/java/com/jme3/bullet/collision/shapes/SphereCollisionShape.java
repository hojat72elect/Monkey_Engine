
package com.jme3.bullet.collision.shapes;

import com.bulletphysics.collision.shapes.SphereShape;
import com.jme3.bullet.util.Converter;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import java.io.IOException;

/**
 * Basic sphere collision shape
 * @author normenhansen
 */
public class SphereCollisionShape extends CollisionShape {

    protected float radius;

    protected SphereCollisionShape() {
    }

    /**
     * creates a SphereCollisionShape with the given radius
     *
     * @param radius the desired radius (in unscaled units)
     */
    public SphereCollisionShape(float radius) {
        this.radius = radius;
        createShape();
    }

    public float getRadius() {
        return radius;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(radius, "radius", 0.5f);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        radius = capsule.readFloat("radius", 0.5f);
        createShape();
    }

    protected void createShape() {
        cShape = new SphereShape(radius);
        cShape.setLocalScaling(Converter.convert(getScale()));
        cShape.setMargin(margin);
    }

}
