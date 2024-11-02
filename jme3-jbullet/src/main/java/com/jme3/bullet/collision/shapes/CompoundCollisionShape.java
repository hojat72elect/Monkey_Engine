
package com.jme3.bullet.collision.shapes;

import com.bulletphysics.collision.shapes.CompoundShape;
import com.bulletphysics.linearmath.Transform;
import com.jme3.bullet.collision.shapes.infos.ChildCollisionShape;
import com.jme3.bullet.util.Converter;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A CompoundCollisionShape allows combining multiple base shapes
 * to generate a more sophisticated shape.
 * @author normenhansen
 */
public class CompoundCollisionShape extends CollisionShape {

    protected ArrayList<ChildCollisionShape> children = new ArrayList<>();

    public CompoundCollisionShape() {
        cShape = new CompoundShape();
    }

    /**
     * adds a child shape at the given local translation
     * @param shape the child shape to add
     * @param location the local location of the child shape
     */
    public void addChildShape(CollisionShape shape, Vector3f location) {
        Transform transA = new Transform(Converter.convert(new Matrix3f()));
        Converter.convert(location, transA.origin);
        children.add(new ChildCollisionShape(location.clone(), new Matrix3f(), shape));
        ((CompoundShape) cShape).addChildShape(transA, shape.getCShape());
    }

    /**
     * adds a child shape at the given local translation
     * @param shape the child shape to add
     * @param location the local location of the child shape
     * @param rotation the local orientation of the child shape (not null,
     * unaffected)
     */
    public void addChildShape(CollisionShape shape, Vector3f location, Matrix3f rotation) {
        if(shape instanceof CompoundCollisionShape){
            throw new IllegalStateException("CompoundCollisionShapes cannot have CompoundCollisionShapes as children!");
        }
        Transform transA = new Transform(Converter.convert(rotation));
        Converter.convert(location, transA.origin);
        Converter.convert(rotation, transA.basis);
        children.add(new ChildCollisionShape(location.clone(), rotation.clone(), shape));
        ((CompoundShape) cShape).addChildShape(transA, shape.getCShape());
    }

    private void addChildShapeDirect(CollisionShape shape, Vector3f location, Matrix3f rotation) {
        if(shape instanceof CompoundCollisionShape){
            throw new IllegalStateException("CompoundCollisionShapes cannot have CompoundCollisionShapes as children!");
        }
        Transform transA = new Transform(Converter.convert(rotation));
        Converter.convert(location, transA.origin);
        Converter.convert(rotation, transA.basis);
        ((CompoundShape) cShape).addChildShape(transA, shape.getCShape());
    }

    /**
     * removes a child shape
     * @param shape the child shape to remove
     */
    public void removeChildShape(CollisionShape shape) {
        ((CompoundShape) cShape).removeChildShape(shape.getCShape());
        for (Iterator<ChildCollisionShape> it = children.iterator(); it.hasNext();) {
            ChildCollisionShape childCollisionShape = it.next();
            if (childCollisionShape.shape == shape) {
                it.remove();
            }
        }
    }

    public List<ChildCollisionShape> getChildren() {
        return children;
    }

    /**
     * WARNING - CompoundCollisionShape scaling has no effect.
     */
    @Override
    public void setScale(Vector3f scale) {
        Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "CompoundCollisionShape cannot be scaled");
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.writeSavableArrayList(children, "children", new ArrayList<ChildCollisionShape>());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        children = capsule.readSavableArrayList("children", new ArrayList<ChildCollisionShape>());
        cShape.setLocalScaling(Converter.convert(getScale()));
        cShape.setMargin(margin);
        loadChildren();
    }

    private void loadChildren() {
        for (Iterator<ChildCollisionShape> it = children.iterator(); it.hasNext();) {
            ChildCollisionShape child = it.next();
            addChildShapeDirect(child.shape, child.location, child.rotation);
        }
    }

}
