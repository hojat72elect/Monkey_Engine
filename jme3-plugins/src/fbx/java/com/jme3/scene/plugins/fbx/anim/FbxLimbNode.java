
package com.jme3.scene.plugins.fbx.anim;

import com.jme3.animation.Bone;
import com.jme3.animation.Skeleton;
import com.jme3.asset.AssetManager;
import com.jme3.scene.plugins.fbx.node.FbxNode;
import java.util.ArrayList;
import java.util.List;

public class FbxLimbNode extends FbxNode {
    
    protected FbxNode skeletonHolder;
    protected Bone bone;
    
    public FbxLimbNode(AssetManager assetManager, String sceneFolderName) {
        super(assetManager, sceneFolderName);
    }
    
    private static void createBones(FbxNode skeletonHolderNode, FbxLimbNode limb, List<Bone> bones) {
        limb.skeletonHolder = skeletonHolderNode;
        
        Bone parentBone = limb.getJmeBone();
        bones.add(parentBone);
        
        for (FbxNode child : limb.children) {
            if (child instanceof FbxLimbNode) {
                FbxLimbNode childLimb = (FbxLimbNode) child;
                createBones(skeletonHolderNode, childLimb, bones);
                parentBone.addChild(childLimb.getJmeBone());
            }
        }
    }
    
    public static Skeleton createSkeleton(FbxNode skeletonHolderNode) {
        if (skeletonHolderNode instanceof FbxLimbNode) {
            throw new UnsupportedOperationException("Limb nodes cannot be skeleton holders");
        }
        
        List<Bone> bones = new ArrayList<>();
        
        for (FbxNode child : skeletonHolderNode.getChildren()) {
            if (child instanceof FbxLimbNode) {
                createBones(skeletonHolderNode, (FbxLimbNode) child, bones);
            }
        }
        
        return new Skeleton(bones.toArray(new Bone[0]));
    }
    
    public FbxNode getSkeletonHolder() {
        return skeletonHolder;
    }
    
    public Bone getJmeBone() {
        if (bone == null) {
            bone = new Bone(name);
            bone.setBindTransforms(jmeLocalBindPose.getTranslation(),
                                   jmeLocalBindPose.getRotation(),
                                   jmeLocalBindPose.getScale());
        }
        return bone;
    }
}
