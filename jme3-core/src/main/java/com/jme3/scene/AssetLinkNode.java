
package com.jme3.scene;

import com.jme3.asset.AssetManager;
import com.jme3.asset.ModelKey;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.util.SafeArrayList;
import com.jme3.util.clone.Cloner;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The AssetLinkNode does not store its children when exported to file.
 * Instead, you can add a list of AssetKeys that will be loaded and attached
 * when the AssetLinkNode is restored.
 *
 * @author normenhansen
 */
public class AssetLinkNode extends Node {

    protected ArrayList<ModelKey> assetLoaderKeys = new ArrayList<>();
    protected Map<ModelKey, Spatial> assetChildren = new HashMap<>();

    public AssetLinkNode() {
    }

    public AssetLinkNode(ModelKey key) {
        this(key.getName(), key);
    }

    public AssetLinkNode(String name, ModelKey key) {
        super(name);
        assetLoaderKeys.add(key);
    }

    /**
     *  Called internally by com.jme3.util.clone.Cloner.  Do not call directly.
     */
    @Override
    public void cloneFields(Cloner cloner, Object original) {
        super.cloneFields(cloner, original);

        // This is a change in behavior because the old version did not clone
        // this list... changes to one clone would be reflected in all.
        // I think that's probably undesirable. -pspeed
        this.assetLoaderKeys = cloner.clone(assetLoaderKeys);
        this.assetChildren = new HashMap<ModelKey, Spatial>();
    }

    /**
     * Add a "linked" child. These are loaded from the assetManager when the
     * AssetLinkNode is loaded from a binary file.
     *
     * @param key the ModelKey to add
     */
    public void addLinkedChild(ModelKey key) {
        if (assetLoaderKeys.contains(key)) {
            return;
        }
        assetLoaderKeys.add(key);
    }

    public void removeLinkedChild(ModelKey key) {
        assetLoaderKeys.remove(key);
    }

    public ArrayList<ModelKey> getAssetLoaderKeys() {
        return assetLoaderKeys;
    }

    public void attachLinkedChild(AssetManager manager, ModelKey key) {
        addLinkedChild(key);
        Spatial child = manager.loadAsset(key);
        assetChildren.put(key, child);
        attachChild(child);
    }

    public void attachLinkedChild(Spatial spat, ModelKey key) {
        addLinkedChild(key);
        assetChildren.put(key, spat);
        attachChild(spat);
    }

    public void detachLinkedChild(ModelKey key) {
        Spatial spatial = assetChildren.get(key);
        if (spatial != null) {
            detachChild(spatial);
        }
        removeLinkedChild(key);
        assetChildren.remove(key);
    }

    public void detachLinkedChild(Spatial child, ModelKey key) {
        removeLinkedChild(key);
        assetChildren.remove(key);
        detachChild(child);
    }

    /**
     * Loads the linked children AssetKeys from the AssetManager and attaches them to the Node<br>
     * If they are already attached, they will be reloaded.
     *
     * @param manager for loading assets
     */
    public void attachLinkedChildren(AssetManager manager) {
        detachLinkedChildren();
        for (Iterator<ModelKey> it = assetLoaderKeys.iterator(); it.hasNext();) {
            ModelKey assetKey = it.next();
            Spatial curChild = assetChildren.get(assetKey);
            if (curChild != null) {
                curChild.removeFromParent();
            }
            Spatial child = manager.loadAsset(assetKey);
            attachChild(child);
            assetChildren.put(assetKey, child);
        }
    }

    public void detachLinkedChildren() {
        Set<Entry<ModelKey, Spatial>> set = assetChildren.entrySet();
        for (Iterator<Entry<ModelKey, Spatial>> it = set.iterator(); it.hasNext();) {
            Entry<ModelKey, Spatial> entry = it.next();
            entry.getValue().removeFromParent();
            it.remove();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void read(JmeImporter importer) throws IOException {
        super.read(importer);

        final InputCapsule capsule = importer.getCapsule(this);
        final AssetManager assetManager = importer.getAssetManager();

        assetLoaderKeys = capsule.readSavableArrayList("assetLoaderKeyList", new ArrayList<>());

        for (final Iterator<ModelKey> iterator = assetLoaderKeys.iterator(); iterator.hasNext(); ) {

            final ModelKey modelKey = iterator.next();
            final Spatial child = assetManager.loadAsset(modelKey);

            if (child != null) {
                child.parent = this;
                children.add(child);
                assetChildren.put(modelKey, child);
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING,
                        "Cannot locate {0} for asset link node {1}", new Object[]{modelKey, key});
            }
        }
    }

    @Override
    public void write(JmeExporter e) throws IOException {
        SafeArrayList<Spatial> childList = children;
        children = new SafeArrayList<>(Spatial.class);
        super.write(e);
        OutputCapsule capsule = e.getCapsule(this);
        capsule.writeSavableArrayList(assetLoaderKeys, "assetLoaderKeyList", null);
        children = childList;
    }
}
