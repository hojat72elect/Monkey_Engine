
package com.jme3.renderer.opengl;

import java.util.HashMap;

public class GLTimingState {
    long timeSpentInGL = 0;
    int sampleCount = 0;
    long lastPrintOutTime = 0;
    final HashMap<String, Long> callTiming = new HashMap<>();
}