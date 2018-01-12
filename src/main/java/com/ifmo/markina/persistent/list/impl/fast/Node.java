package com.ifmo.markina.persistent.list.impl.fast;

public class Node<E> {
    private FatNode<E> next;
    private FatNode<E> prev;
    private FatNode<E> bigBrother;

    private int version;
    private E value;

    Node(E value, int version, FatNode<E> bigBrother) {
        this.value = value;
        this.version = version;
        this.bigBrother = bigBrother;
    }

    void setNext(FatNode<E> next) {
        this.next = next;
    }

    void setPrev(FatNode<E> prev) {
        this.prev = prev;
    }

    FatNode<E> getBigBrother() {
        return bigBrother;
    }

    FatNode<E> getNext() {
        return next;
    }

    FatNode<E> getPrev() {
        return prev;
    }

    int getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return value + "(" + version + ")";
    }

    E getValue() {
        return value;
    }

    public Node<E> next(int version) {
        if (next == null) {
            throw new IllegalArgumentException("Next isn't exist");
        }
        return next.getNode(version);
    }

    boolean hasNext() {
        return next != null;
    }

    boolean hasPrev() {
        return prev != null;
    }
}


