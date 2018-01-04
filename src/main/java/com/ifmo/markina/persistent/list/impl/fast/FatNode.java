package com.ifmo.markina.persistent.list.impl.fast;

public class FatNode<E> {
    private Node<E> first;
    private Node<E> second;

    @Override
    public String toString() {
        return "FatNode{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }

    Node<E> getFirst() {
        return first;
    }

    Node<E> getSecond() {
        return second;
    }

    void setFirst(Node<E> first) {
        if (this.first != null) {
            throw new IllegalArgumentException("First small node in node already exist");
        }
        this.first = first;
    }

    void setSecond(Node<E> second) {
        if (this.second != null) {
            throw new IllegalArgumentException("Second small node in node already exist");
        }
        this.second = second;
    }

    Node<E> getSmallNode(int version) {
        if (hasSecondSmallNode()) {
            if (second.getVersion() <= version) {
                return second;
            } else {
                return first;
            }
        } else {
            return first;
        }
    }

    boolean hasSecondSmallNode() {
        return second != null;
    }
}

