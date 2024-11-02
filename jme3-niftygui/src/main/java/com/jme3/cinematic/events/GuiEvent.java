
package com.jme3.cinematic.events;

import com.jme3.animation.LoopMode;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A CinematicEvent associated with a Nifty screen.
 *
 * @author Nehon
 */
public class GuiEvent extends AbstractCinematicEvent {

    /**
     * message logger for this class
     */
    private static final Logger log = Logger.getLogger(GuiEvent.class.getName());

    /**
     * name of the associated Nifty screen (not null)
     */
    protected String screen;
    /**
     * associated Nifty instance (not null)
     */
    protected Nifty nifty;

    /**
     * No-argument constructor needed by SavableClassUtil. Do not invoke
     * directly!
     */
    public GuiEvent() {
    }

    /**
     * Instantiate an event with the default initial duration (10) and mode
     * (DontLoop).
     *
     * @param nifty the Nifty instance (not null, alias created)
     * @param screen the name of the Nifty screen (not null)
     */
    public GuiEvent(Nifty nifty, String screen) {
        this.screen = screen;
        this.nifty = nifty;
    }

    /**
     * Instantiate an event the default mode (DontLoop).
     *
     * @param nifty the Nifty instance (not null, alias created)
     * @param screen the name of the Nifty screen (not null)
     * @param initialDuration the initial duration (in seconds, &ge;0)
     */
    public GuiEvent(Nifty nifty, String screen, float initialDuration) {
        super(initialDuration);
        this.screen = screen;
        this.nifty = nifty;
    }

    /**
     * Instantiate an event with the default initial duration (10).
     *
     * @param nifty the Nifty instance (not null, alias created)
     * @param screen the name of the Nifty screen (not null)
     * @param loopMode the loop mode (not null)
     */
    public GuiEvent(Nifty nifty, String screen, LoopMode loopMode) {
        super(loopMode);
        this.screen = screen;
        this.nifty = nifty;
    }

    /**
     * Instantiate an event with the specified initial duration and loop mode.
     *
     * @param nifty the Nifty instance (not null, alias created)
     * @param screen the name of the Nifty screen (not null)
     * @param initialDuration the initial duration (in seconds, &ge;0)
     * @param loopMode the loop mode (not null)
     */
    public GuiEvent(Nifty nifty, String screen, float initialDuration, LoopMode loopMode) {
        super(initialDuration, loopMode);
        this.screen = screen;
        this.nifty = nifty;
    }

    /**
     * Invoked when the event is started.
     */
    @Override
    public void onPlay() {
        log.log(Level.FINEST, "screen should be {0}", screen);
        nifty.gotoScreen(screen);
    }

    /**
     * Invoked when the event is stopped.
     */
    @Override
    public void onStop() {
        Screen currentScreen = nifty.getCurrentScreen();
        if (currentScreen != null) {
            currentScreen.endScreen(null);
        }
    }

    /**
     * Invoked when the event is paused.
     */
    @Override
    public void onPause() {
    }

    /**
     * Alter the Nifty instance for this event.
     *
     * @param nifty the new instance (not null)
     */
    public void setNifty(Nifty nifty) {
        this.nifty = nifty;
    }

    /**
     * Alter the screen for this event.
     *
     * @param screen the name of the new screen (not null)
     */
    public void setScreen(String screen) {
        this.screen = screen;
    }

    /**
     * Invoked once per frame, provided the event is playing.
     *
     * @param tpf the time interval between frames (in seconds, &ge;0)
     */
    @Override
    public void onUpdate(float tpf) {
    }

    /**
     * Serialize this event, for example when saving to a J3O file.
     *
     * @param ex exporter (not null)
     * @throws IOException from exporter
     */
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(screen, "screen", "");
    }

    /**
     * De-serialize this event, for example when loading from a J3O file.
     *
     * @param im importer (not null)
     * @throws IOException from importer
     */
    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        screen = ic.readString("screen", "");
    }
}
