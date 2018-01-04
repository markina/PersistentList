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
            recRight(newNode, prev.getNext());
            FatNode<E> left = recLeft(newNode.getBigBrother(), prev.getBigBrother());
            newNode.setPrev(left);
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

    // TODO Убрать рекурсию
    private void recRight(Node<E> prevFatNode, FatNode<E> curFatNode) {
        if (curFatNode == null) { // it's end of list
            tails.add(prevFatNode.getBigBrother());
            return;
        }

        if (!curFatNode.hasSecondNode()) {
            Node<E> copyNode = new Node<>(curFatNode.getFirst().getValue(), currentVersion, curFatNode);
            copyNode.setPrev(prevFatNode.getBigBrother());
            if (curFatNode.getFirst().getNext() == null) {
                tails.add(curFatNode);
            }
            copyNode.setNext(curFatNode.getFirst().getNext());
            curFatNode.setSecond(copyNode);
            prevFatNode.setNext(curFatNode);
        } else {
            FatNode<E> newFatNode = new FatNode<>();
            Node<E> copyNode = new Node<>(curFatNode.getSecond().getValue(), currentVersion, newFatNode);
            newFatNode.setFirst(copyNode);
            copyNode.setPrev(prevFatNode.getBigBrother());
            recRight(copyNode, curFatNode.getSecond().getNext());
            prevFatNode.setNext(newFatNode);
        }
    }

    // TODO Убрать рекурсию
    private FatNode<E> recLeft(FatNode<E> nextFatNode, FatNode<E> curFatNode) {
        if (curFatNode == null) { // it's start of list
            heads.add(nextFatNode);
            return null;
        }

        if (!curFatNode.hasSecondNode()) {
            Node<E> copyNode = new Node<>(curFatNode.getFirst().getValue(), currentVersion, curFatNode);
            if (curFatNode.getFirst().getPrev() == null) { // curFatNode is head
                heads.add(curFatNode);
            }
            copyNode.setNext(nextFatNode);
            copyNode.setPrev(curFatNode.getFirst().getPrev());
            curFatNode.setSecond(copyNode);
            return curFatNode;
        } else {
            FatNode<E> newFatNode = new FatNode<>();
            Node<E> copyNode = new Node<>(curFatNode.getSecond().getValue(), currentVersion, newFatNode);
            newFatNode.setFirst(copyNode);
            copyNode.setNext(nextFatNode);
            FatNode<E> left = recLeft(newFatNode, curFatNode.getSecond().getPrev());
            copyNode.setPrev(left);
            return newFatNode;
        }
    }

    private void addHead(E value) {
        final FatNode<E> fatNode = new FatNode<>();
        final Node<E> node = new Node<>(value, currentVersion, fatNode);
        fatNode.setFirst(node);
        heads.add(fatNode);

        recRight(node, heads.get(prevVersion));
    }

    private void addTail(E value) {
        final FatNode<E> fatNode = new FatNode<>();
        final Node<E> node = new Node<>(value, currentVersion, fatNode);
        fatNode.setFirst(node);
        tails.add(fatNode);

        FatNode<E> leftFatNode = recLeft(fatNode, tails.get(prevVersion));
        node.setPrev(leftFatNode);
    }

    private void set(Node<E> node, E newValue) {
        if (!node.getBigBrother().hasSecondNode()) {
            Node<E> copyNode = new Node<>(newValue, currentVersion, node.getBigBrother());
            node.getBigBrother().setSecond(copyNode);
            recRight(copyNode, node.getNext());
            FatNode<E> left = recLeft(node.getBigBrother(), node.getPrev());
            copyNode.setPrev(left);
        } else {
            FatNode<E> fatNode = new FatNode<>();
            Node<E> copyNode = new Node<>(newValue, currentVersion, fatNode);
            fatNode.setFirst(copyNode);
            recRight(copyNode, node.getNext());
            FatNode<E> left = recLeft(fatNode, node.getPrev());
            copyNode.setPrev(left);
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