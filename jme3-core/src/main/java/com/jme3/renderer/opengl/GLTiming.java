
package com.jme3.renderer.opengl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

public class GLTiming implements InvocationHandler {
    
    private final Object obj;
    private final GLTimingState state;
    
    public GLTiming(Object obj, GLTimingState state) {
        this.obj = obj;
        this.state = state;
    }
    
    public static Object createGLTiming(Object glInterface, GLTimingState state, Class<?> ... glInterfaceClasses) {
        return Proxy.newProxyInstance(glInterface.getClass().getClassLoader(),
                                      glInterfaceClasses, 
                                      new GLTiming(glInterface, state));
    }
    
    private static class CallTimingComparator implements Comparator<Map.Entry<String, Long>> {
        @Override
        public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
            return (int) (o2.getValue() - o1.getValue());
        }
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (methodName.equals("resetStats")) {
            if (state.lastPrintOutTime + 1000000000 <= System.nanoTime() && state.sampleCount > 0) {
                state.timeSpentInGL /= state.sampleCount;
                System.out.println("--- TOTAL TIME SPENT IN GL CALLS: " + (state.timeSpentInGL/1000) + "us");
                
                Map.Entry<String, Long>[] callTimes = new Map.Entry[state.callTiming.size()];
                int i = 0;
                for (Map.Entry<String, Long> callTime : state.callTiming.entrySet()) {
                    callTimes[i++] = callTime;
                }
                Arrays.sort(callTimes, new CallTimingComparator());
                int limit = 10;
                for (Map.Entry<String, Long> callTime : callTimes) {
                    long val = callTime.getValue() / state.sampleCount;
                    String name = callTime.getKey();
                    String pad = "                                     ".substring(0, 30 - name.length());
                    System.out.println("\t" + callTime.getKey() + pad + (val/1000) + "us");
                    if (limit-- == 0) break;
                }
                for (Map.Entry<String, Long> callTime : callTimes) {
                    state.callTiming.put(callTime.getKey(), Long.valueOf(0));
                }
                
                state.sampleCount = 0;
                state.timeSpentInGL = 0;
                state.lastPrintOutTime = System.nanoTime();
            } else {
                state.sampleCount++;
            }
            return null;
        } else {
            Long currentTimeObj = state.callTiming.get(methodName);
            long currentTime = 0;
            if (currentTimeObj != null) currentTime = currentTimeObj;
            
            
            long startTime = System.nanoTime();
            Object result = method.invoke(obj, args);
            long delta = System.nanoTime() - startTime;
            
            currentTime += delta;
            state.timeSpentInGL += delta;
            
            state.callTiming.put(methodName, currentTime);
            
            if (delta > 1000000 && !methodName.equals("glClear")) {
                // More than 1ms
                // Ignore glClear as it cannot be avoided.
                System.out.println("GL call " + methodName + " took " + (delta/1000) + "us to execute!");
            }
            
            return result;
        }
    }
    
}
