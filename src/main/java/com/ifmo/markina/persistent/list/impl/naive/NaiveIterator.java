package com.ifmo.markina.persistent.list.impl.naive;

import com.ifmo.markina.persistent.list.IteratorI;

import java.util.List;
import java.util.NoSuchElementException;

public class NaiveIterator implements IteratorI {
    int currentIndex;
    List<Integer> list;

    public NaiveIterator(List<Integer> list, int index) {
        currentIndex = index;
        this.list = list;
    }


    @Override
    public boolean hasNext() {
        return list.size() > currentIndex + 1;
    }

    @Override
    public boolean hasPrev() {
        return 0 < currentIndex - 1;
    }

    @Override
    public void next() {
        if(!hasNext()) {
            throw new NoSuchElementException("Next element is absent");
        }
        currentIndex++;
    }

    @Override
    public void prev() {
        if(!hasPrev()) {
            throw new NoSuchElementException("Prev element is absent");
        }
        currentIndex--;
    }

    @Override
    public int getValue() {
        if(list.size() <= currentIndex) {
            throw new NoSuchElementException("Element is absent");
        }
        return list.get(currentIndex);
    }
}
