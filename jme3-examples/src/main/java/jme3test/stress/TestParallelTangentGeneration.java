
package jme3test.stress;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;

public class TestParallelTangentGeneration {

    final private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            {
                Node root = new Node("Root");
                for (int count = 0; count < 10; count++) {
                    for (int samples = 4; samples < 50; samples++) {
                        Geometry g = new Geometry();
                        g.setMesh(new Sphere(samples, samples, 1.0f));
                        root.attachChild(g);
                    }
                }

                long start = System.currentTimeMillis();
                TangentBinormalGenerator.generate(root);
                System.out.println("Serial " + (System.currentTimeMillis() - start));
            }

            {
                Node root = new Node("Root");
                for (int count = 0; count < 10; count++) {
                    for (int samples = 4; samples < 50; samples++) {
                        Geometry g = new Geometry();
                        g.setMesh(new Sphere(samples, samples, 1.0f));
                        root.attachChild(g);
                    }
                }

                long start = System.currentTimeMillis();
                TangentBinormalGenerator.generateParallel(root, executor);
                System.out.println("Parallel " + (System.currentTimeMillis() - start));
            }

        }
    }
}