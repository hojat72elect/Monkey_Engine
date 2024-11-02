

package jme3test.niftygui;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture.MagFilter;
import com.jme3.texture.Texture.MinFilter;
import com.jme3.texture.Texture2D;
import com.jme3.texture.FrameBuffer.FrameBufferTarget;
import com.jme3.texture.image.ColorSpace;
import de.lessvoid.nifty.Nifty;

public class TestNiftyToMesh extends SimpleApplication{

    public static void main(String[] args){
        TestNiftyToMesh app = new TestNiftyToMesh();
        app.start();
    }

    @Override
    public void simpleInitApp() {
       ViewPort niftyView = renderManager.createPreView("NiftyView", new Camera(1024, 768));
       niftyView.setClearFlags(true, true, true);

        ColorSpace colorSpace = renderer.isMainFrameBufferSrgb()
                ? ColorSpace.sRGB : ColorSpace.Linear;
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager,
                                                          inputManager,
                                                          audioRenderer,
                                                          niftyView,
                                                          colorSpace);
        Nifty nifty = niftyDisplay.getNifty();
        nifty.fromXml("all/intro.xml", "start");
        niftyView.addProcessor(niftyDisplay);

        Texture2D depthTex = new Texture2D(1024, 768, Format.Depth);
        FrameBuffer fb = new FrameBuffer(1024, 768, 1);
        fb.setDepthTarget(FrameBufferTarget.newTarget(depthTex));

        Texture2D tex = new Texture2D(1024, 768, Format.RGBA8);
        tex.setMinFilter(MinFilter.Trilinear);
        tex.setMagFilter(MagFilter.Bilinear);

        fb.addColorTarget(FrameBufferTarget.newTarget(tex));
        niftyView.setClearFlags(true, true, true);
        niftyView.setOutputFrameBuffer(fb);

        Box b = new Box(1, 1, 1);
        Geometry geom = new Geometry("Box", b);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setTexture("ColorMap", tex);
        geom.setMaterial(mat);
        rootNode.attachChild(geom);
    }
}
