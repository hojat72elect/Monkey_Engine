
package com.jme3.bullet.collision.shapes;

import com.bulletphysics.collision.shapes.IndexedMesh;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.extras.gimpact.GImpactMeshShape;
import com.jme3.bullet.util.Converter;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Basic mesh collision shape
 * @author normenhansen
 */
public class GImpactCollisionShape extends CollisionShape{

    protected Vector3f worldScale;
    protected int numVertices, numTriangles, vertexStride, triangleIndexStride;
    protected ByteBuffer triangleIndexBase, vertexBase;
    protected IndexedMesh bulletMesh;

    protected GImpactCollisionShape() {
    }

    /**
     * creates a collision shape from the given Mesh
     * @param mesh the Mesh to use
     */
    public GImpactCollisionShape(Mesh mesh) {
        createCollisionMesh(mesh, new Vector3f(1,1,1));
    }


    private void createCollisionMesh(Mesh mesh, Vector3f worldScale) {
        this.worldScale = worldScale;
        bulletMesh = Converter.convert(mesh);
        this.numVertices = bulletMesh.numVertices;
        this.numTriangles = bulletMesh.numTriangles;
        this.vertexStride = bulletMesh.vertexStride;
        this.triangleIndexStride = bulletMesh.triangleIndexStride;
        this.triangleIndexBase = bulletMesh.triangleIndexBase;
        this.vertexBase = bulletMesh.vertexBase;
        createShape();
    }

    /**
     * creates a jme mesh from the collision shape, only needed for debugging
     *
     * @return a new Mesh
     */
    public Mesh createJmeMesh(){
        return Converter.convert(bulletMesh);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule capsule = ex.getCapsule(this);
        capsule.write(worldScale, "worldScale", new Vector3f(1, 1, 1));
        capsule.write(numVertices, "numVertices", 0);
        capsule.write(numTriangles, "numTriangles", 0);
        capsule.write(vertexStride, "vertexStride", 0);
        capsule.write(triangleIndexStride, "triangleIndexStride", 0);

        capsule.write(triangleIndexBase.array(), "triangleIndexBase", new byte[0]);
        capsule.write(vertexBase.array(), "vertexBase", new byte[0]);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule capsule = im.getCapsule(this);
        worldScale = (Vector3f) capsule.readSavable("worldScale", new Vector3f(1, 1, 1));
        numVertices = capsule.readInt("numVertices", 0);
        numTriangles = capsule.readInt("numTriangles", 0);
        vertexStride = capsule.readInt("vertexStride", 0);
        triangleIndexStride = capsule.readInt("triangleIndexStride", 0);

        triangleIndexBase = ByteBuffer.wrap(capsule.readByteArray("triangleIndexBase", new byte[0]));
        vertexBase = ByteBuffer.wrap(capsule.readByteArray("vertexBase", new byte[0]));
        createShape();
    }

    protected void createShape() {
        bulletMesh = new IndexedMesh();
        bulletMesh.numVertices = numVertices;
        bulletMesh.numTriangles = numTriangles;
        bulletMesh.vertexStride = vertexStride;
        bulletMesh.triangleIndexStride = triangleIndexStride;
        bulletMesh.triangleIndexBase = triangleIndexBase;
        bulletMesh.vertexBase = vertexBase;
        bulletMesh.triangleIndexBase = triangleIndexBase;
        TriangleIndexVertexArray tiv = new TriangleIndexVertexArray(numTriangles, triangleIndexBase, triangleIndexStride, numVertices, vertexBase, vertexStride);
        cShape = new GImpactMeshShape(tiv);
        cShape.setLocalScaling(Converter.convert(worldScale));
        cShape.setLocalScaling(Converter.convert(getScale()));
        cShape.setMargin(margin);
        ((GImpactMeshShape) cShape).updateBound();
    }

    @Override
    public void setScale(Vector3f scale) {
        super.setScale(scale);
        ((GImpactMeshShape) cShape).updateBound();
    }
}
