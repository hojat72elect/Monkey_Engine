
package com.jme3.export;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <code>JmeExporter</code> specifies an export implementation for jME3 
 * data.
 */
public interface JmeExporter {
    
    /**
     * Export the {@link Savable} to an OutputStream.
     * 
     * @param object The savable to export
     * @param f The output stream
     * @throws IOException If an io exception occurs during export
     */
    public void save(Savable object, OutputStream f) throws IOException;
    
    /**
     * Export the {@link Savable} to a file. If the path to the file doesn't exist, the directories are
     * made.
     *
     * @param object The savable to export
     * @param f The file to export to
     * @throws IOException If an io exception occurs during export
     */
    default void save(Savable object, File f) throws IOException {
        save(object, f, true);
    }

    /**
     * Export the {@link Savable} to a file. If the path to the file doesn't exist, the parent
     * directories can be created if the <code>createDirectories</code> flag is true. If the path does
     * not exist and <code>createDirectories</code> is false, then an exception is thrown.
     *
     * @param object The savable to export
     * @param f The file to export to
     * @param createDirectories flag to indicate if the directories should be created
     * @throws IOException If an io exception occurs during export
     */
    public void save(Savable object, File f, boolean createDirectories) throws IOException;

    /**
     * Returns the {@link OutputCapsule} for the given savable object.
     * 
     * @param object The object to retrieve an output capsule for.
     * @return  the {@link OutputCapsule} for the given savable object.
     */
    public OutputCapsule getCapsule(Savable object);
}
