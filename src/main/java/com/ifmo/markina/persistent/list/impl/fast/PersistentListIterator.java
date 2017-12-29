package com.ifmo.markina.persistent.list.impl.fast;

import com.ifmo.markina.persistent.list.IteratorI;

import java.util.NoSuchElementException;

public class PersistentListIterator implements IteratorI {
    private SmallNode it;
    private int version;

    public PersistentListIterator(SmallNode it, int version) {
        this.it = it;
        this.version = version;
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public boolean hasPrev() {
        return it.hasPrev();
    }

    @Override
    public void next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Next element is absent");
        }
        it = it.getNext().getSmallNode(version);
    }

    @Override
    public void prev() {
        if (!hasPrev()) {
            throw new NoSuchElementException("Prev element is absent");
        }
        it = it.getPrev().getSmallNode(version);
    }

    @Override
    public int getValue() {
        if(it == null) {
            throw new NoSuchElementException("Element is absent");
        }
        return it.getValue();
    }
}
