package com.ifmo.markina.persistent.list.impl;

import com.ifmo.markina.persistent.list.IPersistentList;
import com.ifmo.markina.persistent.list.impl.fast.PersistentList;
import com.ifmo.markina.persistent.list.impl.naive.NaivePersistentList;

import java.util.Random;


public class TestSpeed {
    // TODO + md
    // TODO fix O() in md

    private static Random random = new Random(239);

    public static void add(int n, IPersistentList<Integer> list) {
        for (int i = 0; i < n; i++) {
            list.add(random.nextInt(i + 1), i);
        }
    }

    public static void addHead(int n, IPersistentList<Integer> list) {
        for (int i = 0; i < n; i++) {
            list.add(0, i);
        }
    }

    private static void addTail(int n, IPersistentList<Integer> list) {
        for (int i = 0; i < n; i++) {
            list.add(i, i);
        }
    }

    public static void main(String[] args) {
        int n = 10000;

        warmUp();

        randomAdd(n);
        addHead(n);
        addTail(n);
        randomSet(n);
        headSet(n);
        tailSet(n);
        randomRemove(n);
        headRemove(n);
        tailRemove(n);
    }


    private static void randomRemove(int n) {
        System.out.println("Random remove: " + n);

        NaivePersistentList<Integer> naivePersistentList = new NaivePersistentList<>();
        init(10000, naivePersistentList);

        Long start = System.currentTimeMillis();
        randomRemove(n, naivePersistentList);
        Long end = System.currentTimeMillis();

        System.out.println("NaivePersistentList: " + (end - start));

        PersistentList<Integer> persistentList = new PersistentList<>();
        init(10000, persistentList);

        start = System.currentTimeMillis();
        randomRemove(n, persistentList);
        end = System.currentTimeMillis();

        System.out.println("PersistentList: " + (end - start));
    }

    private static void tailRemove(int n) {
        System.out.println("Tail remove: " + n);

        NaivePersistentList<Integer> naivePersistentList = new NaivePersistentList<>();
        init(10000, naivePersistentList);

        Long start = System.currentTimeMillis();
        tailRemove(n, naivePersistentList);
        Long end = System.currentTimeMillis();

        System.out.println("NaivePersistentList: " + (end - start));

        PersistentList<Integer> persistentList = new PersistentList<>();
        init(10000, persistentList);

        start = System.currentTimeMillis();
        tailRemove(n, persistentList);
        end = System.currentTimeMillis();

        System.out.println("PersistentList: " + (end - start));
    }

    private static void headRemove(int n) {
        System.out.println("Head remove: " + n);

        NaivePersistentList<Integer> naivePersistentList = new NaivePersistentList<>();
        init(10000, naivePersistentList);

        Long start = System.currentTimeMillis();
        headRemove(n, naivePersistentList);
        Long end = System.currentTimeMillis();

        System.out.println("NaivePersistentList: " + (end - start));

        PersistentList<Integer> persistentList = new PersistentList<>();
        init(10000, persistentList);

        start = System.currentTimeMillis();
        headRemove(n, persistentList);
        end = System.currentTimeMillis();

        System.out.println("PersistentList: " + (end - start));
    }

    private static void addTail(int n) {
        System.out.println("Add tail: " + n);

        Long start = System.currentTimeMillis();
        addTail(n, new NaivePersistentList<>());
        Long end = System.currentTimeMillis();

        System.out.println("NaivePersistentList: " + (end - start));

        start = System.currentTimeMillis();
        addTail(n, new PersistentList<>());
        end = System.currentTimeMillis();

        System.out.println("PersistentList: " + (end - start));
    }

    private static void addHead(int n) {
        System.out.println("Add head: " + n);

        Long start = System.currentTimeMillis();
        add(n, new NaivePersistentList<>());
        Long end = System.currentTimeMillis();

        System.out.println("NaivePersistentList: " + (end - start));

        start = System.currentTimeMillis();
        add(n, new PersistentList<>());
        end = System.currentTimeMillis();

        System.out.println("PersistentList: " + (end - start));
    }

    private static void randomAdd(int n) {
        System.out.println("Random add: " + n);

        Long start = System.currentTimeMillis();
        add(n, new NaivePersistentList<>());
        Long end = System.currentTimeMillis();

        System.out.println("NaivePersistentList: " + (end - start));

        start = System.currentTimeMillis();
        add(n, new PersistentList<>());
        end = System.currentTimeMillis();

        System.out.println("PersistentList: " + (end - start));
    }

    private static void randomSet(int n) {
        System.out.println("Random set: " + n);

        NaivePersistentList<Integer> naivePersistentList = new NaivePersistentList<>();
        init(10000, naivePersistentList);

        Long start = System.currentTimeMillis();
        randomSet(n, naivePersistentList);
        Long end = System.currentTimeMillis();

        System.out.println("NaivePersistentList: " + (end - start));

        PersistentList<Integer> persistentList = new PersistentList<>();
        init(10000, persistentList);

        start = System.currentTimeMillis();
        randomSet(n, persistentList);
        end = System.currentTimeMillis();

        System.out.println("PersistentList: " + (end - start));
    }

    private static void headSet(int n) {
        System.out.println("Head set: " + n);

        NaivePersistentList<Integer> naivePersistentList = new NaivePersistentList<>();
        init(10000, naivePersistentList);

        Long start = System.currentTimeMillis();
        headSet(n, naivePersistentList);
        Long end = System.currentTimeMillis();

        System.out.println("NaivePersistentList: " + (end - start));

        PersistentList<Integer> persistentList = new PersistentList<>();
        init(10000, persistentList);

        start = System.currentTimeMillis();
        headSet(n, persistentList);
        end = System.currentTimeMillis();

        System.out.println("PersistentList: " + (end - start));
    }

    private static void tailSet(int n) {
        System.out.println("Tail set: " + n);

        NaivePersistentList<Integer> naivePersistentList = new NaivePersistentList<>();
        init(10000, naivePersistentList);

        Long start = System.currentTimeMillis();
        tailSet(n, naivePersistentList);
        Long end = System.currentTimeMillis();

        System.out.println("NaivePersistentList: " + (end - start));

        PersistentList<Integer> persistentList = new PersistentList<>();
        init(10000, persistentList);

        start = System.currentTimeMillis();
        tailSet(n, persistentList);
        end = System.currentTimeMillis();

        System.out.println("PersistentList: " + (end - start));
    }

    private static void warmUp() {
        add(10000, new NaivePersistentList<>());
    }

    private static void randomRemove(int n, IPersistentList<Integer> list) {
        for (int i = n; i > 0; i--) {
            list.remove(random.nextInt(i));
        }
    }

    private static void tailSet(int n, IPersistentList<Integer> list) {
        for (int i = n - 1; i >= 0; i--) {
            list.set(n - 1, i);
        }
    }

    private static void tailRemove(int n, IPersistentList<Integer> list) {
        for (int i = n - 1; i >= 0; i--) {
            list.remove(i);
        }
    }

    private static void headRemove(int n, IPersistentList<Integer> list) {
        for (int i = n; i > 0; i--) {
            list.remove(0);
        }
    }

    private static void headSet(int n, IPersistentList<Integer> list) {
        for (int i = n; i > 0; i--) {
            list.set(0, i);
        }
    }

    private static void randomSet(int n, IPersistentList<Integer> list) {
        for (int i = n; i > 0; i--) {
            list.set(random.nextInt(i), i);
        }
    }

    private static void init(int n, IPersistentList<Integer> list) {
        add(n, list);
    }
}
