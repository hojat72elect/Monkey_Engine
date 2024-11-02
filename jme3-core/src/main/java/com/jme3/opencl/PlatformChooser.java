
package com.jme3.opencl;

import com.jme3.system.AppSettings;
import java.util.List;

/**
 * This SPI is called on startup to specify which platform and which devices
 * are used for context creation.
 * @author shaman
 * @see AppSettings#setOpenCLPlatformChooser(java.lang.Class) 
 */
public interface PlatformChooser {
    
    /**
     * Chooses one or more devices for the opencl context.
     * All returned devices must belong to the same platform.
     * If the returned list is empty, no context will be created.
     * @param platforms the available platforms
     * @return the list of devices
     */
    List<? extends Device> chooseDevices(List<? extends Platform> platforms);
    
}
