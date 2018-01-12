package com.ifmo.markina.persistent.list.impl.naive;

import com.ifmo.markina.persistent.list.IIterator;

import java.util.List;
import java.util.NoSuchElementException;

public class NaiveIterator<E> implements IIterator<E> {
    private int currentIndex;
    private List<E> list;

    NaiveIterator(List<E> list, int index) {
        currentIndex = index;
        this.list = list;
    }


    @Override
    public boolean hasNext() {
        return list.size() > currentIndex + 1;
    }

    @Override
    public boolean hasPrev() {
        return 0 < currentIndex;
    }

    @Override
    public void next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Next element is absent");
        }
        currentIndex++;
    }

    @Override
    public void prev() {
        if (!hasPrev()) {
            throw new NoSuchElementException("Prev element is absent");
        }
        currentIndex--;
    }

    @Override
    public E getValue() {
        if (list.size() <= currentIndex) {
            throw new NoSuchElementException("Element is absent");
        }
        return list.get(currentIndex);
    }
}
