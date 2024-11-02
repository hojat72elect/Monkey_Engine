
package com.jme3.network.serializing.serializers;

import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Short serializer.
 *
 * @author Lars Wesselius
 */
@SuppressWarnings("unchecked")
public class ShortSerializer extends Serializer {
    @Override
    public Short readObject(ByteBuffer data, Class c) throws IOException {
        return data.getShort();
    }

    @Override
    public void writeObject(ByteBuffer buffer, Object object) throws IOException {
        buffer.putShort((Short)object);
    }
}
