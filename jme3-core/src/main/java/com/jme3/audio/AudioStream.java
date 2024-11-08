
package com.jme3.audio;

import com.jme3.util.NativeObject;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * <code>AudioStream</code> is an implementation of AudioData that acquires the
 * audio from an InputStream. Audio can be streamed from network, hard drive
 * etc. It is assumed the data coming from the input stream is uncompressed.
 *
 * @author Kirill Vainer
 */
public class AudioStream extends AudioData implements Closeable {

    private final static Logger logger = Logger.getLogger(AudioStream.class.getName());
    protected InputStream in;
    protected float duration = -1f;
    protected boolean open = false;
    protected boolean eof = false;
    protected int[] ids;

    protected int unqueuedBuffersBytes = 0;
    
    public AudioStream() {
        super();
    }

    protected AudioStream(int[] ids) {
        // Pass some dummy ID so handle
        // doesn't get created.
        super(-1);
        // This is what gets destroyed in reality
        this.ids = ids;
    }

    public void updateData(InputStream in, float duration) {
        if (id != -1 || this.in != null) {
            throw new IllegalStateException("Data already set!");
        }

        this.in = in;
        this.duration = duration;
        open = true;
    }

    /**
     * Reads samples from the stream. The format of the data depends on the
     * getSampleRate(), getChannels(), getBitsPerSample() values.
     *
     * @param buf Buffer where to read the samples
     * @param offset The offset in the buffer where to read samples
     * @param length The length inside the buffer where to read samples
     * @return number of bytes read.
     */
    public int readSamples(byte[] buf, int offset, int length) {
        if (!open || eof) {
            return -1;
        }

        try {
            int totalRead = in.read(buf, offset, length);
            if (totalRead < 0) {
                eof = true;
            }
            return totalRead;
        } catch (IOException ex) {
            ex.printStackTrace();
            eof = true;
            return -1;
        }
    }

    /**
     * Reads samples from the stream.
     *
     * @see AudioStream#readSamples(byte[], int, int)
     * @param buf Buffer where to read the samples
     * @return number of bytes read.
     */
    public int readSamples(byte[] buf) {
        return readSamples(buf, 0, buf.length);
    }

    @Override
    public float getDuration() {
        return duration;
    }

    @Override
    public int getId() {
        throw new RuntimeException("Don't use getId() on streams");
    }

    @Override
    public void setId(int id) {
        throw new RuntimeException("Don't use setId() on streams");
    }

    public void initIds(int count) {
        ids = new int[count];
    }

    public int getId(int index) {
        return ids[index];
    }

    public void setId(int index, int id) {
        ids[index] = id;
    }

    public int[] getIds() {
        return ids;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }

    @Override
    public DataType getDataType() {
        return DataType.Stream;
    }

    @Override
    public void resetObject() {
        id = -1;
        ids = null;
        setUpdateNeeded();
    }

    @Override
    public void deleteObject(Object rendererObject) {
        ((AudioRenderer) rendererObject).deleteAudioData(this);
    }

    @Override
    public NativeObject createDestructableClone() {
        return new AudioStream(ids);
    }
    
    public boolean isEOF() {
        return eof;
    }
    
    /**
     * Closes the stream, releasing all data relating to it. 
     * Reading from the stream will return eof.
     */
    @Override
    public void close() {
        if (in != null && open) {
            try {
                in.close();
            } catch (IOException ex) {
            }
            open = false;
        } else {
            throw new RuntimeException("AudioStream is already closed!");
        }
    }
    
    public boolean isSeekable() {
        return in instanceof SeekableStream;
    }

    public int getUnqueuedBufferBytes() {
        return unqueuedBuffersBytes;
    }

    public void setUnqueuedBufferBytes(int unqueuedBuffers) {
        this.unqueuedBuffersBytes = unqueuedBuffers;
    }
    
    public void setTime(float time) {
        if (in instanceof SeekableStream) {
            ((SeekableStream) in).setTime(time);
            eof = false;
            
            // TODO: when we actually support seeking, this will need to be properly set.
            unqueuedBuffersBytes = 0; 
        } else {
            throw new IllegalStateException(
                    "Cannot use setTime on a stream that "
                    + "is not seekable. You must load the file "
                    + "with the streamCache option set to true");
        }
    }

    @Override
    public long getUniqueId() {
        return ((long) OBJTYPE_AUDIOSTREAM << 32) | (0xffffffffL & (long) ids[0]);
    }
}
