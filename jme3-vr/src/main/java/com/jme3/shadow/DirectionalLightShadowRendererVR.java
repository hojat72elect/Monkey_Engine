package com.jme3.shadow;



import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.GeometryList;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import java.io.IOException;

/**
 * DirectionalLightShadowRenderer renderer use Parallel Split Shadow Mapping
 * technique (pssm)<br> It splits the view frustum in several parts and compute
 * a shadow map for each one.<br> splits are distributed so that the closer they
 * are from the camera, the smaller they are to maximize the resolution used of
 * the shadow map.<br> This results in a better quality shadow than standard
 * shadow mapping.<br> for more information on this read <a
 * href="http://http.developer.nvidia.com/GPUGems3/gpugems3_ch10.html">http://http.developer.nvidia.com/GPUGems3/gpugems3_ch10.html</a><br>
 *
 * @author Rémy Bouquet aka Nehon
 * @author reden - phr00t - https://github.com/phr00t
 * @author Julien Seinturier - COMEX SA - <a href="http://www.seinturier.fr">http://www.seinturier.fr</a>
 * @deprecated The jme3-vr module is deprecated and will be removed in a future version (as it only supports OpenVR).
 *             For new Virtual Reality projects, use user libraries that provide OpenXR support.
 *             See <a href = "https://wiki.jmonkeyengine.org/docs/3.4/core/vr/virtualreality.html">Virtual Reality JME wiki section</a>
 *             for more information.
 */
@Deprecated
public class DirectionalLightShadowRendererVR extends AbstractShadowRendererVR {

    protected float lambda = 0.65f;    
    protected Camera shadowCam;
    protected ColorRGBA splits;
    protected float[] splitsArray;
    protected DirectionalLight light;
    protected Vector3f[] points = new Vector3f[8];
    //Holding the info for fading shadows in the far distance   
    private boolean stabilize = true;
    
    /**
     * Used for serialization use
     * DirectionalLightShadowRenderer#DirectionalLightShadowRenderer(AssetManager
     * assetManager, int shadowMapSize, int nbSplits)
     */
    public DirectionalLightShadowRendererVR() {
        super();
    }

    /**
     * Creates a DirectionalLight shadow renderer. More info on the technique at <a
     * href="http://http.developer.nvidia.com/GPUGems3/gpugems3_ch10.html">http://http.developer.nvidia.com/GPUGems3/gpugems3_ch10.html</a>
     *
     * @param assetManager the application's asset manager
     * @param shadowMapSize the size of the rendered shadowmaps (512, 1024, 2048,
     *     etcetera)
     * @param nbSplits the number of shadow maps rendered (More shadow maps yield
     *     better quality, fewer fps.)
     */
    public DirectionalLightShadowRendererVR(AssetManager assetManager, int shadowMapSize, int nbSplits) {
        super(assetManager, shadowMapSize, nbSplits);
        init(nbSplits, shadowMapSize);
    }

    private void init(int nbSplits, int shadowMapSize) {
        nbShadowMaps = Math.max(Math.min(nbSplits, 4), 1);
        if (nbShadowMaps != nbSplits) {
            throw new IllegalArgumentException("Number of splits must be between 1 and 4. Given value : " + nbSplits);
        }
        splits = new ColorRGBA();
        splitsArray = new float[nbSplits + 1];
        shadowCam = new Camera(shadowMapSize, shadowMapSize);
        shadowCam.setParallelProjection(true);
        for (int i = 0; i < points.length; i++) {
            points[i] = new Vector3f();
        }
    }

    @Override
    protected void initFrustumCam() {
        //nothing to do
    }

    /**
     * return the light used to cast shadows
     * @return the DirectionalLight
     */
    public DirectionalLight getLight() {
        return light;
    }

    /**
     * Sets the light to use to cast shadows
     * @param light a DirectionalLight
     */
    public void setLight(DirectionalLight light) {
        this.light = light;
    }

    @Override
    protected void updateShadowCams(Camera viewCam) {

        float zFar = zFarOverride;
        if (zFar == 0) {
            zFar = viewCam.getFrustumFar();
        }

        //We prevent computing the frustum points and splits with zeroed or negative near clip value
        float frustumNear = Math.max(viewCam.getFrustumNear(), 0.001f);
        ShadowUtil.updateFrustumPoints(viewCam, frustumNear, zFar, 1.0f, points);

        //shadowCam.setDirection(direction);
        shadowCam.getRotation().lookAt(light.getDirection(), shadowCam.getUp());
        shadowCam.update();
        shadowCam.updateViewProjection();

        PssmShadowUtil.updateFrustumSplits(splitsArray, frustumNear, zFar, lambda);

        // in parallel projection shadow position goe from 0 to 1
        if(viewCam.isParallelProjection()){
            for (int i = 0; i < nbShadowMaps; i++) {
                splitsArray[i] = splitsArray[i]/(zFar- frustumNear);
            }
        }

        switch (splitsArray.length) {
            case 5:
                splits.a = splitsArray[4];
            case 4:
                splits.b = splitsArray[3];
            case 3:
                splits.g = splitsArray[2];
            case 2:
            case 1:
                splits.r = splitsArray[1];
                break;
        }

    }
    
    @Override
    protected GeometryList getOccludersToRender(int shadowMapIndex, GeometryList shadowMapOccluders) {

        // update frustum points based on current camera and split
        ShadowUtil.updateFrustumPoints(viewPort.getCamera(), splitsArray[shadowMapIndex], splitsArray[shadowMapIndex + 1], 1.0f, points);

        //Updating shadow cam with current split frusta
        if (lightReceivers.size()==0) {
            for (Spatial scene : viewPort.getScenes()) {
              ShadowUtil.getGeometriesInCamFrustum(scene, viewPort.getCamera(), RenderQueue.ShadowMode.Receive, lightReceivers);
            }
        }
        ShadowUtil.updateShadowCamera(viewPort, lightReceivers, shadowCam, points, shadowMapOccluders, stabilize?shadowMapSize:0);

        return shadowMapOccluders;
    }

    @Override
    protected void getReceivers(GeometryList lightReceivers) {
        if (lightReceivers.size()==0) {
            for (Spatial scene : viewPort.getScenes()) {
                ShadowUtil.getGeometriesInCamFrustum(scene, viewPort.getCamera(), RenderQueue.ShadowMode.Receive, lightReceivers);
            }
        }
    }

    @Override
    protected Camera getShadowCam(int shadowMapIndex) {
        return shadowCam;
    }

    @Override
    protected void doDisplayFrustumDebug(int shadowMapIndex) {
        ((Node) viewPort.getScenes().get(0)).attachChild(createFrustum(points, shadowMapIndex));
        ShadowUtil.updateFrustumPoints2(shadowCam, points);
        ((Node) viewPort.getScenes().get(0)).attachChild(createFrustum(points, shadowMapIndex));
    }

    @Override
    protected void setMaterialParameters(Material material) {
        material.setColor("Splits", splits);
        material.setVector3("LightDir", light.getDirection());
        if (fadeInfo != null) {
            material.setVector2("FadeInfo", fadeInfo);
        }
    }

    @Override
    protected void clearMaterialParameters(Material material) {
        material.clearParam("Splits");
        material.clearParam("FadeInfo");
        material.clearParam("LightDir");
    }

    /**
     * returns the lambda parameter see #setLambda(float lambda)
     *
     * @return lambda
     */
    public float getLambda() {
        return lambda;
    }

    /**
     * Adjusts the partition of the shadow extend into shadow maps.
     * Lambda is usually between 0 and 1.
     * A low value gives a more linear partition,
     * resulting in consistent shadow quality over the extend,
     * but near shadows could look very jagged.
     * A high value gives a more logarithmic partition,
     * resulting in high quality for near shadows,
     * but quality decreases rapidly with distance.
     * The default value is 0.65 (the theoretical optimum).
     *
     * @param lambda the lambda value.
     */
    public void setLambda(float lambda) {
        this.lambda = lambda;
    }

    /**
     * Check if the stabilization is enabled.
     * @return <code>true</code> if stabilization is enabled and <code>false</code> otherwise.
     */
    public boolean isEnabledStabilization() {
        return stabilize;
    }
    
    /**
     * Enables the stabilization of the shadow's edges. (default is true)
     * This prevents shadow edges from flickering when the camera moves.
     * However, it can lead to some loss of shadow quality in particular scenes.
     *
     *  @param stabilize  <code>true</code> if stabilization has to be enabled and <code>false</code> otherwise.
     */
    public void setEnabledStabilization(boolean stabilize) {
        this.stabilize = stabilize;
    }
    
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        lambda = ic.readFloat("lambda", 0.65f);
        zFarOverride = ic.readInt("zFarOverride", 0);
        light = (DirectionalLight) ic.readSavable("light", null);
        fadeInfo = (Vector2f) ic.readSavable("fadeInfo", null);
        fadeLength = ic.readFloat("fadeLength", 0f);
        init(nbShadowMaps, (int) shadowMapSize);
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(lambda, "lambda", 0.65f);
        oc.write(zFarOverride, "zFarOverride", 0);
        oc.write(light, "light", null);
        oc.write(fadeInfo, "fadeInfo", null);
        oc.write(fadeLength, "fadeLength", 0f);
    }

    /**
     * Directional light is always in the view frustum
     * @param viewCam
     * @return true
     */
    @Override
    protected boolean checkCulling(Camera viewCam) {
        return true;
    }
}
