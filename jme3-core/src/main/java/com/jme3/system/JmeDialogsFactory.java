
package com.jme3.system;

public interface JmeDialogsFactory {
    /**
     * Set a function to handle app settings. The default implementation shows a
     * settings dialog if available.
     *
     * @param settings the settings object to edit
     * @param loadFromRegistry if true, copy the settings, otherwise merge them
     * @return true to continue, false to exit the application
     */
    public boolean showSettingsDialog(AppSettings settings, boolean loadFromRegistry);    

    /**
     * Set function to handle errors. The default implementation show a dialog
     * if available.
     *
     * @param message text to be displayed in the dialog
     */
    public void showErrorDialog(String message);  
}
