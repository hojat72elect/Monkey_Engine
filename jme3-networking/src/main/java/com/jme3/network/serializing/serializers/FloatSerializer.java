
package com.jme3.network.serializing.serializers;

import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Float serializer.
 *
 * @author Lars Wesselius
 */
@SuppressWarnings("unchecked")
public class FloatSerializer extends Serializer {

    @Override
    public Float readObject(ByteBuffer data, Class c) throws IOException {
        return data.getFloat();
    }

    @Override
    public void writeObject(ByteBuffer buffer, Object object) throws IOException {
        buffer.putFloat((Float)object);
    }
}
