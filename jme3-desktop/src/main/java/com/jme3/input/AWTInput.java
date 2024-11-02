
package com.jme3.input;

import java.awt.Component;
import java.util.Objects;

import com.jme3.app.Application;
import com.jme3.input.Input;
import com.jme3.input.RawInputListener;
import com.jme3.system.AWTContext;
import com.jme3.system.AWTTaskExecutor;

/**
 * The implementation of the {@link Input} dedicated to AWT {@link Component component}.
 * <p>
 * This class is based on the <a href="http://www.oracle.com/technetwork/java/javase/overview/javafx-overview-2158620.html">JavaFX</a> original code provided by Alexander Brui (see <a href="https://github.com/JavaSaBr/JME3-JFX">JME3-FX</a>)
 * </p>
 * @author Julien Seinturier - COMEX SA - <a href="http://www.seinturier.fr">http://www.seinturier.fr</a>
 * @author Alexander Brui (JavaSaBr)
 */
public class AWTInput implements Input {

    protected static final AWTTaskExecutor EXECUTOR = AWTTaskExecutor.getInstance();
  
    /**
     * The context.
     */
    protected final AWTContext context;

    /**
     * The raw listener.
     */
    protected RawInputListener listener;

    /**
     * The input node.
     */
    protected Component component;

    /**
     * The {@link Application JMonkey application} that provide the context.
     */
    protected Application application;
    
    /**
     * Initializes is it.
     */
    protected boolean initialized;

    public AWTInput(final AWTContext context) {
        this.context = context;
    }

    public void bind(final Component component) {
        this.component = component;
        Objects.requireNonNull(this.component, "bound Component cannot be null");
    }

    public void unbind() {
      this.component = null;
    }

    @Override
    public void initialize() {
        if (isInitialized()) return;
        initializeImpl();
        initialized = true;
    }

    protected void initializeImpl() {
    }

    @Override
    public void update() {
        if (!context.isRenderable()) return;
        updateImpl();
    }

    protected void updateImpl() {
    }

    @Override
    public void destroy() {
        unbind();
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public void setInputListener(RawInputListener listener) {
        this.listener = listener;
    }

    @Override
    public long getInputTimeNanos() {
        return System.nanoTime();
    }
}