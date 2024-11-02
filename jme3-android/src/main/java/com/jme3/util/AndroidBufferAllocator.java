

package com.jme3.util;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jesus Oliver
 * @deprecated implemented {@link AndroidNativeBufferAllocator} instead.
 */
@Deprecated
public class AndroidBufferAllocator implements BufferAllocator {

    // We make use of the ReflectionAllocator to remove the inner buffer
    private static final ReflectionAllocator reflectionAllocator = new ReflectionAllocator();

    private static final String[] wrapperClassNames = {
            "java.nio.ByteBufferAsFloatBuffer",
            "java.nio.ByteBufferAsIntBuffer",
            "java.nio.ByteBufferAsDoubleBuffer",
            "java.nio.ByteBufferAsShortBuffer",
            "java.nio.ByteBufferAsLongBuffer",
            "java.nio.ByteBufferAsCharBuffer",
    };
    private static final String[] possibleBufferFieldNames = {"bb", "byteBuffer"};

    // Keep track of ByteBuffer field by the wrapper class
    private static final Map<Class, Field> fieldIndex = new HashMap<>();

    static {
        for (String className : wrapperClassNames) {
            try {
                Class clazz = Class.forName(className);

                // loop for all possible field names in android
                for (String fieldName : possibleBufferFieldNames) {
                    try {
                        Field field = clazz.getDeclaredField(fieldName);
                        field.setAccessible(true);
                        fieldIndex.put(clazz, field);
                        break;
                    } catch (NoSuchFieldException e) {
                    }
                }
            } catch (ClassNotFoundException ex) {
            }
        }
    }

    /**
     * Searches the inner direct buffer of the Android-specific wrapped buffer classes
     * and destroys it using the reflection allocator method.
     *
     * @param toBeDestroyed The direct buffer that will be "cleaned".
     *
     */
    @Override
    public void destroyDirectBuffer(Buffer toBeDestroyed) {
        // If it is a wrapped buffer, get it's inner direct buffer field and destroy it
        Field field = fieldIndex.get(toBeDestroyed.getClass());
        if (field != null) {
            try {
                ByteBuffer innerBuffer = (ByteBuffer) field.get(toBeDestroyed);
                if (innerBuffer != null) {
                    // Destroy it using the reflection method
                    reflectionAllocator.destroyDirectBuffer(innerBuffer);
                }
            } catch (IllegalAccessException ex) {
            }

        } else {
            // It is not a wrapped buffer, use default reflection allocator to remove it instead.
            reflectionAllocator.destroyDirectBuffer(toBeDestroyed);
        }
    }

    @Override
    public ByteBuffer allocate(int size) {
        return ByteBuffer.allocateDirect(size);
    }
}

