
package com.jme3.scene.plugins;

public class IrBoneWeightIndex implements Cloneable, Comparable<IrBoneWeightIndex> {
    
    int boneIndex;
    float boneWeight;

    public IrBoneWeightIndex(int boneIndex, float boneWeight) {
        this.boneIndex = boneIndex;
        this.boneWeight = boneWeight;
    }

    @Override
    public IrBoneWeightIndex clone() {
        try {
            return (IrBoneWeightIndex)super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError(ex);
        }
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.boneIndex;
        hash = 23 * hash + Float.floatToIntBits(this.boneWeight);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IrBoneWeightIndex other = (IrBoneWeightIndex) obj;
        if (this.boneIndex != other.boneIndex) {
            return false;
        }
        if (Float.floatToIntBits(this.boneWeight) != Float.floatToIntBits(other.boneWeight)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(IrBoneWeightIndex o) {
        if (boneWeight < o.boneWeight) {
            return 1;
        } else if (boneWeight > o.boneWeight) {
            return -1;
        } else {
            return 0;
        }
    }
}
