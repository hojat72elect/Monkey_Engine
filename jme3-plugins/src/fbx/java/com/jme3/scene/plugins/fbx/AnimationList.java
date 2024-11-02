
package com.jme3.scene.plugins.fbx;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Defines animations set that will be created while loading FBX scene
 * <p>Animation <code>name</code> is using to access animation via {@link com.jme3.animation.AnimControl}.<br>
 * <code>firstFrame</code> and <code>lastFrame</code> defines animation time interval.<br>
 * Use <code>layerName</code> also to define source animation layer in the case of multiple layers in the scene.<br>
 * Skeletal animations will be created if only scene contain skeletal bones</p>
 */
public class AnimationList {

    List<AnimInverval> list = new ArrayList<>();

    /**
     * Use in the case of multiple animation layers in FBX asset
     *
     * @param name - animation name to access via {@link com.jme3.animation.AnimControl}
     * @param firstFrame the index of the first frame
     * @param lastFrame the index of the last frame
     */
    public void add(String name, int firstFrame, int lastFrame) {
        add(name, null, firstFrame, lastFrame);
    }

    /**
     * Use in the case of multiple animation layers in FBX asset
     *
     * @param name - animation name to access via {@link com.jme3.animation.AnimControl}
     * @param layerName - source layer
     * @param firstFrame the index of the first frame
     * @param lastFrame the index of the last frame
     */
    public void add(String name, String layerName, int firstFrame, int lastFrame) {
        AnimInverval cue = new AnimInverval();
        cue.name = name;
        cue.layerName = layerName;
        cue.firstFrame = firstFrame;
        cue.lastFrame = lastFrame;
        list.add(cue);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AnimationList other = (AnimationList)obj;
        return Objects.equals(this.list, other.list);
    }
    
    @Override
    public int hashCode() {
        return 119 + Objects.hashCode(this.list);
    }    

    static class AnimInverval {
        String name;
        String layerName;
        int firstFrame;
        int lastFrame;
        // hashCode generator, for good measure
        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + Objects.hashCode(this.name);
            hash = 29 * hash + Objects.hashCode(this.layerName);
            hash = 29 * hash + this.firstFrame;
            hash = 29 * hash + this.lastFrame;
            return hash;
        }
    }
}
