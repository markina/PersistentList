package com.ifmo.markina.persistent.list.impl;

import com.ifmo.markina.persistent.list.IIterator;
import com.ifmo.markina.persistent.list.IPersistentList;
import com.ifmo.markina.persistent.list.impl.fast.PersistentList;
import com.ifmo.markina.persistent.list.impl.naive.NaivePersistentList;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class Tests {
    private final Random random = new Random(239);

    @Test
    public void addTest() {
        NaivePersistentList<Integer> naivePersistentList = new NaivePersistentList<>();
        PersistentList<Integer> persistentList = new PersistentList<>();

        init100(naivePersistentList, persistentList);

        assertEqualsList(persistentList, naivePersistentList);
    }

    @Test
    public void setTest() {
        NaivePersistentList<Integer> naivePersistentList = new NaivePersistentList<>();
        PersistentList<Integer> persistentList = new PersistentList<>();

        init100(naivePersistentList, persistentList);

        for (int i = 0; i < 100; i++) {
            set(random.nextInt(100), random.nextInt(1000), naivePersistentList, persistentList);
        }
        assertEqualsList(persistentList, naivePersistentList);
    }

    @Test
    public void removeTest() {
        NaivePersistentList<Integer> naivePersistentList = new NaivePersistentList<>();
        PersistentList<Integer> persistentList = new PersistentList<>();
        init100(naivePersistentList, persistentList);

        for (int i = 100 - 1; i > 0; i--) {
            remove(random.nextInt(i), naivePersistentList, persistentList);
        }

        assertEqualsList(persistentList, naivePersistentList);
    }

    private void init100(NaivePersistentList<Integer> naivePersistentList, PersistentList<Integer> persistentList) {
        for (int i = 0; i < 100; i++) {
            add(random.nextInt(i + 1), random.nextInt(1000), naivePersistentList, persistentList);
        }
    }

    private void assertEqualsList(PersistentList<Integer> actual, NaivePersistentList<Integer> expected) {
        for (int version = 0; version <= expected.getCurrentVersion(); version++) {
            if (!expected.isEmpty(version)) {
                assertEqualsIterator(
                        expected.getHeadIterator(version),
                        actual.getHeadIterator(version));
            } else {
                assertTrue("List isn't empty", actual.isEmpty(version));
            }
        }
    }

    private void assertEqualsIterator(IIterator<Integer> expectedIterator, IIterator<Integer> actualIterator) {
        while (expectedIterator.hasNext()) {
            Integer expected = expectedIterator.getValue();
            Integer actual = actualIterator.getValue();
            assertEquals("Invalid value", expected, actual);
            expectedIterator.next();
            actualIterator.next();
        }
        assertFalse("Iterator has next", actualIterator.hasNext());

        // and last element
        Integer expected = expectedIterator.getValue();
        Integer actual = actualIterator.getValue();
        assertEquals("Invalid value", expected, actual);
    }

    private void print(IPersistentList list, int version) {
        if(list.isEmpty(version)) {
            System.out.println("[]");
            return;
        }
        IIterator iterator = list.getHeadIterator(version);

        System.out.print("[");
        while (iterator.hasNext()) {
            System.out.print(iterator.getValue());
            System.out.print(", ");
            iterator.next();
        }
        System.out.println(iterator.getValue() + "]");
    }

    private void print(IPersistentList list) {
        print(list, list.getCurrentVersion());
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
}
