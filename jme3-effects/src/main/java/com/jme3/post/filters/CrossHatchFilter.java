
package com.jme3.post.filters;

import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import java.io.IOException;

/**
 * A Post Processing filter that makes the screen look like it was drawn as
 * diagonal lines with a pen.
 * Try combining this with a cartoon edge filter to obtain manga style visuals.
 *
 * Based on an article from Geeks3D:
 *    <a href="http://www.geeks3d.com/20110219/shader-library-crosshatching-glsl-filter/">http://www.geeks3d.com/20110219/shader-library-crosshatching-glsl-filter/</a>
 *
 * @author Roy Straver a.k.a. Baal Garnaal
 */
public class CrossHatchFilter extends Filter {

    private ColorRGBA lineColor = ColorRGBA.Black.clone();
    private ColorRGBA paperColor = ColorRGBA.White.clone();
    private float colorInfluenceLine = 0.8f;
    private float colorInfluencePaper = 0.1f;
    private float fillValue = 0.9f;
    private float luminance1 = 0.9f;
    private float luminance2 = 0.7f;
    private float luminance3 = 0.5f;
    private float luminance4 = 0.3f;
    private float luminance5 = 0.0f;
    private float lineThickness = 1.0f;
    private float lineDistance = 4.0f;

    /**
     * Creates a crossHatch filter
     */
    public CrossHatchFilter() {
        super("CrossHatchFilter");
    }

    /**
     * Creates a crossHatch filter
     * @param lineColor the colors of the lines
     * @param paperColor the paper color
     */
    public CrossHatchFilter(ColorRGBA lineColor, ColorRGBA paperColor) {
        this();
        this.lineColor = lineColor;
        this.paperColor = paperColor;
    }

    @Override
    protected boolean isRequiresDepthTexture() {
        return false;
    }

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        material = new Material(manager, "Common/MatDefs/Post/CrossHatch.j3md");
        material.setColor("LineColor", lineColor);
        material.setColor("PaperColor", paperColor);

        material.setFloat("ColorInfluenceLine", colorInfluenceLine);
        material.setFloat("ColorInfluencePaper", colorInfluencePaper);

        material.setFloat("FillValue", fillValue);

        material.setFloat("Luminance1", luminance1);
        material.setFloat("Luminance2", luminance2);
        material.setFloat("Luminance3", luminance3);
        material.setFloat("Luminance4", luminance4);
        material.setFloat("Luminance5", luminance5);

        material.setFloat("LineThickness", lineThickness);
        material.setFloat("LineDistance", lineDistance);
    }

    @Override
    protected Material getMaterial() {
        return material;
    }

    /**
     * Sets color used to draw lines
     *
     * @param lineColor the desired color (alias created, default=(0,0,0,1))
     */
    public void setLineColor(ColorRGBA lineColor) {
        this.lineColor = lineColor;
        if (material != null) {
            material.setColor("LineColor", lineColor);
        }
    }

    /**
     * Sets color used as background
     *
     * @param paperColor the desired color (alias created, default=(1,1,1,1))
     */
    public void setPaperColor(ColorRGBA paperColor) {
        this.paperColor = paperColor;
        if (material != null) {
            material.setColor("PaperColor", paperColor);
        }
    }

    /**
     * Sets color influence of original image on lines drawn
     *
     * @param colorInfluenceLine the desired factor (default=0.8) 
     */
    public void setColorInfluenceLine(float colorInfluenceLine) {
        this.colorInfluenceLine = colorInfluenceLine;
        if (material != null) {
            material.setFloat("ColorInfluenceLine", colorInfluenceLine);
        }
    }

    /**
     * Sets color influence of original image on non-line areas
     *
     * @param colorInfluencePaper the desired factor (default=0.1)
     */
    public void setColorInfluencePaper(float colorInfluencePaper) {
        this.colorInfluencePaper = colorInfluencePaper;
        if (material != null) {
            material.setFloat("ColorInfluencePaper", colorInfluencePaper);
        }
    }

    /**
     * Sets line/paper color ratio for areas with values less than luminance5,
     * really dark areas get no lines but a filled blob instead
     *
     * @param fillValue the desired ratio (default=0.9)
     */
    public void setFillValue(float fillValue) {
        this.fillValue = fillValue;
        if (material != null) {
            material.setFloat("FillValue", fillValue);
        }
    }

    /**
     *
     * Sets minimum luminance levels for lines drawn
     * @param luminance1 Top-left to bottom right 1
     * @param luminance2 Top-right to bottom left 1
     * @param luminance3 Top-left to bottom right 2
     * @param luminance4 Top-right to bottom left 2
     * @param luminance5 Blobs
     */
    public void setLuminanceLevels(float luminance1, float luminance2, float luminance3, float luminance4, float luminance5) {
        this.luminance1 = luminance1;
        this.luminance2 = luminance2;
        this.luminance3 = luminance3;
        this.luminance4 = luminance4;
        this.luminance5 = luminance5;

        if (material != null) {
            material.setFloat("Luminance1", luminance1);
            material.setFloat("Luminance2", luminance2);
            material.setFloat("Luminance3", luminance3);
            material.setFloat("Luminance4", luminance4);
            material.setFloat("Luminance5", luminance5);
        }
    }

    /**
     * Sets the thickness of lines drawn
     *
     * @param lineThickness the desired thickness (in pixels, default=1)
     */
    public void setLineThickness(float lineThickness) {
        this.lineThickness = lineThickness;
        if (material != null) {
            material.setFloat("LineThickness", lineThickness);
        }
    }

    /**
     * Sets minimum distance between lines drawn
     * Primary lines are drawn at 2*lineDistance
     * Secondary lines are drawn at lineDistance
     *
     * @param lineDistance the desired distance (in pixels, default=4)
     */
    public void setLineDistance(float lineDistance) {
        this.lineDistance = lineDistance;
        if (material != null) {
            material.setFloat("LineDistance", lineDistance);
        }
    }

    /**
     * Returns line color
     * @return the pre-existing instance
     */
    public ColorRGBA getLineColor() {
        return lineColor;
    }

    /**
     * Returns paper background color
     * @return the pre-existing instance
     */
    public ColorRGBA getPaperColor() {
        return paperColor;
    }

    /**
     * Returns current influence of image colors on lines
     *
     * @return the influence factor
     */
    public float getColorInfluenceLine() {
        return colorInfluenceLine;
    }

    /**
     * Returns current influence of image colors on paper background
     *
     * @return the influence factor
     */
    public float getColorInfluencePaper() {
        return colorInfluencePaper;
    }

    /**
     * Returns line/paper color ratio for blobs
     *
     * @return the ratio
     */
    public float getFillValue() {
        return fillValue;
    }

    /**
     * Returns the thickness of the lines drawn
     *
     * @return the thickness (in pixels)
     */
    public float getLineThickness() {
        return lineThickness;
    }

    /**
     * Returns minimum distance between lines
     *
     * @return the distance (in pixels)
     */
    public float getLineDistance() {
        return lineDistance;
    }

    /**
     * Returns threshold for lines 1
     *
     * @return the first luminance threshold
     */
    public float getLuminance1() {
        return luminance1;
    }

    /**
     * Returns threshold for lines 2
     *
     * @return the 2nd luminance threshold
     */
    public float getLuminance2() {
        return luminance2;
    }

    /**
     * Returns threshold for lines 3
     *
     * @return the 3rd luminance threshold
     */
    public float getLuminance3() {
        return luminance3;
    }

    /**
     * Returns threshold for lines 4
     *
     * @return the 4th luminance threshold
     */
    public float getLuminance4() {
        return luminance4;
    }

    /**
     * Returns threshold for blobs
     *
     * @return the 5th luminance threshold
     */
    public float getLuminance5() {
        return luminance5;
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

        this.colorInfluenceLine = capsule.readFloat("colorInfluenceLine", 0.8f);
        this.colorInfluencePaper = capsule.readFloat("colorInfluencePaper", 0.1f);
        this.fillValue = capsule.readFloat("fillValue", 0.9f);
        this.lineColor = (ColorRGBA) capsule.readSavable(
                "lineColor", new ColorRGBA(0f, 0f, 0f, 0f));
        this.lineDistance = capsule.readFloat("lineDistance", 4f);
        this.lineThickness = capsule.readFloat("lineThickness", 1f);
        this.luminance1 = capsule.readFloat("luminance1", 0.9f);
        this.luminance2 = capsule.readFloat("luminance2", 0.7f);
        this.luminance3 = capsule.readFloat("luminance3", 0.5f);
        this.luminance4 = capsule.readFloat("luminance4", 0.3f);
        this.luminance5 = capsule.readFloat("luminance5", 0f);
        this.paperColor = (ColorRGBA) capsule.readSavable(
                "paperColor", new ColorRGBA(1f, 1f, 1f, 1f));
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

        capsule.write(colorInfluenceLine, "colorInfluenceLine", 0.8f);
        capsule.write(colorInfluencePaper, "colorInfluencePaper", 0.1f);
        capsule.write(fillValue, "fillValue", 0.9f);
        capsule.write(lineColor, "lineColor", new ColorRGBA(0f, 0f, 0f, 0f));
        capsule.write(lineDistance, "lineDistance", 4f);
        capsule.write(lineThickness, "lineThickness", 1f);
        capsule.write(luminance1, "luminance1", 0.9f);
        capsule.write(luminance2, "luminance2", 0.7f);
        capsule.write(luminance3, "luminance3", 0.5f);
        capsule.write(luminance4, "luminance4", 0.3f);
        capsule.write(luminance5, "luminance5", 0f);
        capsule.write(paperColor, "paperColor", new ColorRGBA(1f, 1f, 1f, 1f));
    }
}
