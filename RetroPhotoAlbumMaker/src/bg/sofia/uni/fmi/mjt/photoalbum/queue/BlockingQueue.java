package bg.sofia.uni.fmi.mjt.photoalbum.queue;

import java.util.ArrayDeque;
import java.util.Queue;

public class BlockingQueue<E> {
    private final Queue<E> queue;
    private final int capacity;

    private final Object notFull = new Object();
    private final Object notEmpty = new Object();

    public BlockingQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Blocking queue's capacity cannot be less than or equal to 0");
        }

        this.capacity = capacity;
        queue = new ArrayDeque<>(capacity);
    }

    public void put(E element) throws InterruptedException {
        synchronized (notFull) {
            while (queue.size() == capacity) {
                notFull.wait();
            }
            queue.add(element);
        }

        synchronized (notEmpty) {
            notEmpty.notifyAll();
        }
    }

    public E take() throws InterruptedException {
        E element;
        synchronized (notEmpty) {
            while (queue.isEmpty()) {
                notEmpty.wait();
            }
            element = queue.remove();
        }

        synchronized (notFull) {
            notFull.notifyAll();
        }
        return element;
    }
}
