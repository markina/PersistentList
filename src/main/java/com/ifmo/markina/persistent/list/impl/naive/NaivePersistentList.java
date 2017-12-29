package com.ifmo.markina.persistent.list.impl.naive;

import com.ifmo.markina.persistent.list.IteratorI;
import com.ifmo.markina.persistent.list.PersistentListI;

import java.util.ArrayList;
import java.util.List;

public class NaivePersistentList implements PersistentListI {
    List<List<Integer>> list;
    int currentVersion;


    public NaivePersistentList() {
        this.list = new ArrayList<>();
        currentVersion = 0;
    }

    @Override
    public IteratorI get(int index, int version) {
        if (list.size() < version + 1) {
            throw new IndexOutOfBoundsException("index out of bounds");
        }
        return new NaiveIterator(list.get(version), index);
    }

    @Override
    public void add(int index, int value) {
        currentVersion++;
        if (list.size() != 0) {
            list.add(new ArrayList<>(list.get(list.size() - 1)));
        } else {
            list.add(new ArrayList<>());
        }

        list.get(list.size() - 1).add(index, value);
    }

    @Override
    public void edit(int index, int newValue) {
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
    public IteratorI getHead(int version) {
        return new NaiveIterator(list.get(version), 0);
    }

    @Override
    public IteratorI getTail(int version) {
        return new NaiveIterator(list.get(version), list.get(version).size() - 1);
    }

    @Override
    public int getCurrentVersion() {
        return currentVersion;
    }
}
