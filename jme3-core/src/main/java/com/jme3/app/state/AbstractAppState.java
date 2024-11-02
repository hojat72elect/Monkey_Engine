
package com.jme3.app.state;

import com.jme3.app.Application;
import com.jme3.renderer.RenderManager;

/**
 * <code>AbstractAppState</code> implements some common methods
 * that make creation of AppStates easier.
 * @author Kirill Vainer
 * @see com.jme3.app.state.BaseAppState
 */
public abstract class AbstractAppState implements AppState {

    /**
     * <code>initialized</code> is set to true when the method
     * {@link AbstractAppState#initialize(com.jme3.app.state.AppStateManager, com.jme3.app.Application) }
     * is called. When {@link AbstractAppState#cleanup() } is called, <code>initialized</code>
     * is set back to false.
     */
    protected boolean initialized = false;
    private boolean enabled = true;
    private String id;

    protected AbstractAppState() {
    }

    protected AbstractAppState(String id) {
        this.id = id;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    /**
     *  Sets the unique ID of this app state.  Note: that setting
     *  this while an app state is attached to the state manager will
     *  have no effect on ID-based lookups.
     *
     * @param id the desired ID
     */
    protected void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void stateAttached(AppStateManager stateManager) {
    }

    @Override
    public void stateDetached(AppStateManager stateManager) {
    }

    @Override
    public void update(float tpf) {
    }

    @Override
    public void render(RenderManager rm) {
    }

    @Override
    public void postRender() {
    }

    @Override
    public void cleanup() {
        initialized = false;
    }
}
