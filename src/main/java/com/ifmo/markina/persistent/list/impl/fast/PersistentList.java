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

        if (heads.size() < currentVersion + 1) {
            heads.add(heads.get(prevVersion));
        }
        if (tails.size() < currentVersion + 1) {
            tails.add(tails.get(prevVersion));
        }
    }


    @Override
    public void set(int index, E newValue) {
        prevVersion = currentVersion;
        currentVersion++;

        set(getNode(index, prevVersion), newValue);

        if (heads.size() < currentVersion + 1) { // TODO extract duplicate code
            heads.add(heads.get(prevVersion));
        }
        if (tails.size() < currentVersion + 1) {
            tails.add(tails.get(prevVersion));
        }
    }

    @Override // TODO
    public void remove(int index) {
        prevVersion = currentVersion;
        currentVersion++;

        if (isEmpty()) {
            throw new IllegalArgumentException("Index out of bounds");
        }

        size--;
        throw new UnsupportedOperationException("remove"); // TODO remove
//        if (heads.size() < currentVersion + 1) {
//            heads.add(heads.get(prevVersion));
//        }
//        if (tails.size() < currentVersion + 1) {
//            tails.add(tails.get(prevVersion));
//        }
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

    private void linkToRight(Node<E> leftNode, FatNode<E> toModifyFatNode) {
        if (toModifyFatNode == null) { // it's end of list
            tails.add(leftNode.getBigBrother());
            return;
        }

        if (toModifyFatNode.hasSecondNode()) {
            FatNode<E> newFatNode = new FatNode<>();
            Node<E> newNode = new Node<>(toModifyFatNode.getSecond().getValue(), currentVersion, newFatNode);
            newFatNode.setFirst(newNode);
            newNode.setPrev(leftNode.getBigBrother());
            leftNode.setNext(newFatNode);
            linkToRight(newNode, toModifyFatNode.getSecond().getNext());
        } else {
            Node<E> newNode = new Node<>(toModifyFatNode.getFirst().getValue(), currentVersion, toModifyFatNode);
            toModifyFatNode.setSecond(newNode);
            newNode.setPrev(leftNode.getBigBrother());
            if (toModifyFatNode.getFirst().getNext() == null) {
                tails.add(toModifyFatNode);
            }
            newNode.setNext(toModifyFatNode.getFirst().getNext());
            newNode.setPrev(leftNode.getBigBrother());
            leftNode.setNext(toModifyFatNode);
        }
    }

    private void linkToLeft(Node<E> rightNode, FatNode<E> toModifyFatNode) {
        if (toModifyFatNode == null) { // it's start of list
            heads.add(rightNode.getBigBrother());
            return;
        }

        if (toModifyFatNode.hasSecondNode()) {
            FatNode<E> newFatNode = new FatNode<>();
            Node<E> newNode = new Node<>(toModifyFatNode.getSecond().getValue(), currentVersion, newFatNode);
            newFatNode.setFirst(newNode);
            newNode.setNext(rightNode.getBigBrother());
            rightNode.setPrev(newFatNode);
            linkToLeft(newNode, toModifyFatNode.getSecond().getPrev());
        } else {
            Node<E> newNode = new Node<>(toModifyFatNode.getFirst().getValue(), currentVersion, toModifyFatNode);
            toModifyFatNode.setSecond(newNode);
            if (toModifyFatNode.getFirst().getPrev() == null) { // curFatNode is head
                heads.add(toModifyFatNode);
            }
            newNode.setPrev(toModifyFatNode.getFirst().getPrev());
            newNode.setNext(rightNode.getBigBrother());
            rightNode.setPrev(toModifyFatNode);
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
        if (node.getBigBrother().hasSecondNode()) {
            FatNode<E> newFatNode = new FatNode<>();
            Node<E> newNode = new Node<>(newValue, currentVersion, newFatNode);
            newFatNode.setFirst(newNode);
            linkToRight(newNode, node.getNext());
            linkToLeft(newNode, node.getPrev());
        } else {
            Node<E> newNode = new Node<>(newValue, currentVersion, node.getBigBrother());
            node.getBigBrother().setSecond(newNode);
            linkToRight(newNode, node.getNext());
            linkToLeft(newNode, node.getPrev());
        }
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
        if (head == null) {
            return null;// TODO ?
        }

        return head.getNode(version);
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
        if (tail == null) {
            return null; // TODO ?
        }

        return tail.getNode(version);
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