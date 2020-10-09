package org.wowtools.qrtransfer.common.util;

import java.util.NoSuchElementException;

/**
 * byte双端队列
 *
 * @author liuyu
 * @date 2020/9/29
 */

public class ByteDeque {
    private int n;
    private Node first;
    private Node last;

    private class Node {
        private byte item;
        private Node next;
        private Node prev;
    }

    public ByteDeque() {
        n = 0;
        first = null;
        last = null;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return n;
    }

    public void addFirst(byte item) {
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.prev = null;
        // it is and empty queue..
        if (oldfirst == null) {
            last = first;
            first.next = null;
        } else {
            first.next = oldfirst;
            oldfirst.prev = first;
        }
        n++;
    }

    public void addLast(byte item) {
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        if (oldlast == null) {
            first = last;
            last.prev = null;
        } else {
            last.prev = oldlast;
            oldlast.next = last;
        }
        n++;
    }

    public byte removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException(
                    "Can't remove from empty deque");
        byte item = first.item;
        first = first.next;
        n--;
        if (n == 0)
            last = null;
        else
            first.prev = null;
        return item;
    }

    public byte removeLast() {
        if (isEmpty())
            throw new NoSuchElementException(
                    "Stack underflow");
        byte item = last.item;
        last = last.prev;
        n--;
        if (n == 0)
            first = null;
        else
            last.next = null;
        return item;
    }


}
