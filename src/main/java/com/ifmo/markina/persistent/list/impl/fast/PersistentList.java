package com.ifmo.markina.persistent.list.impl.fast;

import com.ifmo.markina.persistent.list.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class PersistentList<E> implements IPersistentList<E> {
    private List<FatNode<E>> heads;
    private List<FatNode<E>> tails;
    private int prevVersion;
    private int currentVersion;
    private int size;

    public PersistentList() {
        currentVersion = 0;
        heads = new ArrayList<>();
        tails = new ArrayList<>();

        heads.add(null);
        tails.add(null);
    }

    @Override
    public E getFirst(int version) {
        Node<E> node = getFirstNode(version);
        if (node == null) {
            throw new NoSuchElementException("List is empty");
        }
        return node.getValue();
    }

    @Override
    public E getLast(int version) {
        Node<E> node = getLastNode(version);
        if (node == null) {
            throw new NoSuchElementException("List is empty");
        }
        return node.getValue();
    }

    @Override
    public E get(int index, int version) {
        return getNode(index, version).getValue();
    }

    @Override
    public int getCurrentVersion() {
        return currentVersion;
    }

    @Override
    public int getCurrentSize() {
        return size;
    }

    @Override
    public boolean isEmpty(int version) {
        return getFirstNode(version) == null;
    }

    @Override
    public void add(int index, E value) {
        prevVersion = currentVersion;
        currentVersion++;

        if (getCurrentSize() == index) {
            addTail(value);
        } else if (index == 0) {
            addHead(value);
        } else {
            Node<E> prev = getNode(index - 1, prevVersion);
            FatNode<E> newFatNode = new FatNode<>();
            Node<E> newNode = new Node<>(value, currentVersion, newFatNode);
            newFatNode.setFirst(newNode);
            linkToRight(newNode, prev.getNext());
            linkToLeft(newNode, prev.getBigBrother());
        }
        size++;

        addHeadTailIfNeed();
    }

    @Override
    public void set(int index, E newValue) {
        prevVersion = currentVersion;
        currentVersion++;

        set(getNode(index, prevVersion), newValue);

        addHeadTailIfNeed();
    }

    @Override
    public void remove(int index) {
        if (isEmpty()) {
            throw new IllegalArgumentException("Index out of bounds");
        }

        prevVersion = currentVersion;
        currentVersion++;

        Node<E> node = getNode(index, prevVersion);
        FatNode<E> rightFatNode = node.getNext();
        FatNode<E> leftFatNode = node.getPrev();

        if (leftFatNode == null) {
            removeHead(node);
        } else if (rightFatNode == null) {
            removeTail(node);
        } else {
            Node<E> rightNode = createNode(rightFatNode);
            Node<E> leftNode = createNode(leftFatNode);

            rightNode.setPrev(leftNode.getBigBrother());
            leftNode.setNext(rightNode.getBigBrother());

            if (rightNode.getBigBrother().hasSecondNode()) {
                if (rightFatNode.getFirst().getNext() == null) {
                    tails.add(rightNode.getBigBrother());
                }
                rightNode.setNext(rightFatNode.getFirst().getNext());
            } else {
                linkToRight(rightNode, rightFatNode.getSecond().getNext());
            }

            if (leftNode.getBigBrother().hasSecondNode()) {
                if (leftFatNode.getFirst().getPrev() == null) {
                    heads.add(leftNode.getBigBrother());
                }
                leftNode.setPrev(leftFatNode.getFirst().getPrev());
            } else {
                linkToLeft(leftNode, leftFatNode.getSecond().getPrev());
            }
        }

        size--;
        addHeadTailIfNeed();
    }

    private void addHeadTailIfNeed() {
        if (heads.size() < currentVersion + 1) {
            heads.add(heads.get(prevVersion));
        }
        if (tails.size() < currentVersion + 1) {
            tails.add(tails.get(prevVersion));
        }
    }

    private void removeHead(Node<E> head) {
        if (!head.hasNext()) {
            heads.add(null);
            tails.add(null);
        } else {
            Node<E> newNode = createNode(head.getNext());
            newNode.setNext(head.getNext().getNode(prevVersion).getNext());
            heads.add(newNode.getBigBrother());
            if (!newNode.getBigBrother().hasSecondNode()) {
                linkToRight(newNode, head.getNext().getSecond().getNext());
            }
        }
    }

    private void removeTail(Node<E> tail) {
        if (!tail.hasPrev()) {
            heads.add(null);
            tails.add(null);
        } else {
            Node<E> newNode = createNode(tail.getPrev());
            newNode.setPrev(tail.getPrev().getNode(prevVersion).getPrev());
            tails.add(newNode.getBigBrother());
            if (!newNode.getBigBrother().hasSecondNode()) {
                linkToLeft(newNode, tail.getPrev().getSecond().getPrev());
            }
        }
    }

    @Override
    public IIterator<E> getHeadIterator(int version) {
        if (getFirstNode(version) == null) {
            throw new NoSuchElementException("Element is absent");
        }
        return new PersistentListIterator<>(getFirstNode(version), version);
    }

    @Override
    public IIterator<E> getTailIterator(int version) {
        if (getLastNode(version) == null) {
            throw new NoSuchElementException("Element is absent");
        }
        return new PersistentListIterator<>(getLastNode(version), version);
    }

    @Override
    public IIterator<E> getIterator(int index, int version) {
        if (getNode(index, version) == null) {
            throw new NoSuchElementException("Element is absent");
        }
        return new PersistentListIterator<>(getNode(index, version), version);
    }

    private Node<E> createNode(FatNode<E> fatNode, E value) {
        Node<E> newNode;
        if (fatNode.hasSecondNode()) {
            FatNode<E> newFatNode = new FatNode<>();
            newNode = new Node<>(value, currentVersion, newFatNode);
            newFatNode.setFirst(newNode);
        } else {
            newNode = new Node<>(value, currentVersion, fatNode);
            fatNode.setSecond(newNode);
        }
        return newNode;
    }

    private Node<E> createNode(FatNode<E> fatNode) {
        return createNode(fatNode, fatNode.getLasterValue());
    }

    private void linkToRight(Node<E> leftNode, FatNode<E> fatNode) {
        if (fatNode == null) { // it's end of list
            tails.add(leftNode.getBigBrother());
            return;
        }

        if (fatNode.hasSecondNode()) {
            Node<E> newNode = createNode(fatNode);
            newNode.setPrev(leftNode.getBigBrother());
            leftNode.setNext(newNode.getBigBrother());
            linkToRight(newNode, fatNode.getSecond().getNext());
        } else {
            Node<E> newNode = createNode(fatNode);
            if (fatNode.getFirst().getNext() == null) {
                tails.add(fatNode);
            }
            newNode.setPrev(leftNode.getBigBrother());
            newNode.setNext(fatNode.getFirst().getNext());
            leftNode.setNext(newNode.getBigBrother());
        }
    }

    private void linkToLeft(Node<E> rightNode, FatNode<E> fatNode) {
        if (fatNode == null) { // it's start of list
            heads.add(rightNode.getBigBrother());
            return;
        }

        if (fatNode.hasSecondNode()) {
            Node<E> newNode = createNode(fatNode);
            rightNode.setPrev(newNode.getBigBrother());
            newNode.setNext(rightNode.getBigBrother());
            linkToLeft(newNode, fatNode.getSecond().getPrev());
        } else {
            Node<E> newNode = createNode(fatNode);
            if (fatNode.getFirst().getPrev() == null) {
                heads.add(fatNode);
            }
            newNode.setPrev(fatNode.getFirst().getPrev());
            newNode.setNext(rightNode.getBigBrother());
            rightNode.setPrev(newNode.getBigBrother());
        }
    }

    private void addHead(E value) {
        final FatNode<E> fatNode = new FatNode<>();
        final Node<E> node = new Node<>(value, currentVersion, fatNode);
        fatNode.setFirst(node);
        heads.add(fatNode);

        linkToRight(node, heads.get(prevVersion));
    }

    private void addTail(E value) {
        final FatNode<E> fatNode = new FatNode<>();
        final Node<E> node = new Node<>(value, currentVersion, fatNode);
        fatNode.setFirst(node);
        tails.add(fatNode);

        linkToLeft(node, tails.get(prevVersion));
    }

    private void set(Node<E> node, E newValue) {
        Node<E> newNode = createNode(node.getBigBrother(), newValue);
        linkToRight(newNode, node.getNext());
        linkToLeft(newNode, node.getPrev());
    }

    /**
     * Returns node which is first node at the specified version of list.
     *
     * @param version the specified version
     * @return node
     */
    private Node<E> getFirstNode(int version) {
        if (heads.size() < version) {
            throw new IllegalArgumentException("List with version " + version + " isn't exist.");
        }

        FatNode<E> head = heads.get(version);
        return head == null ? null : head.getNode(version);
    }

    /**
     * Returns node which is last node at the specified version of list.
     *
     * @param version the specified version
     * @return node
     */
    private Node<E> getLastNode(int version) {
        if (tails.size() < version) {
            throw new IllegalArgumentException("List with version " + version + " isn't exist.");
        }

        FatNode<E> tail = tails.get(version);
        return tail == null ? null : tail.getNode(version);
    }

    private Node<E> getNode(int index, int version) {
        int curIndex = 0;
        Node<E> node = getFirstNode(version);
        if (node == null) {
            throw new NoSuchElementException("List is empty");
        }
        while (node.hasNext() && curIndex != index) {
            curIndex++;
            node = node.getNext().getNode(version);
        }
        if (curIndex != index) {
            throw new IllegalArgumentException("Index out of bound");
        }

        return node;
    }
}