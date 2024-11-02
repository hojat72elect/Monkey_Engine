
package com.jme3.app.state;

import com.jme3.app.Application;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;

/**
 * AppState that manages and updates a RootNode attached to a ViewPort, the
 * default Application ViewPort is used by default, a RootNode is created by
 * default.
 * @author normenhansen
 */
public class RootNodeAppState extends AbstractAppState {

    protected ViewPort viewPort;
    protected Node rootNode;

    /**
     * Creates the AppState with a new, empty root Node, attaches it to the
     * default Application ViewPort and updates it when attached to the
     * AppStateManager.
     */
    public RootNodeAppState() {
    }

    /**
     * Creates the AppState with the given ViewPort and creates a RootNode
     * that is attached to the given ViewPort and updates it when attached to the
     * AppStateManager.
     * @param viewPort An existing ViewPort
     */
    public RootNodeAppState(ViewPort viewPort) {
        this.viewPort = viewPort;
    }

    /**
     * Creates the AppState with the given root Node, uses the default
     * Application ViewPort and updates the root Node when attached to the
     * AppStateManager.
     * @param rootNode An existing root Node
     */
    public RootNodeAppState(Node rootNode) {
        this.rootNode = rootNode;
    }

    /**
     * Creates the AppState with the given ViewPort and root Node, attaches
     * the root Node to the ViewPort and updates it.
     * @param viewPort An existing ViewPort
     * @param rootNode An existing root Node
     */
    public RootNodeAppState(ViewPort viewPort, Node rootNode) {
        this.viewPort = viewPort;
        this.rootNode = rootNode;
    }

    /**
     * Creates the AppState with the given unique ID, ViewPort, and root Node, attaches
     * the root Node to the ViewPort and updates it.
     *
     * @param id the desired AppState ID
     * @param viewPort An existing ViewPort
     * @param rootNode An existing root Node
     */
    public RootNodeAppState(String id, ViewPort viewPort, Node rootNode) {
        super(id);
        this.viewPort = viewPort;
        this.rootNode = rootNode;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        if (rootNode == null) {
            rootNode = new Node("Root Node");
        }
        if (viewPort == null) {
            viewPort = app.getViewPort();
        }
        viewPort.attachScene(rootNode);
        super.initialize(stateManager, app);
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        rootNode.updateLogicalState(tpf);

        // FIXME: I'm 99% sure that updateGeometricState() should be
        // called in render() so that it is done as late as possible.
        // In complicated app state setups, cross-state chatter could
        // cause nodes (or their children) to be updated after this
        // app state's update has been called.  -pspeed:2019-09-15
        rootNode.updateGeometricState();
    }

    @Override
    public void cleanup() {
        viewPort.detachScene(rootNode);
        super.cleanup();
    }

    /**
     * Returns the managed rootNode.
     * @return The managed rootNode
     */
    public Node getRootNode() {
        return rootNode;
    }

    /**
     * Returns the used ViewPort
     * @return The used ViewPort
     */
    public ViewPort getViewPort() {
        return viewPort;
    }

}
