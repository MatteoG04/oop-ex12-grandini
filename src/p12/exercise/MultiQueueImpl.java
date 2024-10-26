package p12.exercise;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.ArrayList;

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
        final Set<T> elements = new HashSet<>();
        for (Q queue : this.queues.keySet()) {
            elements.addAll(getQueue(queue));
        }
        return elements;
    }

    @Override
    public List<T> dequeueAllFromQueue(Q queue) {
        final List<T> elem = new ArrayList<>();
        while (getQueue(queue).size() > 0) {
            elem.add(getQueue(queue).poll());
        }
        return elem;
    }

    @Override
    public void closeQueueAndReallocate(Q queue) {
        final var numOfAvailableQueues = availableQueues().size() - 1;
        if (numOfAvailableQueues == 0) {
            throw new IllegalStateException();
        }

        final var elemsToReallocate = dequeueAllFromQueue(queue);
        this.queues.remove(queue);
        
        boolean first = true;
        var minSize = 0;
        Q minSizedQueue = null;
        for (Q q : this.queues.keySet()) {
            var qSize = getQueue(q).size();
            if (first || qSize < minSize) {
                minSize = qSize;
                minSizedQueue = q;
                first = false;
            }
        }

        getQueue(minSizedQueue).addAll(elemsToReallocate);        
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
