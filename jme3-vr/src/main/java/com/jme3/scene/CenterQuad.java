
package com.jme3.scene;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.scene.VertexBuffer.Type;
import java.io.IOException;

/**
 * A static, indexed, Triangles-mode mesh for an axis-aligned rectangle in the
 * X-Y plane.
 *
 * <p>The rectangle extends from (-width/2, -height/2, 0) to
 * (width/2, height/2, 0) with normals set to (0,0,1).
 *
 * <p>This differs from com.jme3.scene.shape.Quad because it puts
 * (0,0,0) at the rectangle's center instead of in a corner.
 *
 * @author Kirill Vainer
 * @deprecated use com.jme3.scene.shape.CenterQuad
 */
@Deprecated
public class CenterQuad extends Mesh {

    public static CenterQuad UnitQuad = new CenterQuad(0.5f, 0.5f);
    public static Mesh CenterSplitQuad;
    
    private float width;
    private float height;

    /**
     * Create a quad with the given width and height. The quad
     * is always created in the XY plane.
     * 
     * @param width The X extent or width
     * @param height The Y extent or width
     */
    public CenterQuad(float width, float height){
        updateGeometry(width, height);
    }

    /**
     * Create a quad with the given width and height. The quad
     * is always created in the XY plane.
     * 
     * @param width The X extent or width
     * @param height The Y extent or width
     * @param flipCoords If true, the texture coordinates will be flipped
     * along the Y axis.
     */
    public CenterQuad(float width, float height, boolean flipCoords){
        updateGeometry(width, height, flipCoords);
        this.setStatic();
    }

    /**
     * For serialization only. Do not use.
     */
    protected CenterQuad() {
    }

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public void updateGeometry(float width, float height){
        updateGeometry(width, height, false);
    }

    public void updateGeometry(float width, float height, boolean flipCoords) {
        this.width = width;
        this.height = height;
        setBuffer(Type.Position, 3, new float[]{-width/2,        -height/2,      0,
                                                width/2,  -height/2,      0,
                                                width/2,  height/2, 0,
                                                -width/2,        height/2, 0
                                                });
        

        if (flipCoords){
            setBuffer(Type.TexCoord, 2, new float[]{0, 1,
                                                    1, 1,
                                                    1, 0,
                                                    0, 0});
        }else{
            setBuffer(Type.TexCoord, 2, new float[]{0, 0,
                                                    1, 0,
                                                    1, 1,
                                                    0, 1});
        }
        setBuffer(Type.Normal, 3, new float[]{0, 0, 1,
                                              0, 0, 1,
                                              0, 0, 1,
                                              0, 0, 1});
        if (height < 0){
            setBuffer(Type.Index, 3, new short[]{0, 2, 1,
                                                 0, 3, 2});
        }else{
            setBuffer(Type.Index, 3, new short[]{0, 1, 2,
                                                 0, 2, 3});
        }
        
        updateBound();
    }

    /**
     * De-serializes from the specified importer, for example when loading from
     * a J3O file.
     *
     * @param importer the importer to use (not null)
     * @throws IOException from the importer
     */
    @Override
    public void read(JmeImporter importer) throws IOException {
        super.read(importer);
        InputCapsule capsule = importer.getCapsule(this);

        width = capsule.readFloat("width", 0f);
        height = capsule.readFloat("height", 0f);
    }

    /**
     * Serializes to the specified exporter, for example when saving to a J3O
     * file. The current instance is unaffected.
     *
     * @param exporter the exporter to use (not null)
     * @throws IOException from the exporter
     */
    @Override
    public void write(JmeExporter exporter) throws IOException {
        super.write(exporter);
        OutputCapsule capsule = exporter.getCapsule(this);

        capsule.write(width, "width", 0f);
        capsule.write(height, "height", 0f);
    }
}
