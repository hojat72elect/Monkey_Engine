
package com.jme3.app.state;

import java.awt.Component;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.system.AWTFrameProcessor;
import com.jme3.system.AWTTaskExecutor;

/**
 * An app state dedicated to the rendering of a JMonkey application within an AWT component.
 * @author Julien Seinturier - COMEX SA - <a href="http://www.seinturier.fr">http://www.seinturier.fr</a>
 */
public class AWTComponentAppState extends AbstractAppState {

  private final AWTTaskExecutor executor = AWTTaskExecutor.getInstance();
  
  private Component component = null;
  
  private AWTFrameProcessor processor = null;
 
  private AWTFrameProcessor.TransferMode transferMode = AWTFrameProcessor.TransferMode.ON_CHANGES;
  
  @Override
  public void initialize(AppStateManager stateManager, Application app) {
    super.initialize(stateManager, app);
  }
 
  @Override
  public void stateAttached(final AppStateManager stateManager) {
    processor = new AWTFrameProcessor();
    processor.setTransferMode(transferMode);

    AWTTaskExecutor.getInstance().addToExecute(new Runnable() {

      @Override
      public void run() {
        processor.bind(component, stateManager.getApplication(), stateManager.getApplication().getViewPort());
      }
      
    });
  }
  
  @Override
  public void setEnabled(boolean enabled) {
      super.setEnabled(enabled);
  }
  
  @Override
  public void update(float tpf) {
    executor.execute();
    super.update(tpf);
  }
  
  @Override
  public void cleanup() {
    super.cleanup();
  }
  
  /**
   * Create a new app state dedicated to the rendering of a JMonkey application within the given AWT <code>component</code>.
   * @param component the component that is used as rendering output.
   */
  public AWTComponentAppState(Component component) {
    this.component = component;
  }
  
  /**
   * Get the AWT component that is used as rendering output.
   * @return the AWT component that is used as rendering output.
   * @see #setComponent(Component)
   */
  public Component getComponent() {
    return component;
  }
  
  /**
   * Set the AWT component that is used as rendering output.
   * @param component the AWT component that is used as rendering output.
   * @see #getComponent()
   */
  public void setComponent(Component component) {
    this.component = component;
  }
  
  /**
   * Get the {@link com.jme3.system.AWTFrameProcessor.TransferMode transfer mode} that is used by the underlying frame processor.
   * @return the {@link com.jme3.system.AWTFrameProcessor.TransferMode transfer mode} that is used by the underlying frame processor.
   * @see #setTransferMode(com.jme3.system.AWTFrameProcessor.TransferMode)
   */
  public AWTFrameProcessor.TransferMode getTransferMode(){
    return transferMode;
  }
  
  /**
   * Set the {@link com.jme3.system.AWTFrameProcessor.TransferMode transfer mode} that is used by the underlying frame processor.
   * @param mode the {@link com.jme3.system.AWTFrameProcessor.TransferMode transfer mode} that is used by the underlying frame processor.
   * @see #getTransferMode()
   */
  public void setTransferMode(AWTFrameProcessor.TransferMode mode) {
    this.transferMode = mode;
  }
}
