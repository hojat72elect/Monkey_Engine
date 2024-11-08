
package com.jme3.scene.plugins.gltf;

import com.jme3.asset.AssetInfo;
import com.jme3.util.LittleEndien;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Nehon on 12/09/2017.
 */
public class GlbLoader extends GltfLoader {

    private static final int JSON_TYPE = 0x4E4F534A;
    /**
     * log diagnostic messages from this class
     */
    private static final Logger logger = Logger.getLogger(GlbLoader.class.getName());

    private ArrayList<byte[]> data = new ArrayList<>();

    @Override
    public Object load(AssetInfo assetInfo) throws IOException {
        data.clear();
        LittleEndien stream = new LittleEndien(new DataInputStream(assetInfo.openStream()));
        /* magic */ stream.readInt();

        int version = stream.readInt();
        if (version != 2) {
            logger.log(Level.SEVERE, "GlbLoader doesn''t support file version {0}.", version);
            throw new IOException("GLB file version = " + version);
        }

        int length = stream.readInt();

        byte[] json = null;

        //length is the total size, we have to remove the header size (3 integers = 12 bytes).
        length -= 12;

        while (length > 0) {
            int chunkLength = stream.readInt();
            int chunkType = stream.readInt();
            if (chunkType == JSON_TYPE) {
                json = new byte[chunkLength];
                stream.read(json);
            } else {
                byte[] bin = new byte[chunkLength];
                stream.read(bin);
                data.add(bin);
            }
            //8 is the byte size of the 2 ints chunkLength and chunkType.
            length -= chunkLength + 8;
        }

        if (json == null) {
            throw new IOException("No JSON chunk found.");
        }
        return loadFromStream(assetInfo, new ByteArrayInputStream(json));
    }

    @Override
    protected byte[] getBytes(int bufferIndex, String uri, Integer bufferLength) throws IOException {
        return data.get(bufferIndex);
    }

}
