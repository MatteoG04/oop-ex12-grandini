package p12.exercise;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

public class MultiQueueImpl<T, Q> implements MultiQueue<T, Q>{

    private final Map<Q, Queue<T>> queues;

    public MultiQueueImpl() {
        this.queues = new HashMap<>();
    }

    @Override
    public Set<Q> availableQueues() {
        // Defensive copy
        return Set.copyOf(this.queues.keySet());
    }

    @Override
    public void openNewQueue(Q queue) {
        if (queueExists(queue)) {
            throw new IllegalArgumentException();
        }
        this.queues.put(queue, new ArrayDeque<>());
    }

    @Override
    public boolean isQueueEmpty(Q queue) {
        return getQueue(queue).isEmpty();
    }

    @Override
    public void enqueue(T elem, Q queue) {
        getQueue(queue).add(elem);
    }

    @Override
    public T dequeue(Q queue) {
        return getQueue(queue).poll();
    }

    @Override
    public Map<Q, T> dequeueOneFromAllQueues() {
        final Map<Q, T> map = new TreeMap<>();
        for (Q queue : this.queues.keySet()) {
            map.put(queue, getQueue(queue).poll());
        }
        return map;
    }

    @Override
    public Set<T> allEnqueuedElements() {
        Set<T> elements = new HashSet<>();
        for (Q queue : this.queues.keySet()) {
            elements.addAll(getQueue(queue));
        }
        return elements;
    }

    @Override
    public List<T> dequeueAllFromQueue(Q queue) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'dequeueAllFromQueue'");
    }

    @Override
    public void closeQueueAndReallocate(Q queue) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'closeQueueAndReallocate'");
    }

    private boolean queueExists(Q queue) {
        return this.queues.containsKey(queue);
    }

    private Queue<T> getQueue(Q queue) {
        if (!queueExists(queue)) {
            throw new IllegalArgumentException();
        }
        return this.queues.get(queue);
    }

}
