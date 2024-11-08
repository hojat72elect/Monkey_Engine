
package com.jme3.scene.debug;

import com.jme3.animation.Skeleton;
import com.jme3.export.JmeImporter;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.util.clone.Cloner;

import java.io.IOException;
import java.util.Map;

/**
 * The class that creates a mesh to display how bones behave.
 * If it is supplied with the bones' lengths it will show exactly how the bones look like on the scene.
 * If not then only connections between each bone heads will be shown.
 */
public class SkeletonDebugger extends Node {
    /** The lines of the bones or the wires between their heads. */
    private SkeletonWire          wires;
    /** The heads and tails points of the bones or only heads if no length data is available. */
    private SkeletonPoints        points;
    /** The dotted lines between a bone's tail and the had of its children. Not available if the length data was not provided. */
    private SkeletonInterBoneWire interBoneWires;

    public SkeletonDebugger() {
    }

    /**
     * Creates a debugger with no length data. The wires will be a connection between the bones' heads only.
     * The points will show the bones' heads only and no dotted line of inter bones connection will be visible.
     * @param name
     *            the name of the debugger's node
     * @param skeleton
     *            the skeleton that will be shown
     */
    public SkeletonDebugger(String name, Skeleton skeleton) {
        this(name, skeleton, null);
    }

    /**
     * Creates a debugger with bone lengths data. If the data is supplied then the wires will show each full bone (from head to tail),
     * the points will display both heads and tails of the bones and dotted lines between bones will be seen.
     * @param name
     *            the name of the debugger's node
     * @param skeleton
     *            the skeleton that will be shown
     * @param boneLengths
     *            a map between the bone's index and the bone's length
     */
    public SkeletonDebugger(String name, Skeleton skeleton, Map<Integer, Float> boneLengths) {
        super(name);

        wires = new SkeletonWire(skeleton, boneLengths);
        points = new SkeletonPoints(skeleton, boneLengths);

        this.attachChild(new Geometry(getGeometryName("_wires"), wires));
        this.attachChild(new Geometry(getGeometryName("_points"), points));
        if (boneLengths != null) {
            interBoneWires = new SkeletonInterBoneWire(skeleton, boneLengths);
            this.attachChild(new Geometry(getGeometryName("_interwires"), interBoneWires));
        }

        this.setQueueBucket(Bucket.Transparent);
    }

    private String getGeometryName(String suffix) {
        return name + suffix;
    }

    @Override
    public void updateLogicalState(float tpf) {
        super.updateLogicalState(tpf);
        wires.updateGeometry();
        points.updateGeometry();
        if(interBoneWires != null) {
            interBoneWires.updateGeometry();
        }
    }

    /**
     * @return the skeleton points
     */
    public SkeletonPoints getPoints() {
        return points;
    }

    /**
     * @return the skeleton wires
     */
    public SkeletonWire getWires() {
        return wires;
    }

    /**
     * @return the dotted line between bones (can be null)
     */
    public SkeletonInterBoneWire getInterBoneWires() {
        return interBoneWires;
    }

    @Override
    public void cloneFields(Cloner cloner, Object original) {
        super.cloneFields(cloner, original);

        this.wires = cloner.clone(wires);
        this.points = cloner.clone(points);
        this.interBoneWires = cloner.clone(interBoneWires);
    }

    @Override
    public void read(JmeImporter importer) throws IOException {
        super.read(importer);

        // Find our stuff
        wires = getMesh("_wires");
        points = getMesh("_points");
        interBoneWires = getMesh("_interwires");
    }

    @SuppressWarnings("unchecked")
    private <T extends Mesh> T getMesh(String suffix) {
        Geometry child = (Geometry)getChild(getGeometryName(suffix));
        if(child != null) {
            return (T) child.getMesh();
        }

        return null;
    }
}