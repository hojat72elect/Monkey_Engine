
package jme3test.tools;

import com.jme3.app.SimpleApplication;
import com.jme3.light.AmbientLight;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import jme3tools.savegame.SaveGame;

public class TestSaveGame extends SimpleApplication {

    public static void main(String[] args) {

        TestSaveGame app = new TestSaveGame();
        app.start();
    }

    @Override
    public void simpleUpdate(float tpf) {
    }

    @Override
    public void simpleInitApp() {

        // Create a Node to store player data.
        Node myPlayer = new Node();
        myPlayer.setName("PlayerNode");
        myPlayer.setUserData("name", "Mario");
        myPlayer.setUserData("health", 100.0f);
        myPlayer.setUserData("points", 0);

        // Attach the model to the Node.
        Spatial model = assetManager.loadModel("Models/Oto/Oto.mesh.xml");
        myPlayer.attachChild(model);

        // Before saving the game, detach the model since it doesn't need to be saved.
        myPlayer.detachAllChildren();
        SaveGame.saveGame("mycompany/mygame", "savegame_001", myPlayer);

        // Later, the game is loaded again ...
        Node player = (Node) SaveGame.loadGame("mycompany/mygame", "savegame_001");
        player.attachChild(model);
        rootNode.attachChild(player);

        // and the player data are available.
        System.out.println("Name: " + player.getUserData("name"));
        System.out.println("Health: " + player.getUserData("health"));
        System.out.println("Points: " + player.getUserData("points"));

        AmbientLight al = new AmbientLight();
        rootNode.addLight(al);
        
        // Note that your custom classes can also implement the Savable interface.
    }
}
