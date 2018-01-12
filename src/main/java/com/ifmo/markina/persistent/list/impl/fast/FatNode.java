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
            throw new IllegalArgumentException("First node in fat node already exist");
        }
        this.first = first;
    }

    void setSecond(Node<E> second) {
        if (this.second != null) {
            throw new IllegalArgumentException("Second node in fat node already exist");
        }
        this.second = second;
    }

    Node<E> getNode(int version) {
        if (hasSecondNode()) {
            if (second.getVersion() <= version) {
                return second;
            } else {
                return first;
            }
        } else {
            return first;
        }
    }

    boolean hasSecondNode() {
        return second != null;
    }

    E getLaterValue() {
        return hasSecondNode()
                ? getSecond().getValue()
                : getFirst().getValue();
    }
}

