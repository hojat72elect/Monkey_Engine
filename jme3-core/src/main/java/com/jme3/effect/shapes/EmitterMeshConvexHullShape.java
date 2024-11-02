
package com.jme3.effect.shapes;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import java.util.List;

/**
 * This emitter shape emits the particles from the given shape's interior constrained by its convex hull
 * (a geometry that tightly wraps the mesh). So in case of multiple meshes some vertices may appear
 * in a space between them.
 * @author Marcin Roguski (Kaelthas)
 */
public class EmitterMeshConvexHullShape extends EmitterMeshFaceShape {

    /**
     * Empty constructor. Sets nothing.
     */
    public EmitterMeshConvexHullShape() {
    }

    /**
     * Constructor. It stores a copy of vertex list of all meshes.
     * @param meshes
     *        a list of meshes that will form the emitter's shape
     */
    public EmitterMeshConvexHullShape(List<Mesh> meshes) {
        super(meshes);
    }

    /**
     * Randomly selects a point inside the convex hull
     * of a randomly selected mesh.
     *
     * @param store
     *        storage for the coordinates of the selected point
     */
    @Override
    public void getRandomPoint(Vector3f store) {
        super.getRandomPoint(store);
        // now move the point from the mesh's face toward the center of the mesh
        // the center is in (0, 0, 0) in the local coordinates
        store.multLocal(FastMath.nextRandomFloat());
    }

    /**
     * Randomly selects a point inside the convex hull
     * of a randomly selected mesh.
     * The {@code normal} argument is not used.
     *
     * @param store
     *        storage for the coordinates of the selected point
     * @param normal
     *        not used in this class
     */
    @Override
    public void getRandomPointAndNormal(Vector3f store, Vector3f normal) {
        super.getRandomPointAndNormal(store, normal);
        // now move the point from the mesh's face toward the center of the mesh
        // the center is in (0, 0, 0) in the local coordinates
        store.multLocal(FastMath.nextRandomFloat());
    }
}
