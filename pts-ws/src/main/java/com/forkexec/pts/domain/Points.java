package com.forkexec.pts.domain;

import com.forkexec.pts.domain.exceptions.*;
import com.forkexec.pts.ws.QCint;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Points
 * <p>
 * A points server.
 */
public class Points {
    /**
     * Map with balances of all users
     */
    private Map<String, QCContainer<AtomicInteger>> balances = new ConcurrentHashMap<>();

    /**
     * Constant representing the default initial balance for every new client
     */
    private static final int DEFAULT_INITIAL_BALANCE = 100;

    /**
     * Global with the current value for the initial balance of every new client
     */
    private QCContainer<AtomicInteger> initialBalance = new QCContainer<>(new AtomicInteger(DEFAULT_INITIAL_BALANCE));

    // Singleton -------------------------------------------------------------

    /**
     * Private constructor prevents instantiation from other classes.
     */
    private Points() {
    }

    /**
     * SingletonHolder is loaded on the first execution of Singleton.getInstance()
     * or the first access to SingletonHolder.INSTANCE, not before.
     */
    private static class SingletonHolder {
        private static final Points INSTANCE = new Points();
    }

    public static synchronized Points getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void ctrlInit(final QCint startPoints) {
        initialBalance.getObject().set(startPoints.getValue());
        initialBalance.setSequence(startPoints.getSequence());
    }

    public void ctrlClear() {
        balances = new ConcurrentHashMap<>();
        initialBalance = new QCContainer<>(new AtomicInteger(DEFAULT_INITIAL_BALANCE));
    }

    public void addUser(final String user) throws EmailAlreadyExistsException {
        if (balances.containsKey(user))
            throw new EmailAlreadyExistsException();

        balances.put(user, new QCContainer<>(new AtomicInteger(initialBalance.getObject().get())));
    }

    public QCint getPoints(final String user) {
        if (!balances.containsKey(user))
            balances.put(user, new QCContainer<>(new AtomicInteger(initialBalance.getObject().get())));

        QCint qc = new QCint();

        QCContainer<AtomicInteger> container = balances.get(user);
        qc.setSequence(container.getSequence());
        qc.setValue(container.getObject().get());

        return qc;
    }

    public QCint getStartPoints() {
        QCint qc = new QCint();

        qc.setSequence(initialBalance.getSequence());
        qc.setValue(initialBalance.getObject().get());

        return qc;
    }

    public void setPoints(final String user, final QCint points) {
        if (balances.containsKey(user))
            balances.get(user).getObject().set(points.getValue());
        else
            balances.put(user, new QCContainer<>(new AtomicInteger(points.getValue())));

        balances.get(user).setSequence(points.getSequence());
    }
}
