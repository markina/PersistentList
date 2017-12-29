package com.ifmo.markina.persistent.list.impl.fast;

public class Node {
    private SmallNode first;
    private SmallNode second;

    @Override
    public String toString() {
        return "Node{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }

    SmallNode getFirst() {
        return first;
    }

    SmallNode getSecond() {
        return second;
    }

    void setFirst(SmallNode first) {
        if (this.first != null) {
            throw new IllegalArgumentException("First small node in node already exist");
        }
        this.first = first;
    }

    void setSecond(SmallNode second) {
        if (this.second != null) {
            throw new IllegalArgumentException("Second small node in node already exist");
        }
        this.second = second;
    }

    SmallNode getSmallNode(int version) {
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

