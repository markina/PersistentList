package com.ifmo.markina.persistent.list;

public interface IteratorI {
    boolean hasNext();

    boolean hasPrev();

    void next();

    void prev();

    int getValue();
}
