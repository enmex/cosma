package com.imit.cosma.util;

public class Stack<T> {
    private T[] elements;
    private int currentIndex;

    public Stack(int initialCapacity){
        elements = (T[]) new Object[initialCapacity];
        currentIndex = 0;
    }

    public Stack(){
        this(1000);
    }

    public void push(T element){
        elements[currentIndex] = element;
        currentIndex++;
    }

    public T pop(){
        T element = elements[currentIndex - 1];
        elements[currentIndex] = null;
        currentIndex--;
        return element;
    }
}
