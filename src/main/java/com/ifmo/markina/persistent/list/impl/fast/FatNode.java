package com.ifmo.markina.persistent.list.impl.fast;

public class FatNode {
    private Node first;
    private Node second;

    @Override
    public String toString() {
        return "FatNode{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }

    Node getFirst() {
        return first;
    }

    Node getSecond() {
        return second;
    }

    void setFirst(Node first) {
        if (this.first != null) {
            throw new IllegalArgumentException("First small node in node already exist");
        }
        this.first = first;
    }

    void setSecond(Node second) {
        if (this.second != null) {
            throw new IllegalArgumentException("Second small node in node already exist");
        }
        this.second = second;
    }

    Node getSmallNode(int version) {
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

