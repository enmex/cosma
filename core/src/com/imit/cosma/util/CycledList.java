package com.imit.cosma.util;

import java.util.LinkedList;

class Node<T> {
    protected T value;
    protected Node<T> next;
}

public class CycledList<T> {
    private final Node<T> head;
    private Node<T> current;
    private int size;

    public CycledList() {
        size = 0;
        head = new Node<>();
        current = head;
    }

    public void add(T t) {
        if (size == 0) {
            head.value = t;
            head.next = current;
        } else {
            Node<T> node = new Node<>();
            node.value = t;
            node.next = head;
            current.next = node;
        }
        size++;
        current = current.next;
    }

    public T get() {
        return current.value;
    }

    public T next() {
        current = current.next;
        return current.value;
    }
}
