
package com.jme3.cinematic;

import com.jme3.cinematic.events.CinematicEvent;
import com.jme3.export.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nehon
 */
public class KeyFrame implements Savable {

    public KeyFrame() {
    }

    List<CinematicEvent> cinematicEvents = new ArrayList<>();
    private int index;

    public List<CinematicEvent> getCinematicEvents() {
        return cinematicEvents;
    }

    public void setCinematicEvents(List<CinematicEvent> cinematicEvents) {
        this.cinematicEvents = cinematicEvents;
    }

    public List<CinematicEvent> trigger() {
        for (CinematicEvent event : cinematicEvents) {
            event.play();
        }
        return cinematicEvents;
    }

    public boolean isEmpty() {
        return cinematicEvents.isEmpty();
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.writeSavableArrayList((ArrayList) cinematicEvents, "cinematicEvents", null);
        oc.write(index, "index", 0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        cinematicEvents = ic.readSavableArrayList("cinematicEvents", null);
        index = ic.readInt("index", 0);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
