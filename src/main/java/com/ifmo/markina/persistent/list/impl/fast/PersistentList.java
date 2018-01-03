package com.ifmo.markina.persistent.list.impl.fast;

import com.ifmo.markina.persistent.list.*;

import java.util.ArrayList;
import java.util.List;

public class PersistentList implements PersistentListI {
    private int FIRST_VERSION = -1;

    private List<FatNode> heads;
    private List<FatNode> tails;
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
        Node node = getSmallNode(index, version);
        return new PersistentListIterator(node, version);
    }

    @Override
    public int getCurrentVersion() {
        return currentVersion;
    }

    @Override
    public boolean isEmpty() {
        return prevVersion == FIRST_VERSION || heads.get(prevVersion) == null;
    }

    private Node getHeadSmallNode(int version) {
        if (heads.size() < version) {
            throw new IllegalArgumentException("Head of version " + version + " isn't exist");
        }
        return heads.get(version).getSmallNode(version);
    }

    private Node getTailSmallNode(int version) {
        if (tails.size() < version) {
            throw new IllegalArgumentException("Tails of version " + version + " isn't exist");
        }
        return tails.get(version).getSmallNode(version);
    }

    public Node getSmallNode(int index, int version) {
        int curIndex = 0;
        Node node = getHeadSmallNode(version);
        while (node.hasNext() && curIndex != index) {
            curIndex++;
            node = node.getNext().getSmallNode(version);
        }
        if (curIndex != index) {
            throw new IllegalArgumentException("Index out of bound");
        }

        return node;
    }

    List<FatNode> getHeads() {
        return heads;
    }

    List<FatNode> getTails() {
        return tails;
    }

    private void copyTail() {
        if (tails.size() == currentVersion + 1) {
            return;
        }
        FatNode fatNode = tails.get(prevVersion);
        FatNode newFatNode = recRight(null, fatNode);
        tails.add(newFatNode);
    }
    // TODO как обределить, что версия подвершины принадлежит последней версии
    private void copyHead() {
        if (heads.size() == currentVersion + 1) {
            return;
        }
        FatNode fatNode = heads.get(prevVersion);
        FatNode newFatNode = recRight(null, fatNode);
        heads.add(newFatNode);
    }

    private void initList(int value) {
        final FatNode fatNode = new FatNode();
        final Node node = new Node(value, currentVersion, fatNode);
        fatNode.setFirst(node);
        heads.add(fatNode);
        tails.add(fatNode);
        return;
    }

    private void innerEdit(int index, int newValue) {
        if (isEmpty()) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        Node node = getSmallNode(index, prevVersion);

        if (!node.getBigBrother().hasSecondSmallNode()) {
            Node copyNode = new Node(newValue, currentVersion, node.getBigBrother());
            node.getBigBrother().setSecond(copyNode);
            FatNode right = recRight(node.getBigBrother(), node.getNext());
            FatNode left = recLeft(node.getBigBrother(), node.getPrev());
            copyNode.setPrev(left);
            copyNode.setNext(right);
        } else {
            FatNode fatNode = new FatNode();
            Node copyNode = new Node(newValue, currentVersion, fatNode);
            fatNode.setFirst(copyNode);
            FatNode right = recRight(fatNode, node.getNext());
            FatNode left = recLeft(fatNode, node.getPrev());
            copyNode.setPrev(left);
            copyNode.setNext(right);
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

        Node node = getSmallNode(index-1, prevVersion); // TODO rename node -> prev?

        if (!node.hasNext()) {
            addTail(value);
            return;
        }

        FatNode newFatNode = new FatNode();
        Node newNode = new Node(value, currentVersion, newFatNode);
        newFatNode.setFirst(newNode);
        FatNode right = recRight(newNode.getBigBrother(), node.getNext());
        FatNode left = recLeft(newNode.getBigBrother(), node.getBigBrother());
        newNode.setPrev(left);
        newNode.setNext(right);
    }

    private void innerRemove(int index) { // TODO
        if (isEmpty()) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        throw new UnsupportedOperationException("remove"); // TODO remove
    }

    private FatNode recRight(FatNode prevFatNode, FatNode curFatNode) {
        if (curFatNode == null) { // it's end of list
            tails.add(prevFatNode);
            return null;
        }

        if (!curFatNode.hasSecondSmallNode()) {
            Node copyNode = new Node(curFatNode.getFirst().getValue(), currentVersion, curFatNode);
            copyNode.setPrev(prevFatNode);
            if (curFatNode.getFirst().getNext() == null) {
                tails.add(curFatNode);
            }
            copyNode.setNext(curFatNode.getFirst().getNext());
            curFatNode.setSecond(copyNode);
            return curFatNode;
        } else {
            FatNode newFatNode = new FatNode();
            Node copyNode = new Node(curFatNode.getSecond().getValue(), currentVersion, newFatNode);
            newFatNode.setFirst(copyNode);
            copyNode.setPrev(prevFatNode);
            FatNode right = recRight(newFatNode, curFatNode.getSecond().getNext());
            copyNode.setNext(right);
            return newFatNode;
        }
    }

    private FatNode recLeft(FatNode nextFatNode, FatNode curFatNode) {
        if (curFatNode == null) { // it's start of list
            heads.add(nextFatNode);
            return null;
        }

        if (!curFatNode.hasSecondSmallNode()) {
            Node copyNode = new Node(curFatNode.getFirst().getValue(), currentVersion, curFatNode);
            if (curFatNode.getFirst().getPrev() == null) { // curFatNode is head
                heads.add(curFatNode);
            }
            copyNode.setNext(nextFatNode);
            copyNode.setPrev(curFatNode.getFirst().getPrev());
            curFatNode.setSecond(copyNode);
            return curFatNode;
        } else {
            FatNode newFatNode = new FatNode();
            Node copyNode = new Node(curFatNode.getSecond().getValue(), currentVersion, newFatNode);
            newFatNode.setFirst(copyNode);
            copyNode.setNext(nextFatNode);
            FatNode left = recLeft(newFatNode, curFatNode.getSecond().getPrev());
            copyNode.setPrev(left);
            return newFatNode;
        }
    }

    private void addHead(int value) {
        final FatNode fatNode = new FatNode();
        final Node node = new Node(value, currentVersion, fatNode);
        fatNode.setFirst(node);
        heads.add(fatNode);

        FatNode rightFatNode = recRight(fatNode, heads.get(prevVersion));
        node.setNext(rightFatNode);
    }

    private void addTail(int value) {
        final FatNode fatNode = new FatNode();
        final Node node = new Node(value, currentVersion, fatNode);
        fatNode.setFirst(node);
        tails.add(fatNode);

        FatNode leftFatNode = recLeft(fatNode, tails.get(prevVersion));
        node.setPrev(leftFatNode);
    }
}