
package com.jme3.util;

import java.io.*;

/**
 * <code>LittleEndien</code> is a class to read little-endian stored data
 * via an InputStream.  All functions work as defined in DataInput, but
 * assume they come from a LittleEndien input stream.  Currently used to read .ms3d and .3ds files.
 * @author Jack Lindamood
 */
public class LittleEndien extends InputStream implements DataInput {

    protected BufferedInputStream in;

    /**
     * Creates a new LittleEndien reader from the given input stream.  The
     * stream is wrapped in a BufferedReader automatically.
     * @param in The input stream to read from.
     */
    public LittleEndien(InputStream in) {
        this.in = new BufferedInputStream(in);
    }

    @Override
    public int read() throws IOException {
        return in.read();
    }

    @Override
    public int read(byte[] buf) throws IOException {
        return in.read(buf);
    }

    @Override
    public int read(byte[] buf, int off, int len) throws IOException {
        return in.read(buf, off, len);
    }

    @Override
    public int readUnsignedShort() throws IOException {
        return (in.read() & 0xff) | ((in.read() & 0xff) << 8);
    }

    /**
     * read an unsigned int as a long
     *
     * @return the value that was read
     * @throws IOException if an I/O error occurs while reading
     */
    public long readUInt() throws IOException {
        return ((in.read() & 0xff)
                | ((in.read() & 0xff) << 8)
                | ((in.read() & 0xff) << 16)
                | (((long) (in.read() & 0xff)) << 24));
    }

    @Override
    public boolean readBoolean() throws IOException {
        return (in.read() != 0);
    }

    @Override
    public byte readByte() throws IOException {
        return (byte) in.read();
    }

    @Override
    public int readUnsignedByte() throws IOException {
        return in.read();
    }

    @Override
    public short readShort() throws IOException {
        return (short) this.readUnsignedShort();
    }

    @Override
    public char readChar() throws IOException {
        return (char) this.readUnsignedShort();
    }

    @Override
    public int readInt() throws IOException {
        return ((in.read() & 0xff)
                | ((in.read() & 0xff) << 8)
                | ((in.read() & 0xff) << 16)
                | ((in.read() & 0xff) << 24));
    }

    @Override
    public long readLong() throws IOException {
        return ((in.read() & 0xff)
                | ((long) (in.read() & 0xff) << 8)
                | ((long) (in.read() & 0xff) << 16)
                | ((long) (in.read() & 0xff) << 24)
                | ((long) (in.read() & 0xff) << 32)
                | ((long) (in.read() & 0xff) << 40)
                | ((long) (in.read() & 0xff) << 48)
                | ((long) (in.read() & 0xff) << 56));
    }

    @Override
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    @Override
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    @Override
    public void readFully(byte b[]) throws IOException {
        in.read(b, 0, b.length);
    }

    @Override
    public void readFully(byte b[], int off, int len) throws IOException {
        in.read(b, off, len);
    }

    @Override
    public int skipBytes(int n) throws IOException {
        return (int) in.skip(n);
    }

    @Override
    public String readLine() throws IOException {
        throw new IOException("Unsupported operation");
    }

    @Override
    public String readUTF() throws IOException {
        throw new IOException("Unsupported operation");
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

    @Override
    public int available() throws IOException {
        return in.available();
    }
}
