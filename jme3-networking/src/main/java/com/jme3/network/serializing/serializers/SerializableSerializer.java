
package com.jme3.network.serializing.serializers;

import com.jme3.network.serializing.Serializer;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * Serializes uses Java built-in method.
 *
 * TODO
 * @author Lars Wesselius
 */
@SuppressWarnings("unchecked")
public class SerializableSerializer extends Serializer {

    @Override
    public Serializable readObject(ByteBuffer data, Class c) throws IOException {
        throw new UnsupportedOperationException( "Serializable serialization not supported." );
    }

    @Override
    public void writeObject(ByteBuffer buffer, Object object) throws IOException {
        throw new UnsupportedOperationException( "Serializable serialization not supported." );
    }
}
