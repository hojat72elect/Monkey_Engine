
package com.jme3.input.controls;

/**
 *
 * @author potterec (aka iwgeric)
 */
public interface SoftTextDialogInputListener {

    public static int COMPLETE = 0;
    public static int CANCEL = 1;

    public void onSoftText(int action, String text);
}
