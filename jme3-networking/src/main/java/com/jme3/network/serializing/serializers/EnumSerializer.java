
package com.jme3.network.serializing.serializers;

import com.jme3.network.serializing.Serializer;
import com.jme3.network.serializing.SerializerException;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Enum serializer.
 *
 * @author Lars Wesselius
 */
public class EnumSerializer extends Serializer {
    @Override
    public <T> T readObject(ByteBuffer data, Class<T> c) throws IOException {
        try {
            int ordinal = data.getInt();

            if (ordinal == -1) return null;
            T[] enumConstants = c.getEnumConstants();
            if (enumConstants == null) {
                throw new SerializerException("Class has no enum constants:" + c + "  Ordinal:" + ordinal);
            }
            return enumConstants[ordinal];
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    @Override
    public void writeObject(ByteBuffer buffer, Object object) throws IOException {
        if (object == null) {
            buffer.putInt(-1);
        } else {
            buffer.putInt(((Enum)object).ordinal());
        }
    }
}
