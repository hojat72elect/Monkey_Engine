

package com.jme3.network.service.rmi;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 *  Internal registry of shared types and their ClassInfo and MethodInfo
 *  objects.
 *
 *  @author    Paul Speed
 */
public class ClassInfoRegistry {

    private final Map<Class, ClassInfo> cache = new HashMap<>();
    private final AtomicInteger nextClassId = new AtomicInteger();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
 
    public ClassInfoRegistry() {
        //this.cache = CacheBuilder.newBuilder().build(new ClassInfoLoader());  // Guava version
    }

    public ClassInfo getClassInfo( Class type ) {        
        //return cache.getUnchecked(type); // Guava version
 
        // More complicated without guava
        lock.readLock().lock();
        try {
            ClassInfo result = cache.get(type);
            if( result != null ) {
                return result;
            }
            // Else we need to create it and store it, so grab the write lock.
            lock.readLock().unlock();
            lock.writeLock().lock();
            try {
                // Note: it's technically possible that a race with another thread
                // asking for the same class already created one between our read unlock
                // and our write lock.  No matter as it's cheap to create one and does
                // no harm.  Code is simpler without the double check.
                result = new ClassInfo((short)nextClassId.getAndIncrement(), type);
                cache.put(type, result);
                
                // Re-grab the read lock before leaving... kind of unnecessary, but
                // it makes the method cleaner and widens the gap of lock races.
                // Downgrading a write lock to read is ok.
                lock.readLock().lock();
                
                return result;
            } finally {
                // Unlock the write lock while still holding onto the read lock.
                lock.writeLock().unlock();
            }
        } finally {
            lock.readLock().unlock();
        }
    }
    
    /*
    would be more straight-forward with Guava.  Guava version:
    private class ClassInfoLoader extends CacheLoader<Class, ClassInfo> {
        @Override
        public ClassInfo load( Class type ) {
            return new ClassInfo((short)nextClassId.getAndIncrement(), type);
        }
    }*/ 
}
