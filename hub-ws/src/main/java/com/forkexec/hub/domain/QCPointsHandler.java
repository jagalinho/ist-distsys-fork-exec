package com.forkexec.hub.domain;

import com.forkexec.hub.domain.exceptions.*;
import com.forkexec.pts.ws.*;
import com.forkexec.pts.ws.cli.PointsClient;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;
import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINamingException;

import javax.xml.ws.Response;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class QCPointsHandler {
    private UDDINaming uddi;
    private int n;

    private Pattern emailPattern = Pattern.compile("[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*");

    private Set<String> activeUsers = new HashSet<>();

    public QCPointsHandler(UDDINaming uddi, int n) {
        this.uddi = uddi;
        this.n = n;
    }

    private List<PointsClient> getClients() {
        List<PointsClient> clients;
        try {
            clients = uddi.listRecords("T28_Points%").stream().map(c -> new PointsClient(c.getUrl())).collect(Collectors.toList());
            if (clients.size() == 0)
                throw new UDDINamingException();
        } catch (UDDINamingException e) {
            throw new UddiLookupException("Unable to lookup Points client", e);
        }
        return clients;
    }

    public synchronized void ctrlInit(int startPoints) throws InvalidInitException {
        if (startPoints < 0)
            throw new InvalidInitException();

        List<Response<GetStartPointsResponse>> readResponses;
        do {
            readResponses = getClients().stream().map(PointsClient::getStartPointsAsync).collect(Collectors.toList());
        } while (readResponses.size() < (n/2 + 1));
        while (readResponses.stream().filter(Response::isDone).count() < (n/2 + 1));
        int maxSequence = readResponses.stream().filter(Response::isDone).mapToInt(r -> {
            try {
                return r.get().getReturn().getSequence();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            return -1;
        }).max().orElseThrow(IllegalStateException::new);

        QCint QCstartPoints = new QCint();
        QCstartPoints.setValue(startPoints);
        QCstartPoints.setSequence(maxSequence + 1);

        List<Response<CtrlInitResponse>> responses;
        do {
            responses = getClients().stream().map(c -> c.ctrlInitAsync(QCstartPoints)).collect(Collectors.toList());
        } while (responses.size() < (n/2 + 1));
        while (responses.stream().filter(Response::isDone).count() < (n/2 + 1));
    }

    public void ctrlClear() {
        getClients().forEach(PointsClient::ctrlClear);
    }

    public void activateUser(String userEmail) throws InvalidUserIdException {
        if (userEmail == null || !emailPattern.matcher(userEmail).matches())
            throw new InvalidUserIdException();

        for (PointsClient c : getClients()) {
            try {
                c.addUser(userEmail);
            } catch (EmailAlreadyExistsFault_Exception e) {
                throw new InvalidUserIdException();
            }
        }
    }

    private QCint QCgetPoints(String userEmail) {
        List<Response<GetPointsResponse>> responses;
        do {
           responses = getClients().stream().map(c -> c.getPointsAsync(userEmail)).collect(Collectors.toList());
        } while (responses.size() < (n/2 + 1));
        while (responses.stream().filter(Response::isDone).count() < (n/2 + 1));

        try {
            return responses.stream().filter(Response::isDone).max(Comparator.comparingInt(r -> {
                try {
                    return r.get().getReturn().getSequence();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                return -1;
            })).orElseThrow(IllegalStateException::new).get().getReturn();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException(e);
        }
    }

    private void QCsetPoints(String userEmail, QCint QCnewPoints) {
        List<Response<SetPointsResponse>> responses;
        do {
            responses = getClients().stream().map(c -> c.setPointsAsync(userEmail, QCnewPoints)).collect(Collectors.toList());
        } while (responses.size() < (n/2 + 1));
        while (responses.stream().filter(Response::isDone).count() < (n/2 + 1));
    }

    public int pointsBalance(String userEmail) throws InvalidUserIdException {
        if (userEmail == null || !emailPattern.matcher(userEmail).matches())
            throw new InvalidUserIdException();

        synchronized (this) {
            while (activeUsers.contains(userEmail)) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
            activeUsers.add(userEmail);
        }

        int points = QCgetPoints(userEmail).getValue();

        synchronized (this) {
            activeUsers.remove(userEmail);
            this.notifyAll();
        }

        return points;
    }

    public void addPoints(String userEmail, int pointsToAdd) throws InvalidUserIdException, InvalidMoneyException {
        if (userEmail == null || !emailPattern.matcher(userEmail).matches())
            throw new InvalidUserIdException();

        if (pointsToAdd <= 0)
            throw new InvalidMoneyException();

        synchronized (this) {
            while (activeUsers.contains(userEmail)) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
            activeUsers.add(userEmail);
        }

        QCint QCpoints = QCgetPoints(userEmail);
        QCint QCnewPoints = new QCint();
        QCnewPoints.setValue(QCpoints.getValue() + pointsToAdd);
        QCnewPoints.setSequence(QCpoints.getSequence() + 1);

        QCsetPoints(userEmail, QCnewPoints);

        synchronized (this) {
            activeUsers.remove(userEmail);
            this.notifyAll();
        }
    }

    public void spendPoints(String userEmail, int pointsToSpend) throws InvalidUserIdException, InvalidMoneyException, NotEnoughPointsException {
        if (userEmail == null || !emailPattern.matcher(userEmail).matches())
            throw new InvalidUserIdException();

        if (pointsToSpend <= 0)
            throw new InvalidMoneyException();

        synchronized (this) {
            while (activeUsers.contains(userEmail)) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
            activeUsers.add(userEmail);
        }

        QCint QCpoints = QCgetPoints(userEmail);
        if (QCpoints.getValue() < pointsToSpend)
            throw new NotEnoughPointsException();
        QCint QCnewPoints = new QCint();
        QCnewPoints.setValue(QCpoints.getValue() - pointsToSpend);
        QCnewPoints.setSequence(QCpoints.getSequence() + 1);

        QCsetPoints(userEmail, QCnewPoints);

        synchronized (this) {
            activeUsers.remove(userEmail);
            this.notifyAll();
        }
    }
}
