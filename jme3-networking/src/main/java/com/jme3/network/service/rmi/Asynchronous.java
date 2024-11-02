

package com.jme3.network.service.rmi;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;


/**
 *  Indicates that a given method should be executed asynchronously
 *  through the RMI service.  This must annotate the method on the
 *  shared interface for it to have an effect.  If reliable=false
 *  is specified then remote method invocation is done over UDP
 *  instead of TCP, ie: unreliably... but faster.
 *
 *  @author    Paul Speed
 */
@Retention(value=RUNTIME)
@Target(value=METHOD)
public @interface Asynchronous {
    boolean reliable() default true;
}


