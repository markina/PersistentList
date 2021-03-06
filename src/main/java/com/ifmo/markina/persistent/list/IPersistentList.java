package com.ifmo.markina.persistent.list;

import java.util.NoSuchElementException;

public interface IPersistentList<E> {
    /**
     * Returns the first element  of the specified version of list.
     *
     * @param version the specified version
     * @return the first element  of the specified version of list
     * @throws NoSuchElementException if list is empty
     */
    E getFirst(int version);

    /**
     * Returns the last element of the specified version of list.
     *
     * @param version the specified version
     * @return the last element of the specified version of list
     */
    E getLast(int version);

    /**
     * Returns the element at the specified position at the specified version of list.
     *
     * @param version the specified version
     * @return the element at the specified position at the specified version of list
     */
    E get(int index, int version);

    /**
     * Returns current number of version.
     *
     * @return current number
     */
    int getCurrentVersion();

    /**
     * Returns current size of list. Time should be O(1).
     *
     * @return current size
     */
    int getCurrentSize();

    /**
     * Return size of list at the specified version.
     *
     * @param version the specified version
     * @return size of list at the specified version
     */
    int getSize(int version);

    /**
     * Returns <tt>true</tt> if the specified version of list contains no elements.
     *
     * @param version the specified version
     * @return <tt>true</tt> if the specified version of list contains no elements
     */
    boolean isEmpty(int version);

    /**
     * Inserts the specified element at the specified position in this list.
     * Note: this method increased version
     *
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    void add(int index, E value);

    /**
     * Replaces the element at the specified position in this list with the
     * specified element
     * Note: this method increased version
     *
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    void set(int index, E newValue);

    /**
     * Remove the element at the specified position in this list with the
     * specified element
     * Note: this method increased version
     *
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    void remove(int index);

    void addFirst(E value);

    void addLast(E value);

    void removeFirst();

    void removeLast();

    void setFirst(E value);

    void setLast(E value);

    IIterator<E> getHeadIterator(int version);

    IIterator<E> getTailIterator(int version);

    IIterator<E> getIterator(int index, int version);

    default IIterator<E> getHeadIterator() {
        return getHeadIterator(getCurrentVersion());
    }

    default IIterator<E> getTailIterator() {
        return getTailIterator(getCurrentVersion());
    }

    default IIterator<E> getIterator(int index) {
        return getIterator(index, getCurrentVersion());
    }

    /**
     * Returns the first element of current version of list.
     *
     * @return the first element of current version of list
     */
    default E getFirst() {
        return getFirst(getCurrentVersion());
    }

    /**
     * Returns the last element of current version of list.
     *
     * @return the last element of current version of list
     */
    default E getLast() {
        return getLast(getCurrentVersion());
    }

    /**
     * Returns the element at the specified position in current version of list.
     *
     * @return the element at the specified position in current version of list
     */
    default E get(int index) {
        return get(index, getCurrentVersion());
    }

    /**
     * Returns <tt>true</tt> if the last version of list contains no elements.
     *
     * @return <tt>true</tt> if the last version of list contains no elements
     */
    default boolean isEmpty() {
        return isEmpty(getCurrentVersion());
    }
}