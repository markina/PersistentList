package com.ifmo.markina.persistent.list.impl;

import com.ifmo.markina.persistent.list.IIterator;
import com.ifmo.markina.persistent.list.impl.fast.PersistentList;
import com.ifmo.markina.persistent.list.impl.naive.NaivePersistentList;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Tests {
    private final Random random = new Random(239);

    @Test
    public void addTest() {
        NaivePersistentList<Integer> naivePersistentList = new NaivePersistentList<>();
        PersistentList<Integer> persistentList = new PersistentList<>();

        for (int i = 0; i < 100; i++) {
            add(random.nextInt(i + 1), random.nextInt(1000), naivePersistentList, persistentList);
        }

        assertEqualsList(persistentList, naivePersistentList);
    }

    @Test
    public void setTest() {
        NaivePersistentList<Integer> naivePersistentList = new NaivePersistentList<>();
        PersistentList<Integer> persistentList = new PersistentList<>();

        for (int i = 0; i < 100; i++) {
            add(random.nextInt(i + 1), random.nextInt(1000), naivePersistentList, persistentList);
        }

        for (int i = 0; i < 100; i++) {
            set(random.nextInt(100), random.nextInt(1000), naivePersistentList, persistentList);
        }
        assertEqualsList(persistentList, naivePersistentList);
    }

    @Test
    public void removeTest() {
        NaivePersistentList<Integer> naivePersistentList = new NaivePersistentList<>();
        PersistentList<Integer> persistentList = new PersistentList<>();

        for (int i = 0; i < 10; i++) {
            add(random.nextInt(i + 1), random.nextInt(1000), naivePersistentList, persistentList);
//            if (!naivePersistentList.isEmpty()) {
//                System.out.println(getListByIterator(naivePersistentList.getHeadIterator()));
//                System.out.println(getListByIterator(persistentList.getHeadIterator()));
//            }
        }

        for (int i = 10 - 1; i > 0; i--) {
            remove(random.nextInt(i), naivePersistentList, persistentList);
        }

        assertEqualsList(persistentList, naivePersistentList);
    }

    private void assertEqualsList(PersistentList<Integer> actual, NaivePersistentList<Integer> expected) {
        for (int version = 0; version <= expected.getCurrentVersion(); version++) {
            if (!expected.isEmpty(version)) {
                assertEquals("Invalid list",
                        getListByIterator(expected.getHeadIterator(version)),
                        getListByIterator(actual.getHeadIterator(version)));
            } else {
                assertTrue("List isn't empty", actual.isEmpty(version));
            }
        }
    }

    private void remove(int index,
                        NaivePersistentList<Integer> naivePersistentList,
                        PersistentList<Integer> persistentList) {
        naivePersistentList.remove(index);
        persistentList.remove(index);
    }

    private void set(int index, Integer value,
                     NaivePersistentList<Integer> naivePersistentList,
                     PersistentList<Integer> persistentList) {
        naivePersistentList.set(index, value);
        persistentList.set(index, value);
    }

    private void add(int index, Integer value,
                     NaivePersistentList<Integer> naivePersistentList,
                     PersistentList<Integer> persistentList) {
        naivePersistentList.add(index, value);
        persistentList.add(index, value);
    }


    private List<Integer> getListByIterator(IIterator<Integer> head) { // TODO rewrite to compare iterator
        List<Integer> result = new ArrayList<>();

        if (head != null) { // empty list
            result.add(head.getValue());
            while (head.hasNext()) {
                head.next();
                result.add(head.getValue());
            }
        }
        return result;
    }
}
