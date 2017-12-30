package com.ifmo.markina.persistent.list.impl.fast;

import com.ifmo.markina.persistent.list.*;

import java.util.ArrayList;
import java.util.List;

public class PersistentList implements PersistentListI {
    private int FIRST_VERSION = -1;

    private List<Node> heads;
    private List<Node> tails;
    private int prevVersion;
    private int currentVersion;


    public PersistentList() {
        currentVersion = FIRST_VERSION;
        heads = new ArrayList<>();
        tails = new ArrayList<>();
    }

    @Override
    public void add(int index, int value) {
        prevVersion = currentVersion;
        currentVersion++;

        innerAdd(index, value);

        if (heads.size() < currentVersion + 1) {
            heads.add(heads.get(prevVersion));
        }
        if (tails.size() < currentVersion + 1) {
            tails.add(tails.get(prevVersion));
        }
    }

    @Override
    public void edit(int index, int newValue) {
        prevVersion = currentVersion;
        currentVersion++;

        innerEdit(index, newValue);

        if (heads.size() < currentVersion + 1) {
            heads.add(heads.get(prevVersion));
        }
        if (tails.size() < currentVersion + 1) {
            tails.add(tails.get(prevVersion));
        }
    }

    @Override
    public void remove(int index) {
        prevVersion = currentVersion;
        currentVersion++;

        innerRemove(index);

        copyHead();
        copyTail();
    }

    @Override
    public IteratorI getHead(int version) {
        return new PersistentListIterator(heads.get(version).getSmallNode(version), version);
    }

    @Override
    public IteratorI getTail(int version) {
        return new PersistentListIterator(tails.get(version).getSmallNode(version), version);
    }

    @Override
    public IteratorI get(int index, int version) {
        SmallNode smallNode = getSmallNode(index, version);
        return new PersistentListIterator(smallNode, version);
    }

    @Override
    public int getCurrentVersion() {
        return currentVersion;
    }

    @Override
    public boolean isEmpty() {
        return prevVersion == FIRST_VERSION || heads.get(prevVersion) == null;
    }

    private SmallNode getHeadSmallNode(int version) {
        if (heads.size() < version) {
            throw new IllegalArgumentException("Head of version " + version + " isn't exist");
        }
        return heads.get(version).getSmallNode(version);
    }

    private SmallNode getTailSmallNode(int version) {
        if (tails.size() < version) {
            throw new IllegalArgumentException("Tails of version " + version + " isn't exist");
        }
        return tails.get(version).getSmallNode(version);
    }

    public SmallNode getSmallNode(int index, int version) {
        int curIndex = 0;
        SmallNode smallNode = getHeadSmallNode(version);
        while (smallNode.hasNext() && curIndex != index) {
            curIndex++;
            smallNode = smallNode.getNext().getSmallNode(version);
        }
        if (curIndex != index) {
            throw new IllegalArgumentException("Index out of bound");
        }

        return smallNode;
    }

    List<Node> getHeads() {
        return heads;
    }

    List<Node> getTails() {
        return tails;
    }

    private void copyTail() {
        if (tails.size() == currentVersion + 1) {
            return;
        }
        Node node = tails.get(prevVersion);
        Node newNode = recRight(null, node);
        tails.add(newNode);
    }

    private void copyHead() {
        if (heads.size() == currentVersion + 1) {
            return;
        }
        Node node = heads.get(prevVersion);
        Node newNode = recRight(null, node);
        heads.add(newNode);
    }

    private void initList(int value) {
        final Node node = new Node();
        final SmallNode smallNode = new SmallNode(value, currentVersion, node);
        node.setFirst(smallNode);
        heads.add(node);
        tails.add(node);
        return;
    }

    private void innerEdit(int index, int newValue) {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        SmallNode smallNode = getSmallNode(index, prevVersion);

        if (!smallNode.getBigBrother().hasSecondSmallNode()) {
            SmallNode copySmallNode = new SmallNode(newValue, currentVersion, smallNode.getBigBrother());
            smallNode.getBigBrother().setSecond(copySmallNode);
            Node right = recRight(smallNode.getBigBrother(), smallNode.getNext());
            Node left = recLeft(smallNode.getBigBrother(), smallNode.getPrev());
            copySmallNode.setPrev(left);
            copySmallNode.setNext(right);
        } else {
            Node node = new Node();
            SmallNode copySmallNode = new SmallNode(newValue, currentVersion, node);
            node.setFirst(copySmallNode);
            Node right = recRight(node, smallNode.getNext());
            Node left = recLeft(node, smallNode.getPrev());
            copySmallNode.setPrev(left);
            copySmallNode.setNext(right);
        }
    }

    private void innerAdd(int index, int value) {
        if (isEmpty()) {
            initList(value);
            return;
        }

        if (index == 0) { // add new head
            addHead(value);
            return;
        }

        SmallNode smallNode = getSmallNode(index-1, prevVersion); // TODO rename smallNode -> prev?

        if (!smallNode.hasNext()) {
            addTail(value);
            return;
        }

        Node newNode = new Node();
        SmallNode newSmallNode = new SmallNode(value, currentVersion, newNode);
        newNode.setFirst(newSmallNode);
        Node right = recRight(newSmallNode.getBigBrother(), smallNode.getNext());
        Node left = recLeft(newSmallNode.getBigBrother(), smallNode.getBigBrother());
        newSmallNode.setPrev(left);
        newSmallNode.setNext(right);
    }

    private void innerRemove(int index) { // TODO
        if (isEmpty()) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        throw new UnsupportedOperationException("remove"); // TODO remove
    }

    private Node recRight(Node prevNode, Node curNode) {
        if (curNode == null) { // it's end of list
            tails.add(prevNode);
            return null;
        }

        if (!curNode.hasSecondSmallNode()) {
            SmallNode copySmallNode = new SmallNode(curNode.getFirst().getValue(), currentVersion, curNode);
            copySmallNode.setPrev(prevNode);
            if (curNode.getFirst().getNext() == null) {
                tails.add(curNode);
            }
            copySmallNode.setNext(curNode.getFirst().getNext());
            curNode.setSecond(copySmallNode);
            return curNode;
        } else {
            Node newNode = new Node();
            SmallNode copySmallNode = new SmallNode(curNode.getSecond().getValue(), currentVersion, newNode);
            newNode.setFirst(copySmallNode);
            copySmallNode.setPrev(prevNode);
            Node right = recRight(newNode, curNode.getSecond().getNext());
            copySmallNode.setNext(right);
            return newNode;
        }
    }

    private Node recLeft(Node nextNode, Node curNode) {
        if (curNode == null) { // it's start of list
            heads.add(nextNode);
            return null;
        }

        if (!curNode.hasSecondSmallNode()) {
            SmallNode copySmallNode = new SmallNode(curNode.getFirst().getValue(), currentVersion, curNode);
            if (curNode.getFirst().getPrev() == null) { // curNode is head
                heads.add(curNode);
            }
            copySmallNode.setNext(nextNode);
            copySmallNode.setPrev(curNode.getFirst().getPrev());
            curNode.setSecond(copySmallNode);
            return curNode;
        } else {
            Node newNode = new Node();
            SmallNode copySmallNode = new SmallNode(curNode.getSecond().getValue(), currentVersion, newNode);
            newNode.setFirst(copySmallNode);
            copySmallNode.setNext(nextNode);
            Node left = recLeft(newNode, curNode.getSecond().getPrev());
            copySmallNode.setPrev(left);
            return newNode;
        }
    }

    private void addHead(int value) {
        final Node node = new Node();
        final SmallNode smallNode = new SmallNode(value, currentVersion, node);
        node.setFirst(smallNode);
        heads.add(node);

        Node rightNode = recRight(node, heads.get(prevVersion));
        smallNode.setNext(rightNode);
    }

    private void addTail(int value) {
        final Node node = new Node();
        final SmallNode smallNode = new SmallNode(value, currentVersion, node);
        node.setFirst(smallNode);
        tails.add(node);

        Node leftNode = recLeft(node, tails.get(prevVersion));
        smallNode.setPrev(leftNode);
    }
}