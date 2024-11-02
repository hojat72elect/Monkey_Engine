

package jme3test.awt;

import com.jme3.app.LegacyApplication;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import com.jme3.system.JmeSystem;
import java.applet.Applet;
import java.awt.Canvas;
import java.awt.Graphics;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.SwingUtilities;

/**
 *
 * @author Kirill
 */
public class AppHarness extends Applet {

    private JmeCanvasContext context;
    private Canvas canvas;
    private LegacyApplication app;

    private String appClass;
    private URL appCfg = null;

    @SuppressWarnings("unchecked")
    private void createCanvas(){
        AppSettings settings = new AppSettings(true);

        // load app cfg
        if (appCfg != null){
            try {
                InputStream in = appCfg.openStream();
                settings.load(in);
                in.close();
            } catch (IOException ex){
                ex.printStackTrace();
            }
        }

        settings.setWidth(getWidth());
        settings.setHeight(getHeight());
        settings.setAudioRenderer(null);

        JmeSystem.setLowPermissions(true);

        try{
            Class clazz = Class.forName(appClass);
            app = (LegacyApplication) clazz.getDeclaredConstructor().newInstance();
        }catch (ClassNotFoundException
                | InstantiationException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException
                | NoSuchMethodException
                | SecurityException ex) {
            ex.printStackTrace();
        }

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
            ex.printStackTrace();
            appCfg = null;
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
    }

}
