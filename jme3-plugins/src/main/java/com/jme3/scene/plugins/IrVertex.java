
package com.jme3.scene.plugins;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import java.util.Arrays;

public class IrVertex implements Cloneable {

    public Vector3f pos;
    public Vector3f norm;
    public Vector4f tang4d;
    public Vector3f tang;
    public Vector3f bitang;
    public Vector2f uv0;
    public Vector2f uv1;
    public ColorRGBA color;
    public Integer material;
    public Integer smoothing;
    public IrBoneWeightIndex[] boneWeightsIndices;

    public IrVertex deepClone() {
        IrVertex v = new IrVertex();
        v.pos    = pos != null ? pos.clone() : null;
        v.norm   = norm != null ? norm.clone() : null;
        v.tang4d = tang4d != null ? tang4d.clone() : null;
        v.tang = tang != null ? tang.clone() : null;
        v.bitang = bitang != null ? bitang.clone() : null;
        v.uv0 = uv0 != null ? uv0.clone() : null;
        v.uv1 = uv1 != null ? uv1.clone() : null;
        v.color = color != null ? color.clone() : null;
        v.material = material;
        v.smoothing = smoothing;
        if (boneWeightsIndices != null) {
            v.boneWeightsIndices = new IrBoneWeightIndex[boneWeightsIndices.length];
            for (int i = 0; i < boneWeightsIndices.length; i++) {
                v.boneWeightsIndices[i] = (IrBoneWeightIndex) boneWeightsIndices[i].clone();
            }
        }
        return v;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + (this.pos != null ? this.pos.hashCode() : 0);
        hash = 73 * hash + (this.norm != null ? this.norm.hashCode() : 0);
        hash = 73 * hash + (this.tang4d != null ? this.tang4d.hashCode() : 0);
        hash = 73 * hash + (this.tang != null ? this.tang.hashCode() : 0);
        hash = 73 * hash + (this.uv0 != null ? this.uv0.hashCode() : 0);
        hash = 73 * hash + (this.uv1 != null ? this.uv1.hashCode() : 0);
        hash = 73 * hash + (this.color != null ? this.color.hashCode() : 0);
        hash = 73 * hash + (this.material != null ? this.material.hashCode() : 0);
        hash = 73 * hash + (this.smoothing != null ? this.smoothing.hashCode() : 0);
        hash = 73 * hash + Arrays.deepHashCode(this.boneWeightsIndices);
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
        final IrVertex other = (IrVertex) obj;
        if (this.pos != other.pos && (this.pos == null || !this.pos.equals(other.pos))) {
            return false;
        }
        if (this.norm != other.norm && (this.norm == null || !this.norm.equals(other.norm))) {
            return false;
        }
        if (this.tang4d != other.tang4d && (this.tang4d == null || !this.tang4d.equals(other.tang4d))) {
            return false;
        }
        if (this.tang != other.tang && (this.tang == null || !this.tang.equals(other.tang))) {
            return false;
        }
        if (this.uv0 != other.uv0 && (this.uv0 == null || !this.uv0.equals(other.uv0))) {
            return false;
        }
        if (this.uv1 != other.uv1 && (this.uv1 == null || !this.uv1.equals(other.uv1))) {
            return false;
        }
        if (this.color != other.color && (this.color == null || !this.color.equals(other.color))) {
            return false;
        }
        if (this.material != other.material && (this.material == null || !this.material.equals(other.material))) {
            return false;
        }
        if (this.smoothing != other.smoothing && (this.smoothing == null || !this.smoothing.equals(other.smoothing))) {
            return false;
        }
        if (!Arrays.deepEquals(this.boneWeightsIndices, other.boneWeightsIndices)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vertex { ");

        if (pos != null) {
            sb.append("pos=").append(pos).append(", ");
        }
        if (norm != null) {
            sb.append("norm=").append(pos).append(", ");
        }
        if (tang != null) {
            sb.append("tang=").append(pos).append(", ");
        }
        if (uv0 != null) {
            sb.append("uv0=").append(pos).append(", ");
        }
        if (uv1 != null) {
            sb.append("uv1=").append(pos).append(", ");
        }
        if (color != null) {
            sb.append("color=").append(pos).append(", ");
        }
        if (material != null) {
            sb.append("material=").append(pos).append(", ");
        }
        if (smoothing != null) {
            sb.append("smoothing=").append(pos).append(", ");
        }

        if (sb.toString().endsWith(", ")) {
            sb.delete(sb.length() - 2, sb.length());
        }

        sb.append(" }");
        return sb.toString();
    }
}
