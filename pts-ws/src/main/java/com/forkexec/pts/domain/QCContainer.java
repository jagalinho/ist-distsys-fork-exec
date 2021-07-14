package com.forkexec.pts.domain;

import java.util.concurrent.atomic.AtomicInteger;

public class QCContainer<E> {
    private AtomicInteger sequence;
    private E object;

    public QCContainer(E object) {
        this.object = object;
        this.sequence = new AtomicInteger(0);
    }

    public int getSequence() {
        return sequence.get();
    }

    public void setSequence(int sequence) {
        this.sequence.set(sequence);
    }

    public E getObject() {
        return object;
    }

    public void setObject(E object) {
        this.object = object;
    }
}
