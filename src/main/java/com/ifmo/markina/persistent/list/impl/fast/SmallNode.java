package com.ifmo.markina.persistent.list.impl.fast;

public class SmallNode {
    private Node next;
    private Node prev;
    private Node bigBrother;

    private int version;
    private int value;

    SmallNode(int value, int version, Node bigBrother) {
        this.value = value;
        this.version = version;
        this.bigBrother = bigBrother;
    }

    void setNext(Node next) {
        this.next = next;
    }

    void setPrev(Node prev) {
        this.prev = prev;
    }

    Node getBigBrother() {
        return bigBrother;
    }

    Node getNext() {
        return next;
    }

    Node getPrev() {
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

    public SmallNode next(int version) {
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


