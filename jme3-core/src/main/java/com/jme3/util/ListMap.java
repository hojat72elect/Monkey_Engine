
package com.jme3.util;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

/**
 * Implementation of a Map that favors iteration speed rather than
 * get/put speed.
 *
 * @author Kirill Vainer
 */
public final class ListMap<K, V> extends AbstractMap<K, V> implements Cloneable, Serializable {

    private static final class ListMapEntry<K, V> implements Map.Entry<K, V>, Cloneable {

        private final K key;
        private V value;

        public ListMapEntry(K key, V value){
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V v) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ListMapEntry<K, V> clone(){
            return new ListMapEntry<K, V>(key, value);
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ListMapEntry<K, V> other = (ListMapEntry<K, V>) obj;
            if (this.key != other.key && (this.key == null || !this.key.equals(other.key))) {
                return false;
            }
            if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            return (this.key != null ? this.key.hashCode() : 0) ^
                   (this.value != null ? this.value.hashCode() : 0);
        }
    }
    
    private final HashMap<K, V> backingMap;
    private ListMapEntry<K, V>[] entries;
    
//    private final ArrayList<ListMapEntry<K,V>> entries;

    @SuppressWarnings("unchecked")
    public ListMap(){
        entries = new ListMapEntry[4];
        backingMap = new HashMap<K, V>(4);
//       entries = new ArrayList<ListMapEntry<K,V>>();
    }

    @SuppressWarnings("unchecked")
    public ListMap(int initialCapacity){
        entries = new ListMapEntry[initialCapacity];
        backingMap = new HashMap<K, V>(initialCapacity);
//        entries = new ArrayList<ListMapEntry<K, V>>(initialCapacity);
    }

    @SuppressWarnings("unchecked")
    public ListMap(Map<? extends K, ? extends V> map){
        entries = new ListMapEntry[map.size()];
        backingMap = new HashMap<K, V>(map.size());
//        entries = new ArrayList<ListMapEntry<K, V>>(map.size());
        putAll(map);
    }

    @Override
    public int size() {
//        return entries.size();
        return backingMap.size();
    }

    public Entry<K, V> getEntry(int index){
//        return entries.get(index);
        return entries[index];
    }

    public V getValue(int index){
//        return entries.get(index).value;
        return entries[index].value;
    }

    public K getKey(int index){
//        return entries.get(index).key;
        return entries[index].key;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    private static boolean keyEq(Object keyA, Object keyB){
        return keyA.hashCode() == keyB.hashCode() ? (keyA == keyB) || keyA.equals(keyB) : false;
    }
//
//    private static boolean valEq(Object a, Object b){
//        return a == null ? (b == null) : a.equals(b);
//    }

    @Override
    public boolean containsKey(Object key) {
        return backingMap.containsKey(key); 
//        if (key == null)
//            throw new IllegalArgumentException();
//
//        for (int i = 0; i < entries.size(); i++){
//            ListMapEntry<K,V> entry = entries.get(i);
//            if (keyEq(entry.key, key))
//                return true;
//        }
//        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return backingMap.containsValue(value); 
//        for (int i = 0; i < entries.size(); i++){
//            if (valEq(entries.get(i).value, value))
//                return true;
//        }
//        return false;
    }

    @Override
    public V get(Object key) {
        return backingMap.get(key); 
//        if (key == null)
//            throw new IllegalArgumentException();
//
//        for (int i = 0; i < entries.size(); i++){
//            ListMapEntry<K,V> entry = entries.get(i);
//            if (keyEq(entry.key, key))
//                return entry.value;
//        }
//        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public V put(K key, V value) {
        if (backingMap.containsKey(key)){
            // set the value on the entry
            int size = size();
            for (int i = 0; i < size; i++){
                ListMapEntry<K, V> entry = entries[i];
                if (keyEq(entry.key, key)){
                    entry.value = value;
                    break;
                }
            }
        }else{
            int size = size();
            // expand list as necessary
            if (size == entries.length){
                ListMapEntry<K, V>[] tmpEntries = entries;
                entries = new ListMapEntry[size * 2];
                System.arraycopy(tmpEntries, 0, entries, 0, size);
            }
            entries[size] = new ListMapEntry<K, V>(key, value);
        }
        return backingMap.put(key, value);
//        if (key == null)
//            throw new IllegalArgumentException();
//
//        // check if entry exists, if yes, overwrite it with new value
//        for (int i = 0; i < entries.size(); i++){
//            ListMapEntry<K,V> entry = entries.get(i);
//            if (keyEq(entry.key, key)){
//                V prevValue = entry.value;
//                entry.value = value;
//                return prevValue;
//            }
//        }
//        
//        // add a new entry
//        entries.add(new ListMapEntry<K, V>(key, value));
//        return null;
    }

    @Override
    public V remove(Object key) {
        V element = backingMap.remove(key);
        if (element != null){
            // find removed element
            int size = size() + 1; // includes removed element
            int removedIndex = -1;
            for (int i = 0; i < size; i++){
                ListMapEntry<K, V> entry = entries[i];
                if (keyEq(entry.key, key)){
                    removedIndex = i;
                    break;
                }
            }
            assert removedIndex >= 0;
            
            size --;
            for (int i = removedIndex; i < size; i++){
                entries[i] = entries[i+1];
            }
        }
        return element;
//        if (key == null)
//            throw new IllegalArgumentException();
//
//        for (int i = 0; i < entries.size(); i++){
//            ListMapEntry<K,V> entry = entries.get(i);
//            if (keyEq(entry.key, key)){
//                return entries.remove(i).value;
//            }
//        }
//        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Entry<? extends K, ? extends V> entry : map.entrySet()){
            put(entry.getKey(), entry.getValue());
        }
        
        
//        if (map instanceof ListMap){
//            ListMap<K, V> listMap = (ListMap<K, V>) map;
//            ArrayList<ListMapEntry<K, V>> otherEntries = listMap.entries;
//            for (int i = 0; i < otherEntries.size(); i++){
//                ListMapEntry<K, V> entry = otherEntries.get(i);
//                put(entry.key, entry.value);
//            }
//        }else{
//            for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()){
//                put(entry.getKey(), entry.getValue());
//            }
//        }
    }

    @Override
    public void clear() {
        backingMap.clear();
//        entries.clear();
    }

    @Override
    public ListMap<K, V> clone(){
        ListMap<K, V> clone = new ListMap<>(size());
        clone.putAll(this);
        return clone;
    }

    @Override
    public Set<K> keySet() {
        return backingMap.keySet();
//        HashSet<K> keys = new HashSet<K>();
//        for (int i = 0; i < entries.size(); i++){
//            ListMapEntry<K,V> entry = entries.get(i);
//            keys.add(entry.key);
//        }
//        return keys;
    }

    @Override
    public Collection<V> values() {
        return backingMap.values();
//        ArrayList<V> values = new ArrayList<V>();
//        for (int i = 0; i < entries.size(); i++){
//            ListMapEntry<K,V> entry = entries.get(i);
//            values.add(entry.value);
//        }
//        return values;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return backingMap.entrySet();
//        HashSet<Entry<K, V>> entrySet = new HashSet<Entry<K, V>>();
//        entrySet.addAll(entries);
//        return entrySet;
    }

}
