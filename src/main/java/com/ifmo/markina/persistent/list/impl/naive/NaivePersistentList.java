package com.ifmo.markina.persistent.list.impl.naive;

import com.ifmo.markina.persistent.list.IIterator;
import com.ifmo.markina.persistent.list.IPersistentList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class NaivePersistentList<E> implements IPersistentList<E> {
    private List<List<E>> list;
    private int currentVersion;

    public NaivePersistentList() {
        list = new ArrayList<>();
        list.add(new LinkedList<>());
        currentVersion = 0;
    }

    @Override
    public void add(int index, E value) {
        currentVersion++;
        if (list.size() != 0) {
            list.add(new ArrayList<>(list.get(list.size() - 1)));
        } else {
            list.add(new ArrayList<>());
        }

        if (index == list.get(list.size() - 1).size()) {
            list.get(list.size() - 1).add(value);
        } else {
            list.get(list.size() - 1).add(index, value);
        }
    }

    @Override
    public void set(int index, E newValue) {
        currentVersion++;
        if (list.size() != 0) {
            list.add(new ArrayList<>(list.get(list.size() - 1)));
        } else {
            list.add(new ArrayList<>());
        }
        list.get(list.size() - 1).set(index, newValue);
    }

    @Override
    public void remove(int index) {
        currentVersion++;
        if (list.size() != 0) {
            list.add(new ArrayList<>(list.get(list.size() - 1)));
        } else {
            list.add(new ArrayList<>());
        }
        list.get(list.size() - 1).remove(index);
    }

    @Override
    public void addFirst(E value) {
        add(0, value);
    }

    @Override
    public void addLast(E value) {
        add(getCurrentSize(), value);
    }

    @Override
    public void removeFirst() {
        remove(0);
    }

    @Override
    public void removeLast() {
        remove(getCurrentSize() - 1);
    }

    @Override
    public void setFirst(E value) {
        set(0, value);
    }

    @Override
    public void setLast(E value) {
        set(getCurrentSize() - 1, value);
    }

    @Override
    public IIterator<E> getHeadIterator(int version) {
        if (list.get(version).isEmpty()) {
            throw new NoSuchElementException("Element is absent");
        }
        return new NaiveIterator<>(list.get(version), 0);
    }

    @Override
    public IIterator<E> getTailIterator(int version) {
        if (list.get(version).isEmpty()) {
            throw new NoSuchElementException("Element is absent");
        }
        return new NaiveIterator<>(list.get(version), list.get(version).size() - 1);
    }

    @Override
    public IIterator<E> getIterator(int index, int version) {
        if (list.get(version).isEmpty()) {
            throw new NoSuchElementException("Element is absent");
        }
        return new NaiveIterator<>(list.get(version), index);
    }

    @Override
    public boolean isEmpty() {
        return list.get(currentVersion).size() == 0;
    }

    @Override
    public E getFirst(int version) {
        return getHeadIterator(version).getValue();
    }

    @Override
    public E getLast(int version) {
        return getTailIterator(version).getValue();
    }

    @Override
    public E get(int index, int version) {
        return getIterator(index, version).getValue();
    }

    @Override
    public int getCurrentVersion() {
        return currentVersion;
    }

    @Override
    public int getCurrentSize() {
        return list.get(getCurrentVersion()).size();
    }

    @Override
    public boolean isEmpty(int version) {
        return list.get(version).size() == 0;
    }

    @Override
    public int getSize(int version) {
        return list.get(version).size();
    }
}
