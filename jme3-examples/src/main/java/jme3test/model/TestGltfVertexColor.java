
package jme3test.model;

import com.jme3.app.*;
import com.jme3.math.*;
import com.jme3.renderer.Limits;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.*;
import com.jme3.scene.plugins.gltf.GltfModelKey;

public class TestGltfVertexColor extends SimpleApplication {
    Node probeNode;

    public static void main(String[] args) {
        TestGltfVertexColor app = new TestGltfVertexColor();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        rootNode.setShadowMode(RenderQueue.ShadowMode.CastAndReceive);
        probeNode = (Node) assetManager.loadModel("Scenes/defaultProbe.j3o");
        rootNode.attachChild(probeNode);

        cam.setFrustumPerspective(45f, (float) cam.getWidth() / cam.getHeight(), 0.1f, 100f);
        renderer.setDefaultAnisotropicFilter(Math.min(renderer.getLimits().get(Limits.TextureAnisotropy), 8));
        setPauseOnLostFocus(false);

        flyCam.setEnabled(false);
        viewPort.setBackgroundColor(new ColorRGBA().setAsSrgb(0.2f, 0.2f, 0.2f, 1.0f));

        loadModel("jme3test/gltfvertexcolor/VertexColorTest.glb", new Vector3f(0, -1, 0), 1);
    }

    private void loadModel(String path, Vector3f offset, float scale) {
        GltfModelKey k = new GltfModelKey(path);
        Spatial s = assetManager.loadModel(k);
        s.scale(scale);
        s.move(offset);
        probeNode.attachChild(s);
    }

}
