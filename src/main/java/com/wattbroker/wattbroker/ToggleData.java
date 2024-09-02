package com.wattbroker.wattbroker;

/**
 * This class sores toggleable data in a way that
 * allows for several different classes to be the
 * sets of data.
 * @see toggleable Interface*/
public class ToggleData<T, T1> {
    T t;
    T1 t1;

    public ToggleData(T t, T1 t1) {
        this.t = t;
        this.t1 = t1;
    }

    public T getName() {
        return t;
    }

    public T1 getValue() {
        return t1;
    }
}
