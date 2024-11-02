
package com.jme3.post.filters;

import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.export.binary.BinaryExporter;
import org.junit.Assert;
import org.junit.Test;

/**
 * Automated tests for the {@code PosterizationFilter} class.
 *
 * @author sgold
 */
public class PosterizationFilterTest {
    /**
     * Tests serialization and de-serialization of a
     * {@code PosterizationFilter}. This test would've detected JME issue #2167,
     * for instance.
     */
    @Test
    public void testSaveAndLoad() {
        PosterizationFilter filter = new PosterizationFilter();

        // Verify the default parameter values:
        verifyDefaults(filter);

        // Set parameters to new values:
        filter.setEnabled(false);
        filter.setGamma(0.7f);
        filter.setNumColors(4);
        filter.setStrength(0.8f);

        // Create a duplicate filter using serialization:
        AssetManager assetManager = new DesktopAssetManager();
        PosterizationFilter copy
                = BinaryExporter.saveAndLoad(assetManager, filter);

        // Verify the parameter values of the duplicate:
        Assert.assertEquals(0.7f, copy.getGamma(), 0f);
        Assert.assertEquals(4, copy.getNumColors(), 0f);
        Assert.assertEquals(0.8f, copy.getStrength(), 0f);
        Assert.assertFalse(copy.isEnabled());
    }

    /**
     * Verify some default values of a newly instantiated
     * {@code PosterizationFilter}.
     *
     * @param filter (not null, unaffected)
     */
    private void verifyDefaults(PosterizationFilter filter) {
        Assert.assertEquals(0.6f, filter.getGamma(), 0f);
        Assert.assertEquals(8, filter.getNumColors(), 0f);
        Assert.assertEquals(1f, filter.getStrength(), 0f);
        Assert.assertTrue(filter.isEnabled());
    }
}
