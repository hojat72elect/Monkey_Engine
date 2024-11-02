
package com.jme3.cinematic.events;

/**
 *
 * @author Nehon
 */
public interface CinematicEventListener {

    public void onPlay(CinematicEvent cinematic);
    public void onPause(CinematicEvent cinematic);
    public void onStop(CinematicEvent cinematic);
}
