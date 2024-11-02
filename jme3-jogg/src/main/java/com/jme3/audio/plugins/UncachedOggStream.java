
package com.jme3.audio.plugins;

import de.jarnbjo.ogg.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Single-threaded physical ogg stream. Decodes audio in the same thread
 * that reads.
 */
public class UncachedOggStream implements PhysicalOggStream {

    private boolean closed = false;
    private boolean eos = false;
    private boolean bos = false;
    private InputStream sourceStream;
    private LinkedList<OggPage> pageCache = new LinkedList<>();
    private HashMap<Integer, LogicalOggStream> logicalStreams 
            = new HashMap<>();
    private OggPage lastPage = null;

    public UncachedOggStream(InputStream in) throws OggFormatException, IOException {
        this.sourceStream = in;

        // read until beginning of stream
        while (!bos){
            readNextOggPage();
        }

        // now buffer up an addition 25 pages
//        while (pageCache.size() < 25 && !eos){
//            readNextOggPage();
//        }
    }

    public OggPage getLastOggPage() {
        return lastPage;
    }

    private void readNextOggPage() throws IOException {
        OggPage op = OggPage.create(sourceStream);
        if (!op.isBos()){
            bos = true;
        }
        if (op.isEos()){
            eos = true;
            lastPage = op;
        }

        LogicalOggStreamImpl los = (LogicalOggStreamImpl) getLogicalStream(op.getStreamSerialNumber());
        if (los == null){
            los = new LogicalOggStreamImpl(this, op.getStreamSerialNumber());
            logicalStreams.put(op.getStreamSerialNumber(), los);
            los.checkFormat(op);
        }

        pageCache.add(op);
    }

    @Override
    public OggPage getOggPage(int index) throws IOException {
        if (eos){
            return null;
        }

//        if (!eos){
//            int num = pageCache.size();
//            long fiveMillis = 5000000;
//            long timeStart  = System.nanoTime();
//            do {
//                readNextOggPage();
//            } while ( !eos && (System.nanoTime() - timeStart) < fiveMillis );
//            System.out.println( pageCache.size() - num );

            if (pageCache.size() == 0 /*&& !eos*/){
                readNextOggPage();
            }
//        }

        return pageCache.removeFirst();
    }

    private LogicalOggStream getLogicalStream(int serialNumber) {
        return logicalStreams.get(Integer.valueOf(serialNumber));
    }

    @Override
    public Collection<LogicalOggStream> getLogicalStreams() {
        return logicalStreams.values();
    }

    @Override
    public void setTime(long granulePosition) throws IOException {
    }

    @Override
    public boolean isSeekable() {
        return false;
    }

    @Override
    public boolean isOpen() {
        return !closed;
    }

    @Override
    public void close() throws IOException {
        closed = true;
        sourceStream.close();
    }
}
