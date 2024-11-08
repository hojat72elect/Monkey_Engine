
package com.jme3.cinematic.events;

import com.jme3.animation.AnimationUtils;
import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.cinematic.Cinematic;
import com.jme3.cinematic.PlayState;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This call contains the basic behavior of a cinematic event.
 * Every cinematic event must extend this class.
 *
 * A cinematic event must be given an initial duration in seconds
 * (duration of the event at speed = 1). Default is 10 sec.
 * @author Nehon
 */
public abstract class AbstractCinematicEvent implements CinematicEvent {

    protected PlayState playState = PlayState.Stopped;
    protected LoopMode loopMode = LoopMode.DontLoop;
    protected float initialDuration = 10;
    protected float speed = 1;
    protected float time = 0;
    protected boolean resuming = false;

    /**
     * The list of listeners.
     */
    protected List<CinematicEventListener> listeners;

    /**
     * Construct a cinematic event (empty constructor).
     */
    public AbstractCinematicEvent() {
    }

    /**
     * Construct a cinematic event with the given initial duration.
     *
     * @param initialDuration the desired duration (in seconds, default=10)
     */
    public AbstractCinematicEvent(float initialDuration) {
        this.initialDuration = initialDuration;
    }

    /**
     * Construct a cinematic event with the given loopMode.
     *
     * @param loopMode the desired mode (Loop/DontLoop/Cycle)
     */
    public AbstractCinematicEvent(LoopMode loopMode) {
        this.loopMode = loopMode;
    }

    /**
     * Construct a cinematic event with the given loopMode and the given initialDuration.
     *
     * @param initialDuration the duration of the event at speed = 1.
     * @param loopMode the loop mode of the event.
     */
    public AbstractCinematicEvent(float initialDuration, LoopMode loopMode) {
        this.initialDuration = initialDuration;
        this.loopMode = loopMode;
    }

    /**
     * Implement this method if the event needs different handling when
     * stopped naturally (when the event reach its end),
     * or when it was force-stopped during playback.
     * By default, this method just calls regular stop().
     */
    @Override
    public void forceStop() {
        stop();
    }

    /**
     * Play this event.
     */
    @Override
    public void play() {
        onPlay();
        playState = PlayState.Playing;
        if (listeners != null) {
            for (int i = 0; i < listeners.size(); i++) {
                CinematicEventListener cel = listeners.get(i);
                cel.onPlay(this);
            }
        }
    }

    /**
     * Implement this method with code that you want to execute when the event is started.
     */
    protected abstract void onPlay();

    /**
     * Used internally only.
     * @param tpf time per frame.
     */
    @Override
    public void internalUpdate(float tpf) {
        if (playState == PlayState.Playing) {
            time = time + (tpf * speed);
            onUpdate(tpf);
            if (time >= initialDuration && loopMode == LoopMode.DontLoop) {
                stop();
            } else if (time >= initialDuration && loopMode == LoopMode.Loop) {
                setTime(0);
            } else {
                time = AnimationUtils.clampWrapTime(time, initialDuration, loopMode);
                if (time < 0) {
                    speed = -speed;
                    time = -time;
                }
            }
        }

    }

    /**
     * Implement this method with the code that you want to execute on update
     * (only called when the event is playing).
     * @param tpf time per frame
     */
    protected abstract void onUpdate(float tpf);

    /**
     * Stops the animation.
     * Next time when play() is called, the animation starts from the beginning.
     */
    @Override
    public void stop() {
        onStop();
        time = 0;
        playState = PlayState.Stopped;
        if (listeners != null) {
            for (int i = 0; i < listeners.size(); i++) {
                CinematicEventListener cel = listeners.get(i);
                cel.onStop(this);
            }
        }
    }

    /**
     * Implement this method with code that you want to execute when the event is stopped.
     */
    protected abstract void onStop();

    /**
     * Pause this event.
     * Next time when play() is called, the animation restarts from here.
     */
    @Override
    public void pause() {
        onPause();
        playState = PlayState.Paused;
        if (listeners != null) {
            for (int i = 0; i < listeners.size(); i++) {
                CinematicEventListener cel = listeners.get(i);
                cel.onPause(this);
            }
        }
    }

    /**
     * Implement this method with code that you want to execute when the event is paused.
     */
    public abstract void onPause();

    /**
     * Returns the actual duration of the animation (initialDuration/speed)
     * @return the duration (in seconds)
     */
    @Override
    public float getDuration() {
        return initialDuration / speed;
    }

    /**
     * Sets the speed of the animation.
     * At speed = 1, the animation will last initialDuration seconds,
     * At speed = 2, the animation will last initialDuration/2...
     * @param speed the desired speedup factor (default=1)
     */
    @Override
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /**
     * Returns the speed of the animation.
     * @return the speed
     */
    @Override
    public float getSpeed() {
        return speed;
    }

    /**
     * Returns the current play state of the animation (playing or paused or stopped).
     * @return the enum value
     */
    @Override
    public PlayState getPlayState() {
        return playState;
    }

    /**
     * Returns the initial duration of the animation at speed = 1 in seconds.
     * @return the duration in seconds
     */
    @Override
    public float getInitialDuration() {
        return initialDuration;
    }

    /**
     * Sets the duration of the animation at speed = 1 in seconds.
     * @param initialDuration the desired duration (in de-scaled seconds)
     */
    @Override
    public void setInitialDuration(float initialDuration) {
        this.initialDuration = initialDuration;
    }

    /**
     * Returns the loopMode of the animation.
     * @see LoopMode
     * @return the enum value
     */
    @Override
    public LoopMode getLoopMode() {
        return loopMode;
    }

    /**
     * Sets the loopMode of the animation.
     * @see LoopMode
     * @param loopMode the desired mode (default=DontLoop)
     */
    @Override
    public void setLoopMode(LoopMode loopMode) {
        this.loopMode = loopMode;
    }

    /**
     * Used for serialization only.
     * @param ex exporter
     * @throws IOException from the exporter
     */
    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(playState, "playState", PlayState.Stopped);
        oc.write(speed, "speed", 1);
        oc.write(initialDuration, "initalDuration", 10);
        oc.write(loopMode, "loopMode", LoopMode.DontLoop);
    }

    /**
     * Used for serialization only.
     * @param im importer
     * @throws IOException from the importer
     */
    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        playState = ic.readEnum("playState", PlayState.class, PlayState.Stopped);
        speed = ic.readFloat("speed", 1);
        initialDuration = ic.readFloat("initalDuration", 10);
        loopMode = ic.readEnum("loopMode", LoopMode.class, LoopMode.DontLoop);
    }

    /**
     * Initialize this event (called internally only).
     *
     * @param app ignored
     * @param cinematic ignored
     */
    @Override
    public void initEvent(Application app, Cinematic cinematic) {
    }

    /**
     * Returns the list of CinematicEventListeners added to this event.
     * @return
     */
    private List<CinematicEventListener> getListeners() {
        if (listeners == null) {
            listeners = new ArrayList<CinematicEventListener>();
        }
        return listeners;
    }

    /**
     * Adds a CinematicEventListener to this event.
     *
     * @param listener CinematicEventListener
     */
    public void addListener(CinematicEventListener listener) {
        getListeners().add(listener);
    }

    /**
     * Removes a CinematicEventListener from this event.
     *
     * @param listener CinematicEventListener
     */
    public void removeListener(CinematicEventListener listener) {
        getListeners().remove(listener);
    }

    /**
     * Fast-forwards the event to the given timestamp. Time=0 is the start of the event.
     *
     * @param time the time to fast-forward to.
     */
    @Override
    public void setTime(float time) {
        this.time = time;
    }

    /**
     * Return the current timestamp of the event. Time=0 is the start of the event.
     */
    @Override
    public float getTime() {
        return time;
    }

    @Override
    public void dispose() {
    }
}
