
package com.jme3.scene.plugins.fbx.file;

import java.util.ArrayList;
import java.util.List;

public class FbxFile {

    public List<FbxElement> rootElements = new ArrayList<>();
    public long version;

    /**
     * Between file versions 7400 and 7500, the "endOffset", "propCount", and
     * "propsLength" fields in an FBX element were extended, from 4 bytes to 8
     * bytes each.
     *
     * @return true for 8-byte offsets, otherwise false
     */
    public boolean hasExtendedOffsets() {
        if (version >= 7500L) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Between file versions 7400 and 7500, the FBX block sentinel was reduced,
     * from 13 bytes to 9 bytes.
     *
     * @return the number of bytes in the block sentinel (&ge;0)
     */
    public int numSentinelBytes() {
        if (version >= 7500L) {
            return 9;
        } else {
            return 13;
        }
    }

    @Override
    public String toString() {
        return "FBXFile[version=" + version + ",numElements=" + rootElements.size() + "]";
    }
}
