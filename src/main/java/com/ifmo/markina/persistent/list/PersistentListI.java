package com.ifmo.markina.persistent.list;

public interface PersistentListI {
    IteratorI get(int index, int version);

    void add(int index, int value);

    void edit(int index, int newValue);

    void remove(int index);

    IteratorI getHead(int version);

    IteratorI getTail(int version);

    boolean isEmpty();

    int getCurrentVersion();
}