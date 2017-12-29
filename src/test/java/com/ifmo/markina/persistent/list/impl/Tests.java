package com.ifmo.markina.persistent.list.impl;

import com.ifmo.markina.persistent.list.IteratorI;
import com.ifmo.markina.persistent.list.impl.fast.PersistentList;
import com.ifmo.markina.persistent.list.impl.naive.NaivePersistentList;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class Tests {
    private final Random random = new Random(239);

    @Test
    public void addTest() {
        NaivePersistentList naivePersistentList = new NaivePersistentList();
        PersistentList persistentList = new PersistentList();

        for (int i = 0; i < 100; i++) {
            add(random.nextInt(i + 1), random.nextInt(1000), naivePersistentList, persistentList);
        }

        for (int i = 0; i < persistentList.getCurrentVersion() + 1; i++) {
            System.out.println(getListByIterator(naivePersistentList.getHead(i)));
            System.out.println(getListByIterator(persistentList.getHead(i)));
            assertEquals("Invalid list",
                    getListByIterator(naivePersistentList.getHead(i)),
                    getListByIterator(persistentList.getHead(i)));
        }

    }

    @Test
    public void editTest() { // TODO rename ?
        NaivePersistentList naivePersistentList = new NaivePersistentList();
        PersistentList persistentList = new PersistentList();

        for (int i = 0; i < 100; i++) {
            add(random.nextInt(i + 1), random.nextInt(1000), naivePersistentList, persistentList);
        }

        for(int i = 0; i < 100; i++) {
            edit(random.nextInt(100), random.nextInt(1000), naivePersistentList, persistentList);
        }

        for (int i = 0; i < persistentList.getCurrentVersion() + 1; i++) {
            assertEquals("Invalid list",
                    getListByIterator(naivePersistentList.getHead(i)),
                    getListByIterator(persistentList.getHead(i)));
        }

    }

    private void edit(int index, int value,
                      NaivePersistentList naivePersistentList,
                      PersistentList persistentList) {
        naivePersistentList.edit(index, value);
        persistentList.edit(index, value);
    }

    void add(int index, int value,
             NaivePersistentList naivePersistentList,
             PersistentList persistentList) {
        naivePersistentList.add(index, value);
        persistentList.add(index, value);
    }


    List<Integer> getListByIterator(IteratorI head) {
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
