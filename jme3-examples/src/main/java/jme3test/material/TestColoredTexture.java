

package jme3test.material;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;

public class TestColoredTexture extends SimpleApplication {

    private float time = 0;
    private ColorRGBA nextColor;
    private ColorRGBA prevColor;
    private Material mat;

    public static void main(String[] args){
        TestColoredTexture app = new TestColoredTexture();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        Quad quadMesh = new Quad(512,512);
        Geometry quad = new Geometry("Quad", quadMesh);
        quad.setQueueBucket(Bucket.Gui);

        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", assetManager.loadTexture("Textures/ColoredTex/Monkey.png"));
        quad.setMaterial(mat);
        guiNode.attachChildAt(quad, 0);

        nextColor = ColorRGBA.randomColor();
        prevColor = ColorRGBA.Black;
    }

    @Override
    public void simpleUpdate(float tpf){
        time += tpf;
        if (time > 1f){
            time -= 1f;
            prevColor = nextColor;
            nextColor = ColorRGBA.randomColor();
        }
        ColorRGBA currentColor = new ColorRGBA();
        currentColor.interpolateLocal(prevColor, nextColor, time);

        mat.setColor("Color", currentColor);
    }

}
