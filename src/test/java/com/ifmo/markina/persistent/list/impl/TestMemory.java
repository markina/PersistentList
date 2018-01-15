package com.ifmo.markina.persistent.list.impl;

import com.ifmo.markina.persistent.list.IPersistentList;
import com.ifmo.markina.persistent.list.impl.fast.PersistentList;
import com.ifmo.markina.persistent.list.impl.naive.NaivePersistentList;
import org.junit.Test;

import java.util.Random;

public class TestMemory {

    //long heapSize = Runtime.getRuntime().totalMemory();
    //long heapFreeSize = Runtime.getRuntime().freeMemory();

    private static Random random = new Random(239);

    @Test
    public void addNaiveTest() {
        long heapSize = Runtime.getRuntime().totalMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        System.out.println("Total: " + heapSize);
        System.out.println("Free: " + heapFreeSize);
        System.out.println("Used: " + (heapSize - heapFreeSize));

        IPersistentList<Integer> list = new NaivePersistentList<>();
        for (int i = 0; i < 10000; i++) {
            list.addLast(random.nextInt(i + 1));
        }

        heapSize = Runtime.getRuntime().totalMemory();
        heapFreeSize = Runtime.getRuntime().freeMemory();
        System.out.println("Free: " + heapFreeSize);
        System.out.println("Used: " + (heapSize - heapFreeSize));

    }

    @Test
    public void addTest() {
        long heapSize = Runtime.getRuntime().totalMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        System.out.println("Total: " + heapSize);
        System.out.println("Free: " + heapFreeSize);
        System.out.println("Used: " + (heapSize - heapFreeSize));

        IPersistentList<Integer> list = new PersistentList<>();
        for (int i = 0; i < 10000; i++) {
            list.addLast(random.nextInt(i + 1));
        }

        heapSize = Runtime.getRuntime().totalMemory();
        heapFreeSize = Runtime.getRuntime().freeMemory();
        System.out.println("Free: " + heapFreeSize);
        System.out.println("Used: " + (heapSize - heapFreeSize));

    }
}
