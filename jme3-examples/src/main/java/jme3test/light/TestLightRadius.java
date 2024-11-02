

package jme3test.light;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Torus;

public class TestLightRadius extends SimpleApplication {

    private float pos, vel=1;
    private PointLight pl;
    private Geometry lightMdl;

    public static void main(String[] args){
        TestLightRadius app = new TestLightRadius();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        Torus torus = new Torus(10, 6, 1, 3);
//        Torus torus = new Torus(50, 30, 1, 3);
        Geometry g = new Geometry("Torus Geom", torus);
        g.rotate(-FastMath.HALF_PI, 0, 0);
        g.center();
//        g.move(0, 1, 0);
        
        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setFloat("Shininess", 32f);
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Ambient",  ColorRGBA.Black);
        mat.setColor("Diffuse",  ColorRGBA.White);
        mat.setColor("Specular", ColorRGBA.White);
//        mat.setBoolean("VertexLighting", true);
//        mat.setBoolean("LowQuality", true);
        g.setMaterial(mat);

        rootNode.attachChild(g);

        lightMdl = new Geometry("Light", new Sphere(10, 10, 0.1f));
        lightMdl.setMaterial(assetManager.loadMaterial("Common/Materials/RedColor.j3m"));
        rootNode.attachChild(lightMdl);

        pl = new PointLight();
        pl.setColor(ColorRGBA.Green);
        pl.setRadius(4f);
        rootNode.addLight(pl);

        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.Red);
        dl.setDirection(new Vector3f(0, 1, 0));
        rootNode.addLight(dl);
    }

    @Override
    public void simpleUpdate(float tpf){
//        cam.setLocation(new Vector3f(5.0347548f, 6.6481347f, 3.74853f));
//        cam.setRotation(new Quaternion(-0.19183293f, 0.80776674f, -0.37974006f, -0.40805697f));

        pos += tpf * vel * 5f;
        if (pos > 15){
            vel *= -1;
        }else if (pos < -15){
            vel *= -1;
        }
        
        pl.setPosition(new Vector3f(pos, 2, 0));
        lightMdl.setLocalTranslation(pl.getPosition());
    }

}
