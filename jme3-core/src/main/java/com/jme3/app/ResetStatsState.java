
package com.jme3.app;

import com.jme3.app.state.AbstractAppState;
import com.jme3.renderer.RenderManager;


/**
 *  Resets (clearFrame()) the render's stats object every frame
 *  during AppState.render().  This state is registered once
 *  with Application to ensure that the stats are cleared once
 *  a frame.  Using this makes sure that any Application-based
 *  application that properly runs its state manager will have
 *  stats reset no matter how many views it has or if it even
 *  has views.
 *
 *  @author    Paul Speed
 */
public class ResetStatsState extends AbstractAppState {

    public ResetStatsState() {
    }    

    @Override
    public void render(RenderManager rm) {
        super.render(rm);
        rm.getRenderer().getStatistics().clearFrame();        
    } 

}
