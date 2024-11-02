
package com.jme3.animation;

import com.jme3.export.*;
import com.jme3.util.clone.Cloner;
import com.jme3.util.clone.JmeCloneable;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class is intended as a UserData added to a Spatial that is referenced by a Track.
 * (ParticleEmitter for EffectTrack and AudioNode for AudioTrack)
 * It holds the list of tracks that are directly referencing the Spatial.
 *
 * This is used when loading a Track to find the cloned reference of a Spatial in the cloned model returned by the assetManager.
 *
 * @author Nehon
 */
@Deprecated
public class TrackInfo implements Savable, JmeCloneable {

    ArrayList<Track> tracks = new ArrayList<>();

    public TrackInfo() {
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule c = ex.getCapsule(this);
        c.writeSavableArrayList(tracks, "tracks", null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void read(JmeImporter im) throws IOException {
        InputCapsule c = im.getCapsule(this);
        tracks = c.readSavableArrayList("tracks", null);
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    public void addTrack(Track track) {
        tracks.add(track);
    }

    @Override
    public Object jmeClone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Error cloning", e);
        }
    }

    @Override
    public void cloneFields(Cloner cloner, Object original) {
        this.tracks = cloner.clone(tracks);
    }
}
