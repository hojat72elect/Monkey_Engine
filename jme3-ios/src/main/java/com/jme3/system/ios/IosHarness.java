
package com.jme3.system.ios;

import com.jme3.app.LegacyApplication;
import com.jme3.system.JmeSystem;

/**
 * @author normenhansen
 */
public abstract class IosHarness extends ObjcNativeObject {

    protected LegacyApplication app;

    public IosHarness(long appDelegate) {
        super(appDelegate);
        JmeSystem.setSystemDelegate(new JmeIosSystem());
    }

    public abstract void appPaused();

    public abstract void appReactivated();

    public abstract void appClosed();

    public abstract void appUpdate();

    public abstract void appDraw();
    
    public abstract void appReshape(int width, int height);

}