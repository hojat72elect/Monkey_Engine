
package com.jme3.app;

import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import com.jme3.system.JmeSystem;
import com.jme3.util.res.Resources;

import java.applet.Applet;
import java.awt.Canvas;
import java.awt.Graphics;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * @author Kirill Vainer
 */
public class AppletHarness extends Applet {

    public static final HashMap<LegacyApplication, Applet> appToApplet
                         = new HashMap<LegacyApplication, Applet>();

    protected JmeCanvasContext context;
    protected Canvas canvas;
    protected LegacyApplication app;

    protected String appClass;
    protected URL appCfg = null;
    protected URL assetCfg = null;

    public static Applet getApplet(Application app){
        return appToApplet.get(app);
    }

    @SuppressWarnings("unchecked")
    private void createCanvas(){
        AppSettings settings = new AppSettings(true);

        // load app cfg
        if (appCfg != null){
            InputStream in = null;
            try {
                in = appCfg.openStream();
                settings.load(in);
                in.close();
            } catch (IOException ex){
                // Called before application has been created ....
                // Display error message through AWT
                JOptionPane.showMessageDialog(this, "An error has occurred while "
                                                  + "loading applet configuration"
                                                  + ex.getMessage(),
                                              "jME3 Applet",
                                              JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                if (in != null)
                    try {
                    in.close();
                } catch (IOException ex) {
                }
            }
        }

        if (assetCfg != null){
            settings.putString("AssetConfigURL", assetCfg.toString());
        }

        settings.setWidth(getWidth());
        settings.setHeight(getHeight());

        JmeSystem.setLowPermissions(true);

        try{
            Class clazz = Class.forName(appClass);
            app = (LegacyApplication) clazz.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException
                | InstantiationException
                | IllegalAccessException
                | NoSuchMethodException
                | IllegalArgumentException
                | InvocationTargetException ex) {
            ex.printStackTrace();
        }

        appToApplet.put(app, this);
        app.setSettings(settings);
        app.createCanvas();

        context = (JmeCanvasContext) app.getContext();
        canvas = context.getCanvas();
        canvas.setSize(getWidth(), getHeight());

        add(canvas);
        app.startCanvas();
    }

    @Override
    public final void update(Graphics g) {
        canvas.setSize(getWidth(), getHeight());
    }

    @Override
    public void init(){
        appClass = getParameter("AppClass");
        if (appClass == null)
            throw new RuntimeException("The required parameter AppClass isn't specified!");

        try {
            appCfg = new URL(getParameter("AppSettingsURL"));
        } catch (MalformedURLException ex) {
            System.out.println(ex.getMessage());
            appCfg = null;
        }

        try {
            assetCfg = new URL(getParameter("AssetConfigURL"));
        } catch (MalformedURLException ex){
            System.out.println(ex.getMessage());
            assetCfg = Resources.getResource("/com/jme3/asset/Desktop.cfg",this.getClass());
        }

        createCanvas();
        System.out.println("applet:init");
    }

    @Override
    public void start(){
        context.setAutoFlushFrames(true);
        System.out.println("applet:start");
    }

    @Override
    public void stop(){
        context.setAutoFlushFrames(false);
        System.out.println("applet:stop");
    }

    @Override
    public void destroy(){
        System.out.println("applet:destroyStart");
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){
                removeAll();
                System.out.println("applet:destroyRemoved");
            }
        });
        app.stop(true);
        System.out.println("applet:destroyDone");

        appToApplet.remove(app);
    }

}
