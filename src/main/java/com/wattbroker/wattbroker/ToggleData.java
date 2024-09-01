package com.wattbroker.wattbroker;

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
