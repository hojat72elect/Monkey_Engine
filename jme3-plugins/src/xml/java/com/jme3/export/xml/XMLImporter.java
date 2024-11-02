

package com.jme3.export.xml;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 * Part of the jME XML IO system as introduced in the Google Code jmexml project.
 * @author Kai Rabien (hevee) - original author of the code.google.com jmexml project
 * @author Doug Daniels (dougnukem) - adjustments for jME 2.0 and Java 1.5
 */
public class XMLImporter implements JmeImporter {

    private AssetManager assetManager;
    private DOMInputCapsule domIn;
    int formatVersion = 0;
    
    public XMLImporter() {
    }

    @Override
    public int getFormatVersion() {
        return formatVersion;
    }
    
    @Override
    public AssetManager getAssetManager(){
        return assetManager;
    }

    public void setAssetManager(AssetManager assetManager){
        this.assetManager = assetManager;
    }

    @Override
    public Object load(AssetInfo info) throws IOException {
        assetManager = info.getManager();
        InputStream in = info.openStream();
        try {
            return load(in);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
    
    public Savable load(File f) throws IOException {
        FileInputStream fis = null; 
        try {
            fis = new FileInputStream(f);
            Savable sav = load(fis);
            return sav;
        } finally {
            if (fis != null) fis.close();
        }
    }

    public Savable load(InputStream f) throws IOException {
        try {
            domIn = new DOMInputCapsule(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f), this);
            return domIn.readSavable(null, null);
        } catch (SAXException | ParserConfigurationException e) {
            IOException ex = new IOException();
            ex.initCause(e);
            throw ex;
        }
    }

    @Override
    public InputCapsule getCapsule(Savable id) {
        return domIn;
    }

    public static XMLImporter getInstance() {
        return new XMLImporter();
    }
}
