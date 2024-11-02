
package com.jme3.network.serializing.serializers;

import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Byte serializer.
 *
 * @author Lars Wesselius
 */
@SuppressWarnings("unchecked")
public class ByteSerializer extends Serializer {

    @Override
    public Byte readObject(ByteBuffer data, Class c) throws IOException {
        return data.get();
    }

    @Override
    public void writeObject(ByteBuffer buffer, Object object) throws IOException {
        buffer.put((Byte)object);
    }
}
