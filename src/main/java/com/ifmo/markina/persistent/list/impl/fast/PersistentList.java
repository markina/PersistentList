package com.ifmo.markina.persistent.list.impl.fast;

import com.ifmo.markina.persistent.list.*;

import javax.naming.OperationNotSupportedException;
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

    // TODO check
    @Override
    public void remove(int index) {
        throw new IllegalArgumentException("remove"); // TODO remove !!
    }
//        prevVersion = currentVersion;
//        currentVersion++;
//
//        if (isEmpty()) {
//            throw new IllegalArgumentException("Index out of bounds");
//        }
//
//        Node<E> node = getNode(index, prevVersion);
//        FatNode<E> rightFatNode = node.getNext();
//        FatNode<E> leftFatNode = node.getPrev();
//
//        Node<E> rightNode = null;
//        Node<E> leftNode = null;
//
//        if (rightFatNode != null && rightFatNode.hasSecondNode()) {
//            FatNode<E> newFatNode = new FatNode<>();
//            rightNode = new Node<>(rightFatNode.getSecond().getValue(), currentVersion, newFatNode);
//            newFatNode.setFirst(rightNode);
//            rightNode.setPrev(leftFatNode);
//            linkToRight(rightNode, rightFatNode.getSecond().getNext());
//        } else if (rightFatNode != null && !rightFatNode.hasSecondNode()) {
//            rightNode = new Node<>(rightFatNode.getFirst().getValue(), currentVersion, rightFatNode);
//            rightFatNode.setSecond(rightNode);
//            rightNode.setPrev(leftFatNode);
//            if (rightFatNode.getFirst().getNext() == null) {
//                tails.add(rightFatNode);
//            }
//            rightNode.setNext(rightFatNode.getFirst().getNext());
//        }
//
//        if (leftFatNode != null && leftFatNode.hasSecondNode()) {
//            FatNode<E> newFatNode = new FatNode<>();
//            leftNode = new Node<>(leftFatNode.getSecond().getValue(), currentVersion, newFatNode);
//            newFatNode.setFirst(leftNode);
//            leftNode.setNext(rightFatNode);
//            linkToLeft(leftNode, leftFatNode.getSecond().getPrev());
//        } else if (leftFatNode != null && !leftFatNode.hasSecondNode()) {
//            leftNode = new Node<>(leftFatNode.getFirst().getValue(), currentVersion, leftFatNode);
//            leftFatNode.setSecond(leftNode);
//            leftNode.setNext(rightFatNode);
//            if (leftFatNode.getFirst().getPrev() == null) {
//                heads.add(leftFatNode);
//            }
//            leftNode.setPrev(leftFatNode.getFirst().getPrev());
//        }
//
//        if (rightNode != null) {
//            rightNode.setPrev(leftNode == null ? null : leftNode.getBigBrother());
//        }
//
//        if (leftNode != null) {
//            leftNode.setNext(rightNode == null ? null : rightNode.getBigBrother());
//        }
//
//        if (leftFatNode == null) {
//            heads.add(rightFatNode);
//        }
//        if (rightFatNode == null) {
//            tails.add(leftFatNode);
//        }
//
//        size--;
//        if (heads.size() < currentVersion + 1) {
//            heads.add(heads.get(prevVersion));
//        }
//        if (tails.size() < currentVersion + 1) {
//            tails.add(tails.get(prevVersion));
//        }
//    }

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

    private Node<E> createNode(FatNode<E> fatNode, E value) {  // TODO rename
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

    private Node<E> createNode(FatNode<E> fatNode) { // TODO rename
        Node<E> newNode;
        if (fatNode.hasSecondNode()) {
            FatNode<E> newFatNode = new FatNode<>();
            newNode = new Node<>(fatNode.getSecond().getValue(), currentVersion, newFatNode);
            newFatNode.setFirst(newNode);
        } else {
            newNode = new Node<>(fatNode.getFirst().getValue(), currentVersion, fatNode);
            fatNode.setSecond(newNode);
        }
        return newNode;
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
            rightNode.setPrev(newNode.getBigBrother());
            newNode.setNext(rightNode.getBigBrother());
            linkToLeft(newNode, toModifyFatNode.getSecond().getPrev());
        } else {
            Node<E> newNode = new Node<>(toModifyFatNode.getFirst().getValue(), currentVersion, toModifyFatNode);
            toModifyFatNode.setSecond(newNode);
            if (toModifyFatNode.getFirst().getPrev() == null) { // curFatNode is head
                heads.add(toModifyFatNode);
            }
            newNode.setPrev(toModifyFatNode.getFirst().getPrev());
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