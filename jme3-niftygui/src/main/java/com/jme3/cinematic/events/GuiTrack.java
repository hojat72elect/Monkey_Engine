
package com.jme3.cinematic.events;

import com.jme3.animation.LoopMode;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import java.io.IOException;

/**
 *
 * @author Nehon
 * @deprecated use GuiEvent instead
 */
@Deprecated
public class GuiTrack extends AbstractCinematicEvent {

    protected String screen;
    protected Nifty nifty;

    public GuiTrack() {
    }

    public GuiTrack(Nifty nifty, String screen) {
        this.screen = screen;
        this.nifty = nifty;
    }

    public GuiTrack(Nifty nifty, String screen, float initialDuration) {
        super(initialDuration);
        this.screen = screen;
        this.nifty = nifty;
    }

    public GuiTrack(Nifty nifty, String screen, LoopMode loopMode) {
        super(loopMode);
        this.screen = screen;
        this.nifty = nifty;
    }

    public GuiTrack(Nifty nifty, String screen, float initialDuration, LoopMode loopMode) {
        super(initialDuration, loopMode);
        this.screen = screen;
        this.nifty = nifty;
    }

    @Override
    public void onPlay() {
        System.out.println("screen should be " + screen);
        nifty.gotoScreen(screen);
    }

    @Override
    public void onStop() {
        Screen currentScreen = nifty.getCurrentScreen();
        if (currentScreen != null) {
            currentScreen.endScreen(null);
        }
    }

    @Override
    public void onPause() {
    }

    public void setNifty(Nifty nifty) {
        this.nifty = nifty;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    @Override
    public void onUpdate(float tpf) {
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(screen, "screen", "");
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        screen = ic.readString("screen", "");
    }
}
