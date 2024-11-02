
package com.jme3.post.filters;

import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import java.io.IOException;

/**
 * <a href="http://www.geeks3d.com/20110405/fxaa-fast-approximate-anti-aliasing-demo-glsl-opengl-test-radeon-geforce/3/">demo</a>
 * <a href="http://developer.download.nvidia.com/assets/gamedev/files/sdk/11/FXAA_WhitePaper.pdf">whitepaper</a>
 *
 * @author Phate666 (adapted to jme3)
 *
 */
public class FXAAFilter extends Filter {

    private float subPixelShift = 1.0f / 4.0f;
    private float vxOffset = 0.0f;
    private float spanMax = 8.0f;
    private float reduceMul = 1.0f / 8.0f;

    public FXAAFilter() {
        super("FXAAFilter");
    }

    @Override
    protected void initFilter(AssetManager manager,
            RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "Common/MatDefs/Post/FXAA.j3md");   
        material.setFloat("SubPixelShift", subPixelShift);
        material.setFloat("VxOffset", vxOffset);
        material.setFloat("SpanMax", spanMax);
        material.setFloat("ReduceMul", reduceMul);
    }

    @Override
    protected Material getMaterial() {
        return material;
    }
    
    @Override
    protected boolean isRequiresBilinear() {
        // FXAA wants the input texture to be filtered.
        return true;
    }

    public void setSpanMax(float spanMax) {
        this.spanMax = spanMax;
        if (material != null) {
            material.setFloat("SpanMax", this.spanMax);
        }
    }

    /**
     * set to 0.0f for higher quality
     *
     * @param subPixelShift the desired shift (default=0.25)
     */
    public void setSubPixelShift(float subPixelShift) {
        this.subPixelShift = subPixelShift;
        if (material != null) {
            material.setFloat("SubPixelShift", this.subPixelShift);
        }
    }

    /**
     * set to 0.0f for higher quality
     *
     * @param reduceMul the desired value (default=0.125)
     */
    public void setReduceMul(float reduceMul) {
        this.reduceMul = reduceMul;
        if (material != null) {
            material.setFloat("ReduceMul", this.reduceMul);
        }
    }

    public void setVxOffset(float vxOffset) {
        this.vxOffset = vxOffset;
        if (material != null) {
            material.setFloat("VxOffset", this.vxOffset);
        }
    }

    public float getReduceMul() {
        return reduceMul;
    }

    public float getSpanMax() {
        return spanMax;
    }

    public float getSubPixelShift() {
        return subPixelShift;
    }

    public float getVxOffset() {
        return vxOffset;
    }

    /**
     * Load properties when the filter is de-serialized, for example when
     * loading from a J3O file.
     *
     * @param importer the importer to use (not null)
     * @throws IOException from the importer
     */
    @Override
    public void read(JmeImporter importer) throws IOException {
        super.read(importer);
        InputCapsule capsule = importer.getCapsule(this);

        this.reduceMul = capsule.readFloat("reduceMul", 0.125f);
        this.spanMax = capsule.readFloat("spanMax", 8f);
        this.subPixelShift = capsule.readFloat("subPixelShift", 0.25f);
        this.vxOffset = capsule.readFloat("vxOffset", 0f);
    }

    /**
     * Save properties when the filter is serialized, for example when saving to
     * a J3O file.
     *
     * @param exporter the exporter to use (not null)
     * @throws IOException from the exporter
     */
    @Override
    public void write(JmeExporter exporter) throws IOException {
        super.write(exporter);
        OutputCapsule capsule = exporter.getCapsule(this);

        capsule.write(reduceMul, "reduceMul", 0.125f);
        capsule.write(spanMax, "spanMax", 8f);
        capsule.write(subPixelShift, "subPixelShift", 0.25f);
        capsule.write(vxOffset, "vxOffset", 0f);
    }
}
