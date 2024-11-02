
package com.jme3.post.filters;

import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.export.binary.BinaryExporter;
import org.junit.Assert;
import org.junit.Test;

/**
 * Automated tests for the {@code DepthOfFieldFilter} class.
 *
 * @author sgold
 */
public class DepthOfFieldFilterTest {
    /**
     * Tests serialization and de-serialization of a {@code DepthOfFieldFilter}.
     * This test would've detected JME issue #2166, for instance.
     */
    @Test
    public void testSaveAndLoad() {
        DepthOfFieldFilter filter = new DepthOfFieldFilter();

        // Verify the default parameter values:
        verifyDefaults(filter);

        // Set parameters to new values:
        filter.setBlurScale(10.5f);
        filter.setBlurThreshold(0.1f);
        filter.setDebugUnfocus(true);
        filter.setEnabled(false);
        filter.setFocusDistance(66f);
        filter.setFocusRange(15f);

        // Create a duplicate filter using serialization:
        AssetManager assetManager = new DesktopAssetManager();
        DepthOfFieldFilter copy = BinaryExporter.saveAndLoad(assetManager, filter);

        // Verify the parameter values of the copy:
        Assert.assertEquals(10.5f, copy.getBlurScale(), 0f);
        Assert.assertEquals(0.1f, copy.getBlurThreshold(), 0f);
        Assert.assertTrue(copy.getDebugUnfocus());
        Assert.assertEquals(66f, copy.getFocusDistance(), 0f);
        Assert.assertEquals(15f, copy.getFocusRange(), 0f);
        Assert.assertEquals("Depth Of Field", copy.getName());
        Assert.assertFalse(copy.isEnabled());
    }

    /**
     * Verify some default values of a newly instantiated
     * {@code DepthOfFieldFilter}.
     *
     * @param filter (not null, unaffected)
     */
    private void verifyDefaults(DepthOfFieldFilter filter) {
        Assert.assertEquals(1f, filter.getBlurScale(), 0f);
        Assert.assertEquals(0.2f, filter.getBlurThreshold(), 0f);
        Assert.assertFalse(filter.getDebugUnfocus());
        Assert.assertEquals(50f, filter.getFocusDistance(), 0f);
        Assert.assertEquals(10f, filter.getFocusRange(), 0f);
        Assert.assertEquals("Depth Of Field", filter.getName());
        Assert.assertTrue(filter.isEnabled());
    }
}
