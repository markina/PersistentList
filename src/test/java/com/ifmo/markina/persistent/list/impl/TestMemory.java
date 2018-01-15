package com.ifmo.markina.persistent.list.impl;

import com.ifmo.markina.persistent.list.IPersistentList;
import com.ifmo.markina.persistent.list.impl.fast.PersistentList;
import com.ifmo.markina.persistent.list.impl.naive.NaivePersistentList;
import org.junit.Test;

import java.util.Random;

public class TestMemory {

    private int n = 5000;

    private static Random random = new Random(239);

    @Test
    public void addLastNaiveTest() {
        System.out.println("Add last");
        long heapSize = Runtime.getRuntime().totalMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        long headUsed = heapSize - heapFreeSize;

        IPersistentList<Integer> list = new NaivePersistentList<>();
        for (int i = 0; i < n; i++) {
            list.addLast(random.nextInt(i + 1));
        }

        long heapSizeAfter = Runtime.getRuntime().totalMemory();
        long heapFreeSizeAfter = Runtime.getRuntime().freeMemory();
        long headUsedAfter = heapSizeAfter - heapFreeSizeAfter;

        System.out.println("NaivePersistentList: " + (headUsedAfter - headUsed) / 1048576);
    }

    @Test
    public void addLastTest() {
        System.out.println("Add last");
        long heapSize = Runtime.getRuntime().totalMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        long headUsed = heapSize - heapFreeSize;

        IPersistentList<Integer> list = new PersistentList<>();
        for (int i = 0; i < n; i++) {
            list.addLast(random.nextInt(i + 1));
        }

        long heapSizeAfter = Runtime.getRuntime().totalMemory();
        long heapFreeSizeAfter = Runtime.getRuntime().freeMemory();
        long headUsedAfter = heapSizeAfter - heapFreeSizeAfter;

        System.out.println("PersistentList: " + (headUsedAfter - headUsed) / 1048576);
    }

    @Test
    public void addFirstNaiveTest() {
        System.out.println("Add first");
        long heapSize = Runtime.getRuntime().totalMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        long headUsed = heapSize - heapFreeSize;

        IPersistentList<Integer> list = new NaivePersistentList<>();
        for (int i = 0; i < n; i++) {
            list.addFirst(random.nextInt(i + 1));
        }

        long heapSizeAfter = Runtime.getRuntime().totalMemory();
        long heapFreeSizeAfter = Runtime.getRuntime().freeMemory();
        long headUsedAfter = heapSizeAfter - heapFreeSizeAfter;

        System.out.println("NaivePersistentList: " + (headUsedAfter - headUsed) / 1048576);
    }

    @Test
    public void addFirstTest() {
        System.out.println("Add first");
        long heapSize = Runtime.getRuntime().totalMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        long headUsed = heapSize - heapFreeSize;

        IPersistentList<Integer> list = new PersistentList<>();
        for (int i = 0; i < n; i++) {
            list.addFirst(random.nextInt(i + 1));
        }

        long heapSizeAfter = Runtime.getRuntime().totalMemory();
        long heapFreeSizeAfter = Runtime.getRuntime().freeMemory();
        long headUsedAfter = heapSizeAfter - heapFreeSizeAfter;

        System.out.println("PersistentList: " + (headUsedAfter - headUsed) / 1048576);
    }

    @Test
    public void addNaiveTest() {
        System.out.println("Random add");
        long heapSize = Runtime.getRuntime().totalMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        long headUsed = heapSize - heapFreeSize;

        IPersistentList<Integer> list = new NaivePersistentList<>();
        for (int i = 0; i < n; i++) {
            list.add(random.nextInt(i + 1), i);
        }

        long heapSizeAfter = Runtime.getRuntime().totalMemory();
        long heapFreeSizeAfter = Runtime.getRuntime().freeMemory();
        long headUsedAfter = heapSizeAfter - heapFreeSizeAfter;

        System.out.println("NaivePersistentList: " + (headUsedAfter - headUsed) / 1048576);
    }

    @Test
    public void addTest() {
        System.out.println("Random add");
        long heapSize = Runtime.getRuntime().totalMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        long headUsed = heapSize - heapFreeSize;

        IPersistentList<Integer> list = new PersistentList<>();
        for (int i = 0; i < n; i++) {
            list.add(random.nextInt(i + 1), i);
        }

        long heapSizeAfter = Runtime.getRuntime().totalMemory();
        long heapFreeSizeAfter = Runtime.getRuntime().freeMemory();
        long headUsedAfter = heapSizeAfter - heapFreeSizeAfter;

        System.out.println("PersistentList: " + (headUsedAfter - headUsed) / 1048576);
    }

    @Test
    public void setLastNaiveTest() {
        System.out.println("Set last");

        IPersistentList<Integer> list = new NaivePersistentList<>();
        init(n, list);

        long heapSize = Runtime.getRuntime().totalMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        long headUsed = heapSize - heapFreeSize;

        for (int i = 0; i < n; i++) {
            list.setLast(random.nextInt(i + 1));
        }

        long heapSizeAfter = Runtime.getRuntime().totalMemory();
        long heapFreeSizeAfter = Runtime.getRuntime().freeMemory();
        long headUsedAfter = heapSizeAfter - heapFreeSizeAfter;

        System.out.println("NaivePersistentList: " + (headUsedAfter - headUsed) / 1048576);
    }

    @Test
    public void setLastTest() {
        System.out.println("Set last");

        IPersistentList<Integer> list = new PersistentList<>();
        init(n, list);

        long heapSize = Runtime.getRuntime().totalMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        long headUsed = heapSize - heapFreeSize;

        for (int i = 0; i < n; i++) {
            list.setLast(random.nextInt(i + 1));
        }

        long heapSizeAfter = Runtime.getRuntime().totalMemory();
        long heapFreeSizeAfter = Runtime.getRuntime().freeMemory();
        long headUsedAfter = heapSizeAfter - heapFreeSizeAfter;

        System.out.println("PersistentList: " + (headUsedAfter - headUsed) / 1048576);
    }

    @Test
    public void setFirstTest() {
        System.out.println("Set first");

        IPersistentList<Integer> list = new PersistentList<>();
        init(n, list);

        long heapSize = Runtime.getRuntime().totalMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        long headUsed = heapSize - heapFreeSize;

        for (int i = 0; i < n; i++) {
            list.setFirst(random.nextInt(i + 1));
        }

        long heapSizeAfter = Runtime.getRuntime().totalMemory();
        long heapFreeSizeAfter = Runtime.getRuntime().freeMemory();
        long headUsedAfter = heapSizeAfter - heapFreeSizeAfter;

        System.out.println("PersistentList: " + (headUsedAfter - headUsed) / 1048576);
    }

    @Test
    public void setFirstNaiveTest() {
        System.out.println("Set first");

        IPersistentList<Integer> list = new NaivePersistentList<>();
        init(n, list);

        long heapSize = Runtime.getRuntime().totalMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        long headUsed = heapSize - heapFreeSize;

        for (int i = 0; i < n; i++) {
            list.setFirst(random.nextInt(i + 1));
        }

        long heapSizeAfter = Runtime.getRuntime().totalMemory();
        long heapFreeSizeAfter = Runtime.getRuntime().freeMemory();
        long headUsedAfter = heapSizeAfter - heapFreeSizeAfter;

        System.out.println("NaivePersistentList: " + (headUsedAfter - headUsed) / 1048576);
    }

    @Test
    public void randomSetTest() {
        System.out.println("Random set");

        IPersistentList<Integer> list = new PersistentList<>();
        init(n, list);

        long heapSize = Runtime.getRuntime().totalMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        long headUsed = heapSize - heapFreeSize;

        for (int i = 0; i < n; i++) {
            list.set(random.nextInt(i + 1), random.nextInt(i + 1));
        }

        long heapSizeAfter = Runtime.getRuntime().totalMemory();
        long heapFreeSizeAfter = Runtime.getRuntime().freeMemory();
        long headUsedAfter = heapSizeAfter - heapFreeSizeAfter;

        System.out.println("PersistentList: " + (headUsedAfter - headUsed) / 1048576);
    }

    @Test
    public void randomSetNaiveTest() {
        System.out.println("Random Set");

        IPersistentList<Integer> list = new NaivePersistentList<>();
        init(n, list);

        long heapSize = Runtime.getRuntime().totalMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        long headUsed = heapSize - heapFreeSize;

        for (int i = 0; i < n; i++) {
            list.set(random.nextInt(i + 1), random.nextInt(i + 1));
        }

        long heapSizeAfter = Runtime.getRuntime().totalMemory();
        long heapFreeSizeAfter = Runtime.getRuntime().freeMemory();
        long headUsedAfter = heapSizeAfter - heapFreeSizeAfter;

        System.out.println("NaivePersistentList: " + (headUsedAfter - headUsed) / 1048576);
    }

    @Test
    public void randomRemoveNaiveTest() {
        System.out.println("Random remove");

        IPersistentList<Integer> list = new NaivePersistentList<>();
        init(n, list);

        long heapSize = Runtime.getRuntime().totalMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        long headUsed = heapSize - heapFreeSize;

        for (int i = n; i > 0; i--) {
            list.remove(random.nextInt(i));
        }

        long heapSizeAfter = Runtime.getRuntime().totalMemory();
        long heapFreeSizeAfter = Runtime.getRuntime().freeMemory();
        long headUsedAfter = heapSizeAfter - heapFreeSizeAfter;

        System.out.println("NaivePersistentList: " + (headUsedAfter - headUsed) / 1048576);
    }

    @Test
    public void randomRemoveTest() {
        System.out.println("Random remove");

        IPersistentList<Integer> list = new PersistentList<>();
        init(n, list);

        long heapSize = Runtime.getRuntime().totalMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        long headUsed = heapSize - heapFreeSize;

        for (int i = n; i > 0; i--) {
            list.remove(random.nextInt(i));
        }

        long heapSizeAfter = Runtime.getRuntime().totalMemory();
        long heapFreeSizeAfter = Runtime.getRuntime().freeMemory();
        long headUsedAfter = heapSizeAfter - heapFreeSizeAfter;

        System.out.println("PersistentList: " + (headUsedAfter - headUsed) / 1048576);
    }

    @Test
    public void removeFirstTest() {
        System.out.println("Random remove");

        IPersistentList<Integer> list = new PersistentList<>();
        init(n, list);

        long heapSize = Runtime.getRuntime().totalMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        long headUsed = heapSize - heapFreeSize;

        for (int i = n; i > 0; i--) {
            list.removeFirst();
        }

        long heapSizeAfter = Runtime.getRuntime().totalMemory();
        long heapFreeSizeAfter = Runtime.getRuntime().freeMemory();
        long headUsedAfter = heapSizeAfter - heapFreeSizeAfter;

        System.out.println("PersistentList: " + (headUsedAfter - headUsed) / 1048576);
    }

    @Test
    public void removeLastTest() {
        System.out.println("Random last");

        IPersistentList<Integer> list = new PersistentList<>();
        init(n, list);

        long heapSize = Runtime.getRuntime().totalMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        long headUsed = heapSize - heapFreeSize;

        for (int i = n; i > 0; i--) {
            list.removeLast();
        }

        long heapSizeAfter = Runtime.getRuntime().totalMemory();
        long heapFreeSizeAfter = Runtime.getRuntime().freeMemory();
        long headUsedAfter = heapSizeAfter - heapFreeSizeAfter;

        System.out.println("PersistentList: " + (headUsedAfter - headUsed) / 1048576);
    }

    @Test
    public void removeLastNaiveTest() {
        System.out.println("Remove last");

        IPersistentList<Integer> list = new NaivePersistentList<>();
        init(n, list);

        long heapSize = Runtime.getRuntime().totalMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        long headUsed = heapSize - heapFreeSize;

        for (int i = n; i > 0; i--) {
            list.removeLast();
        }

        long heapSizeAfter = Runtime.getRuntime().totalMemory();
        long heapFreeSizeAfter = Runtime.getRuntime().freeMemory();
        long headUsedAfter = heapSizeAfter - heapFreeSizeAfter;

        System.out.println("NaivePersistentList: " + (headUsedAfter - headUsed) / 1048576);
    }

    @Test
    public void removeFistNaiveTest() {
        System.out.println("Random remove");

        IPersistentList<Integer> list = new NaivePersistentList<>();
        init(n, list);

        long heapSize = Runtime.getRuntime().totalMemory();
        long heapFreeSize = Runtime.getRuntime().freeMemory();
        long headUsed = heapSize - heapFreeSize;

        for (int i = n; i > 0; i--) {
            list.removeFirst();
        }

        long heapSizeAfter = Runtime.getRuntime().totalMemory();
        long heapFreeSizeAfter = Runtime.getRuntime().freeMemory();
        long headUsedAfter = heapSizeAfter - heapFreeSizeAfter;

        System.out.println("NaivePersistentList: " + (headUsedAfter - headUsed) / 1048576);
    }

    private static void init(int n, IPersistentList<Integer> list) {
        for (int i = 0; i < n; i++) {
            list.addFirst(random.nextInt(i + 1));
        }
    }
}
