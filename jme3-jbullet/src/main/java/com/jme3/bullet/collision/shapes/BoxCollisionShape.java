
package com.jme3.bullet.collision.shapes;

import com.bulletphysics.collision.shapes.BoxShape;
import com.jme3.bullet.util.Converter;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector3f;
import java.io.IOException;

/**
 * Basic box collision shape
 * @author normenhansen
 */
public class BoxCollisionShape extends CollisionShape {

    private Vector3f halfExtents;

    protected BoxCollisionShape() {
    }

    /**
     * creates a collision box from the given halfExtents
     * @param halfExtents the halfExtents of the CollisionBox
     */
    public BoxCollisionShape(Vector3f halfExtents) {
        this.halfExtents = halfExtents;
        createShape();
    }

    public final Vector3f getHalfExtents() {
        return halfExtents;
    }
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(halfExtents, "halfExtents", new Vector3f(1, 1, 1));
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        Vector3f halfExtents = (Vector3f) capsule.readSavable("halfExtents", new Vector3f(1, 1, 1));
        this.halfExtents = halfExtents;
        createShape();
    }

    protected void createShape() {
        cShape = new BoxShape(Converter.convert(halfExtents));
        cShape.setLocalScaling(Converter.convert(getScale()));
        cShape.setMargin(margin);
    }

}
