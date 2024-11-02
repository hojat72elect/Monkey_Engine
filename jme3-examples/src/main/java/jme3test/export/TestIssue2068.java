
package jme3test.export;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.export.JmeExporter;
import com.jme3.export.xml.XMLExporter;
import com.jme3.export.xml.XMLImporter;
import com.jme3.scene.Spatial;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Test case for JME issue: #2068 exporting a Map to XML results in a
 * DOMException.
 *
 * <p>If the issue is unresolved, the application will exit prematurely with an
 * uncaught exception.
 *
 * <p>If the issue is resolved, the application will complete normally.
 *
 * @author Stephen Gold sgold@sonic.net
 */
public class TestIssue2068 extends SimpleApplication {
    // *************************************************************************
    // constants and loggers
    
    /**
     * message logger for this class
     */
    final public static Logger logger
            = Logger.getLogger(TestIssue2068.class.getName());
    // *************************************************************************
    // new methods exposed

    /**
     * Main entry point for the TestIssue2068 application.
     *
     * @param args array of command-line arguments (not null)
     */
    public static void main(String[] args) {
        TestIssue2068 app = new TestIssue2068();
        app.start();
    }

    /**
     * Initialize the application.
     */
    @Override
    public void simpleInitApp() {
        
        ArrayList<String> list = new ArrayList<>();
        list.add("list-value");
        rootNode.setUserData("list", list);
        
        Map<String, String> map = new HashMap<>();
        map.put("map-key", "map-value");
        rootNode.setUserData("map", map);
        
        String[] array = new String[1];
        array[0] = "array-value";
        rootNode.setUserData("array", array);
        
        // export xml
        String filename = "TestIssue2068.xml";
        File xmlFile = new File(filename);
        JmeExporter exporter = XMLExporter.getInstance();
        try {
            exporter.save(rootNode, xmlFile);
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }
        
        // import binary/xml
        assetManager.registerLocator("", FileLocator.class);
        assetManager.registerLoader(XMLImporter.class, "xml");
        Spatial model = assetManager.loadModel(filename);
        //Spatial model = assetManager.loadModel("Models/Jaime/Jaime.j3o");
        model.depthFirstTraversal((Spatial spatial) -> {
            System.out.println("UserData for "+spatial);
            for (String key : spatial.getUserDataKeys()) {
                System.out.println("  "+key+": "+spatial.getUserData(key));
            }
        });
        
        stop();
        
    }
}
