
package com.jme3.scene.plugins.ogre;

import com.jme3.anim.AnimClip;
import com.jme3.anim.Armature;

import java.util.ArrayList;

public class AnimData {

    public final Armature armature;
    public final ArrayList<AnimClip> anims;

    public AnimData(Armature armature, ArrayList<AnimClip> anims) {
        this.armature = armature;
        this.anims = anims;
    }
}
