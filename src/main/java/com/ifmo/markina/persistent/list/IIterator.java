package com.ifmo.markina.persistent.list;

public interface IIterator<E> {
    boolean hasNext();

    boolean hasPrev();

    void next();

    void prev();

    E getValue();
}
