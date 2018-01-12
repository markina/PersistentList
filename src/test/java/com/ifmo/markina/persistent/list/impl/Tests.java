package com.ifmo.markina.persistent.list.impl;

import com.ifmo.markina.persistent.list.IIterator;
import com.ifmo.markina.persistent.list.IPersistentList;
import com.ifmo.markina.persistent.list.impl.fast.PersistentList;
import com.ifmo.markina.persistent.list.impl.naive.NaivePersistentList;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class Tests {
    private final Random random = new Random(239);
    private NaivePersistentList<Integer> expected;
    private PersistentList<Integer> actual;

    @Test
    public void addTest() {
        init(0);

        for (int i = 0; i < 100; i++) {
            addBoth(random.nextInt(i + 1), random.nextInt(1000));
        }

        assertEqualsList();
    }

    @Test
    public void setTest() {
        init(100);

        for (int i = 0; i < 100; i++) {
            setBoth(random.nextInt(100), random.nextInt(1000));
        }
        assertEqualsList();
    }

    @Test
    public void removeTest() {
        init(100);

        for (int i = 100 - 1; i > 0; i--) {
            removeBoth(random.nextInt(i));
        }

        assertEqualsList();
    }

    @Test
    public void getFirstTest() {
        init(100);

        for (int version = 1; version < expected.getCurrentVersion(); version++) {
            assertEquals("Invalid first", expected.getFirst(version), actual.getFirst(version));
        }
    }

//    @Test // TODO update junit
//    public void getFirstFailTest() {
//        init(0);
//
//        assertThrows(actual.getFirst(), NoSuchElementException.class);
//        assertThrows(expected.getFirst(), NoSuchElementException.class);
//
//        addBoth(0, 1);
//        removeBoth(0);
//
//        assertThrows(actual.getFirst(), NoSuchElementException.class);
//        assertThrows(expected.getFirst(), NoSuchElementException.class);
//    }

    @Test
    public void getLastTest() {
        init(100);

        for (int version = 1; version < expected.getCurrentVersion(); version++) {
            assertEquals("Invalid first", expected.getLast(version), actual.getLast(version));
        }
    }

//    @Test  // TODO update junit
//    public void getLastFailTest() {
//        init(0);
//
//        assertThrows(actual.getLast(), NoSuchElementException.class);
//        assertThrows(expected.getLast(), NoSuchElementException.class);
//
//        addBoth(0, 1);
//        removeBoth(0);
//
//        assertThrows(actual.getLast(), NoSuchElementException.class);
//        assertThrows(expected.getLast(), NoSuchElementException.class);
//    }


    @Test
    public void getHeadIterator() throws Exception {
        init(100);
        for (int version = 0; version < expected.getCurrentVersion(); version++) {
            if (!expected.isEmpty(version)) {
                assertEqualsHeadIterator(expected.getHeadIterator(version), actual.getHeadIterator(version));
            }
        }
    }

    @Test
    public void getTailIterator() throws Exception {
        init(100);
        for (int version = 0; version < expected.getCurrentVersion(); version++) {
            if (!expected.isEmpty(version)) {
                assertEqualsTailIterator(expected.getTailIterator(version), actual.getTailIterator(version));
            }
        }
    }

    @Test
    public void getIterator() throws Exception {
        init(100);
        for (int version = 0; version < expected.getCurrentVersion(); version++) {
            if (!expected.isEmpty(version)) {
                int size = expected.getSize(version);
                int index = random.nextInt(size);
                assertEqualsTailIterator(expected.getIterator(index), actual.getIterator(index));
                int index2 = random.nextInt(size);
                assertEqualsHeadIterator(expected.getIterator(index2), actual.getIterator(index2));
            }
        }
    }

    @Test
    public void isEmptyTest() {
        init(0);

        assertEquals("One of list isn't empty", expected.isEmpty(), actual.isEmpty());

        addBoth(0, 1);

        assertEquals("One of list isn't empty", expected.isEmpty(), actual.isEmpty());

        removeBoth(0);

        assertEquals("One of list isn't empty", expected.isEmpty(), actual.isEmpty());
    }

    @Test
    public void getTest() {
        init(100);

        for (int version = 0; version < expected.getCurrentVersion(); version++) {
            int size = expected.getSize(version);
            for (int i = 0; i < size; i++) {
                assertEquals("Invalid value", expected.get(i), actual.get(i));
            }
        }
    }

    @Test
    public void getSizeTest() {
        init(100);

        for (int version = 0; version < expected.getCurrentVersion(); version++) {
            assertEquals("Invalid size", expected.getSize(version), actual.getSize(version));
        }
    }

    @Test
    public void getCurrentVersionTest() {
        init(0);

        for (int i = 0; i < 100; i++) {
            addBoth(random.nextInt(i + 1), random.nextInt(1000));
            assertEquals("Invalid size", expected.getCurrentVersion(), actual.getCurrentVersion());
        }
    }

    @Test
    public void getCurrentSizeTest() {
        init(0);

        for (int i = 0; i < 100; i++) {
            addBoth(random.nextInt(i + 1), random.nextInt(1000));
            assertEquals("Invalid size", expected.getCurrentSize(), actual.getCurrentSize());
        }
    }

    private void init(int size) {
        expected = new NaivePersistentList<>();
        actual = new PersistentList<>();

        for (int i = 0; i < size; i++) {
            addBoth(random.nextInt(i + 1), random.nextInt(1000));
        }
    }

    private void assertEqualsList() {
        for (int version = 0; version <= expected.getCurrentVersion(); version++) {
            if (!expected.isEmpty(version)) {
                assertEqualsHeadIterator(
                        expected.getHeadIterator(version),
                        actual.getHeadIterator(version));
            } else {
                assertTrue("List isn't empty", actual.isEmpty(version));
            }
        }
    }

    private void assertEqualsHeadIterator(IIterator<Integer> expectedHeadIterator,
                                          IIterator<Integer> actualHeadIterator) {
        while (expectedHeadIterator.hasNext()) {
            Integer expected = expectedHeadIterator.getValue();
            Integer actual = actualHeadIterator.getValue();
            assertEquals("Invalid value", expected, actual);
            expectedHeadIterator.next();
            actualHeadIterator.next();
        }
        assertFalse("Iterator has next", actualHeadIterator.hasNext());

        // and last element
        Integer expected = expectedHeadIterator.getValue();
        Integer actual = actualHeadIterator.getValue();
        assertEquals("Invalid value", expected, actual);
    }

    private void assertEqualsTailIterator(IIterator<Integer> expectedTailIterator,
                                          IIterator<Integer> actualTailIterator) {
        while (expectedTailIterator.hasPrev()) {
            Integer expected = expectedTailIterator.getValue();
            Integer actual = actualTailIterator.getValue();
            assertEquals("Invalid value", expected, actual);
            expectedTailIterator.prev();
            actualTailIterator.prev();
        }
        assertFalse("Iterator has prev element", actualTailIterator.hasPrev());

        // and first element
        Integer expected = expectedTailIterator.getValue();
        Integer actual = actualTailIterator.getValue();
        assertEquals("Invalid value", expected, actual);
    }

    private void print(IPersistentList list, int version) {
        if (list.isEmpty(version)) {
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

    private void printAll() {
        for (int version = 0; version < expected.getCurrentVersion(); version++) {
            print(expected, version);
            print(actual, version);
        }
    }

    private void removeBoth(int index) {
        expected.remove(index);
        actual.remove(index);
    }

    private void setBoth(int index, Integer value) {
        expected.set(index, value);
        actual.set(index, value);
    }

    private void addBoth(int index, Integer value) {
        expected.add(index, value);
        actual.add(index, value);
    }
}
