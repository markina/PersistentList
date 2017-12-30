package com.ifmo.markina.persistent.list.impl.fast;

public class Node {
    private FatNode next;
    private FatNode prev;
    private FatNode bigBrother;

    private int version;
    private int value;

    Node(int value, int version, FatNode bigBrother) {
        this.value = value;
        this.version = version;
        this.bigBrother = bigBrother;
    }

    void setNext(FatNode next) {
        this.next = next;
    }

    void setPrev(FatNode prev) {
        this.prev = prev;
    }

    FatNode getBigBrother() {
        return bigBrother;
    }

    FatNode getNext() {
        return next;
    }

    FatNode getPrev() {
        return prev;
    }

    int getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return value + "(" + version + ")";
    }

    int getValue() {
        return value;
    }

    public Node next(int version) {
        if(next == null) {
            throw new IllegalArgumentException("Next isn't exist");
        }
        return next.getSmallNode(version);
    }

    boolean hasNext() {
        return next != null;
    }

    boolean hasPrev() {
        return prev != null;
    }
}


