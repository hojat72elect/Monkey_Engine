
package com.jme3.audio;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;

/**
 * <code>AudioListenerState</code> updates the audio listener's position,
 * orientation, and velocity from a {@link Camera}.
 *
 * @author Kirill Vainer
 */
public class AudioListenerState extends BaseAppState {

    private Listener listener;
    private Camera camera;
    private float lastTpf;

    public AudioListenerState() {
    }

    @Override
    protected void initialize(Application app) {
        this.camera = app.getCamera();
        this.listener = app.getListener();
    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    public void update(float tpf) {
        lastTpf = tpf;
    }

    @Override
    public void render(RenderManager rm) {
        if (!isEnabled() || listener == null) {
            return;
        }

        Vector3f lastLocation = listener.getLocation();
        Vector3f currentLocation = camera.getLocation();
        Vector3f velocity = listener.getVelocity();

        if (!lastLocation.equals(currentLocation)) {
            velocity.set(currentLocation).subtractLocal(lastLocation);
            velocity.multLocal(1f / lastTpf);
            listener.setLocation(currentLocation);
            listener.setVelocity(velocity);
        } else if (!velocity.equals(Vector3f.ZERO)) {
            listener.setVelocity(Vector3f.ZERO);
        }

        Quaternion lastRotation = listener.getRotation();
        Quaternion currentRotation = camera.getRotation();
        if (!lastRotation.equals(currentRotation)) {
            listener.setRotation(currentRotation);
        }
    }

    @Override
    protected void onEnable() {
    }

    @Override
    protected void onDisable() {
    }
}
