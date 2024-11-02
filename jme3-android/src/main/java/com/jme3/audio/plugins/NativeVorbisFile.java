
package com.jme3.audio.plugins;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Represents the android implementation for the native <a href="https://xiph.org"> vorbis file decoder.</a>
 * This decoder initializes an OggVorbis_File from an already opened file designated by the {@link NativeVorbisFile#fd}.
 * 
 * @author Kirill Vainer
 * @author Modified by pavl_g
 */
public class NativeVorbisFile {
    
    public int fd;
    public ByteBuffer ovf;
    public boolean seekable;
    public int channels;
    public int sampleRate;
    public int bitRate;
    public int totalBytes;
    public float duration;
    
    static {
        System.loadLibrary("decodejme");
        preInit();
    }
    
    /**
     * Initializes an ogg vorbis native file from a file descriptor [fd] of an already opened file.
     * 
     * @param fd an integer representing the file descriptor 
     * @param offset an integer indicating the start of the buffer
     * @param length an integer indicating the end of the buffer
     * @throws IOException in cases of a failure to initialize the vorbis file 
     */
    public NativeVorbisFile(int fd, long offset, long length) throws IOException {
        init(fd, offset, length);
    }
    
    /**
     * Seeks to a playback time relative to the decompressed pcm (Pulse-code modulation) stream.
     * 
     * @param time the playback seek time
     * @throws IOException if the seek is not successful
     */
    public native void seekTime(double time) throws IOException;
    
    /**
     * Reads the vorbis file into a primitive byte buffer [buf] with an [offset] indicating the start byte and a [length] indicating the end byte on the output buffer.
     * 
     * @param buffer a primitive byte buffer to read the data into it
     * @param offset an integer representing the offset or the start byte on the output buffer
     * @param length an integer representing the end byte on the output buffer
     * @return the number of the read bytes, (-1) if the reading has failed indicating an EOF, 
     *         returns (0) if the reading has failed or the primitive [buffer] passed is null
     * @throws IOException if the library has failed to read the file into the [out] buffer
     *                     or if the java primitive byte array [buffer] is inaccessible
     */
    public native int readIntoArray(byte[] buffer, int offset, int length) throws IOException;
    
    /**
     * Reads the vorbis file into a direct {@link java.nio.ByteBuffer}, starting from offset [0] till the buffer end on the output buffer.
     * 
     * @param out a reference to the output direct buffer 
     * @throws IOException if a premature EOF is encountered before reaching the end of the buffer
     *                     or if the library has failed to read the file into the [out] buffer
     */
    public native void readIntoBuffer(ByteBuffer out) throws IOException;
    
    /**
     * Clears the native resources and destroys the buffer {@link NativeVorbisFile#ovf} reference.
     */
    public native void clearResources();
    
    /**
     * Prepares the java fields for the native environment.
     */
    private static native void preInit();
    
    /**
     * Initializes an ogg vorbis native file from a file descriptor [fd] of an already opened file.
     * 
     * @param fd an integer representing the file descriptor 
     * @param offset an integer representing the start of the buffer
     * @param length an integer representing the length of the buffer
     * @throws IOException in cases of a failure to initialize the vorbis file 
     */
    private native void init(int fd, long offset, long length) throws IOException;
}
