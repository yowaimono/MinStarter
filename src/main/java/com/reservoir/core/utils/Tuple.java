package com.reservoir.core.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Function;

public class Tuple {
    private final Object[] elements;

    private Tuple(Object... elements) {
        this.elements = Arrays.copyOf(elements, elements.length);
    }

    public static Tuple of(Object... elements) {
        return new Tuple(elements);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(int index) {
        if (index < 0 || index >= elements.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + elements.length);
        }
        return (T) elements[index];
    }

    public int size() {
        return elements.length;
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder("( ");
        Iterator<Object> iter = Arrays.asList(elements).iterator();
        while (iter.hasNext()) {
            res.append(iter.next());
            if (iter.hasNext()) {
                res.append(", ");
            }
        }
        res.append(" )");

        return res.toString();
    }


    public <R> Tuple map(Function<Object, R> function) {
        Object[] result = new Object[elements.length];
        for (int i = 0; i < elements.length; i++) {
            result[i] = function.apply(elements[i]);
        }
        return new Tuple(result);
    }

}