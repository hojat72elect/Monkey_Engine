
package com.jme3.system;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>
 * This class is dedicated to the queuing of AWT related tasks and their execution.
 * It's able to store tasks that have to be executed within an AWT context and execute them at the specified time.
 * </p>
 * <p>
 * This class is an AWT implementation of the <a href="http://www.oracle.com/technetwork/java/javase/overview/javafx-overview-2158620.html">JavaFX</a> original code provided by Alexander Brui (see <a href="https://github.com/JavaSaBr/JME3-JFX">JME3-FX</a>)
 * </p>
 * @author Julien Seinturier - COMEX SA - <a href="http://www.seinturier.fr">http://www.seinturier.fr</a>
 * @author Alexander Brui (JavaSaBr)
 */
public class AWTTaskExecutor {
  
  private final ReadWriteLock  lock = new ReentrantReadWriteLock();
  
  private static final AWTTaskExecutor INSTANCE = new AWTTaskExecutor();

  /**
   * Get the instance of the executor.
   * @return the instance of executor.
   */
  public static AWTTaskExecutor getInstance() {
      return INSTANCE;
  }

  /**
   * The list of waiting tasks.
   */
  private final List<Runnable> waitTasks;

  private AWTTaskExecutor() {
    waitTasks = new LinkedList<Runnable>();
  }

  public List<Runnable> getWaitingTasks(){
    return waitTasks;
  }
  
  /**
   * Add the given {@link Runnable runnable} to the list of planned executions. 
   * @param task the task to add.
   * @see #execute()
   */
  public void addToExecute(final Runnable task) {
    lock.writeLock().lock();
    try {
      waitTasks.add(task);
    } catch (Exception e) {
      // This try catch block enable to free the lock in case of any unexpected error.
    }
    lock.writeLock().unlock();
  }

  /**
   * Execute all the tasks that are waiting.
   * @see #addToExecute(Runnable)
   */
  public void execute() {

      if (waitTasks.isEmpty()) return;

      lock.readLock().lock();
      
      try {
        for(Runnable runnable : waitTasks) {
          runnable.run();
        }
      } catch (Exception e) {
        // This try catch block enable to free the lock in case of any unexpected error.
      }
      
      waitTasks.clear();
      
      lock.readLock().unlock();
  }
}
