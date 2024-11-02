
package com.jme3.app;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Statistics;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.util.clone.Cloner;
import com.jme3.util.clone.JmeCloneable;

/**
 * The <code>StatsView</code> provides a heads-up display (HUD) of various
 * statistics of rendering. The data is retrieved every frame from a
 * {@link com.jme3.renderer.Statistics} and then displayed on screen.
 * <p>
 * To use the stats view, you need to retrieve the
 * {@link com.jme3.renderer.Statistics} from the
 * {@link com.jme3.renderer.Renderer} used by the application. Then, attach
 * the <code>StatsView</code> to the scene graph.
 * <pre>
 * Statistics stats = renderer.getStatistics();
 * StatsView statsView = new StatsView("MyStats", assetManager, stats);
 * rootNode.attachChild(statsView);
 * </pre>
 */
public class StatsView extends Node implements Control, JmeCloneable {
    private final BitmapText statText;
    private final Statistics statistics;

    private final String[] statLabels;
    private final int[] statData;

    private boolean enabled = true;

    private final StringBuilder stringBuilder = new StringBuilder();

    public StatsView(String name, AssetManager manager, Statistics stats) {
        super(name);

        setQueueBucket(Bucket.Gui);
        setCullHint(CullHint.Never);

        statistics = stats;
        statistics.setEnabled(enabled);

        statLabels = statistics.getLabels();
        statData = new int[statLabels.length];

        BitmapFont font = manager.loadFont("Interface/Fonts/Console.fnt");
        statText = new BitmapText(font);
        statText.setLocalTranslation(0, statText.getLineHeight() * statLabels.length, 0);
        attachChild(statText);

        addControl(this);
    }

    public float getHeight() {
        return statText.getLineHeight() * statLabels.length;
    }

    @Override
    public void update(float tpf) {
        if (!isEnabled()) {
            return;
        }

        statistics.getData(statData);
        stringBuilder.setLength(0);

        // Need to walk through it backwards, as the first label
        // should appear at the bottom, not the top.
        for (int i = statLabels.length - 1; i >= 0; i--) {
            stringBuilder.append(statLabels[i]).append(" = ").append(statData[i]).append('\n');
        }
        statText.setText(stringBuilder);

        // Moved to ResetStatsState to make sure it is
        // done even if there is no StatsView or the StatsView
        // is disabled.
        //statistics.clearFrame();
    }

    @Deprecated
    @Override
    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException();
    }

    @Override
    public StatsView jmeClone() {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void cloneFields(Cloner cloner, Object original) {
        throw new UnsupportedOperationException("Not yet implemented.");
    }

    @Override
    public void setSpatial(Spatial spatial) {
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        statistics.setEnabled(enabled);
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void render(RenderManager rm, ViewPort vp) {
    }
}
