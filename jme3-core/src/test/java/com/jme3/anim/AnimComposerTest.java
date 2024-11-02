
package com.jme3.anim;

import com.jme3.anim.tween.action.Action;
import java.util.Set;
import java.util.TreeSet;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Remy Van Doosselaer
 */
public class AnimComposerTest {

    @Test
    public void testGetAnimClips() {
        AnimComposer composer = new AnimComposer();

        Assert.assertNotNull(composer.getAnimClips());
        Assert.assertEquals(0, composer.getAnimClips().size());
    }

    @Test
    public void testGetAnimClipsNames() {
        AnimComposer composer = new AnimComposer();

        Assert.assertNotNull(composer.getAnimClipsNames());
        Assert.assertEquals(0, composer.getAnimClipsNames().size());
    }
    
    @Test
    public void testMakeLayer() {
        AnimComposer composer = new AnimComposer();
        
        final String layerName = "TestLayer";

        composer.makeLayer(layerName, null);
        
        final Set<String> layers = new TreeSet<>();
        layers.add("Default");
        layers.add(layerName);
        
        Assert.assertNotNull(composer.getLayer(layerName));
        Assert.assertEquals(layers, composer.getLayerNames());
    }
    
    @Test
    public void testMakeAction() {
        AnimComposer composer = new AnimComposer();
        
        final String animName = "TestClip";
        
        final AnimClip anim = new AnimClip(animName);
        composer.addAnimClip(anim);
        
        final Action action = composer.makeAction(animName);
        
        Assert.assertNotNull(action);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAnimClipsIsNotModifiable() {
        AnimComposer composer = new AnimComposer();

        composer.getAnimClips().add(new AnimClip("test"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetAnimClipsNamesIsNotModifiable() {
        AnimComposer composer = new AnimComposer();

        composer.getAnimClipsNames().add("test");
    }

}
