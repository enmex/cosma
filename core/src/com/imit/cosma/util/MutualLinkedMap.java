package com.imit.cosma.util;

import java.util.Collection;
import java.util.LinkedList;

public class MutualLinkedMap<K, V> {
    private LinkedList<K> keys;
    private LinkedList<V> values;

    public MutualLinkedMap() {
        keys = new LinkedList<>();
        values = new LinkedList<>();
    }

    public V getValue(K k) {
        int index = keys.indexOf(k);

        if(index == -1) {
            return null;
        }

        return values.get(index);
    }

    public K getKey(V v) {
        int index = values.indexOf(v);

        if(index == -1) {
            return null;
        }

        return keys.get(index);
    }

    public void put(K k, V v) {
        keys.add(k);
        values.add(v);
    }

    public void putAll(MutualLinkedMap<K, V> map) {
        keys.addAll(map.keys);
        values.addAll(map.values);
    }

    public void removeKey(K k) {
        int index = keys.indexOf(k);

        if(index != -1) {
            keys.remove(index);
            values.remove(index);
        }
    }

    public void removeValue(V v) {
        int index = values.indexOf(v);

        if(index != -1) {
            keys.remove(index);
            values.remove(index);
        }
    }


    public int size() {
        return keys.size();
    }

    public void clear() {
        keys.clear();
        values.clear();
    }

    public Collection<K> keySet() {
        return keys;
    }

    public Collection<V> values() {
        return values;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for(K k : keys) {
            V v = getValue(k);
            builder.append(k + "-" + v + ", ");
        }

        return builder.append("\n").toString();
    }
}
