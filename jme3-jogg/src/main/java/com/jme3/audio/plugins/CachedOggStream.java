
package com.jme3.audio.plugins;

import com.jme3.util.IntMap;
import de.jarnbjo.ogg.LogicalOggStream;
import de.jarnbjo.ogg.LogicalOggStreamImpl;
import de.jarnbjo.ogg.OggPage;
import de.jarnbjo.ogg.PhysicalOggStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Implementation of the <code>PhysicalOggStream</code> interface for reading
 *  and caching an Ogg stream from a URL. This class reads the data as fast as
 *  possible from the URL, caches it locally either in memory or on disk, and
 *  supports seeking within the available data.
 */
public class CachedOggStream implements PhysicalOggStream {

    private boolean closed = false;
    private boolean eos = false;
    private InputStream sourceStream;
    private HashMap<Integer, LogicalOggStream> logicalStreams 
            = new HashMap<>();
    
    private IntMap<OggPage> oggPages = new IntMap<>();
    private OggPage lastPage;
   
    private int pageNumber;
    private int serialNumber;
    
    public CachedOggStream(InputStream in) throws IOException {
        sourceStream = in;

        // Read all OGG pages in file
        long time = System.nanoTime();
        while (!eos){
            readOggNextPage();
        }
        long dt = System.nanoTime() - time;
        Logger.getLogger(CachedOggStream.class.getName()).log(Level.FINE, "Took {0} ms to load OGG", dt/1000000);
    }

    public OggPage getLastOggPage() {
        return lastPage;
    }
    
    @Override
    public Collection<LogicalOggStream> getLogicalStreams() {
        return logicalStreams.values();
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

    @Override
    public OggPage getOggPage(int index) throws IOException {
        return oggPages.get(index);
    }

   @Override
   public void setTime(long granulePosition) throws IOException {
       for (LogicalOggStream los : getLogicalStreams()){
           los.setTime(granulePosition);
       }
   }

    public LogicalOggStream reloadLogicalOggStream() {
        logicalStreams.clear();
        LogicalOggStreamImpl los = new LogicalOggStreamImpl(this, serialNumber);
        logicalStreams.put(serialNumber, los);

        for (IntMap.Entry<OggPage> entry : oggPages) {
            los.addPageNumberMapping(entry.getKey());
            los.addGranulePosition(entry.getValue().getAbsoluteGranulePosition());
        }

        return los;
    }
   
   private int readOggNextPage() throws IOException {
       if (eos)
           return -1;

       OggPage op = OggPage.create(sourceStream);
       op.isBos();
       if (op.isEos()){
           eos = true;
           lastPage = op;
       }

       LogicalOggStreamImpl los = (LogicalOggStreamImpl) logicalStreams.get(op.getStreamSerialNumber());
       if (los == null) {
           serialNumber = op.getStreamSerialNumber();
           los = new LogicalOggStreamImpl(this, op.getStreamSerialNumber());
           logicalStreams.put(op.getStreamSerialNumber(), los);
           los.checkFormat(op);
       }

       los.addPageNumberMapping(pageNumber);
       los.addGranulePosition(op.getAbsoluteGranulePosition());

       oggPages.put(pageNumber, op);
       pageNumber++;

       return pageNumber-1;
   }

   @Override
   public boolean isSeekable() {
      return true;
   }
}