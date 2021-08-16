package com.github.strider_by.util;

public class Pair<T, V>  {
    
    T value0;
    V value1;

    public Pair() {
        
    }

    public Pair(T item1, V item2) {
        this.value0 = item1;
        this.value1 = item2;
    }

    public T getValue0() {
        return value0;
    }

    public V getValue1() {
        return value1;
    }

    public void setValue0(T value0) {
        this.value0 = value0;
    }

    public void setValue1(V value1) {
        this.value1 = value1;
    }
   
}
