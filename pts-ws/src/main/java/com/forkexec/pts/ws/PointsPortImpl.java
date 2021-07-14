package com.forkexec.pts.ws;

import com.forkexec.pts.domain.Points;
import com.forkexec.pts.domain.exceptions.*;

import javax.jws.WebService;

/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map" the Java class to the WSDL definitions.
 */
@WebService(endpointInterface = "com.forkexec.pts.ws.PointsPortType", wsdlLocation = "PointsService.wsdl", name = "PointsWebService", portName = "PointsPort", targetNamespace = "http://ws.pts.forkexec.com/", serviceName = "PointsService")
public class PointsPortImpl implements PointsPortType {

    /**
     * The Endpoint manager controls the Web Service instance during its whole
     * lifecycle.
     */
    private final PointsEndpointManager endpointManager;

    /** Constructor receives a reference to the endpoint manager. */
    public PointsPortImpl(final PointsEndpointManager endpointManager) {
	this.endpointManager = endpointManager;
    }

    // Main operations -------------------------------------------------------

    @Override
	public void addUser(final String user) throws EmailAlreadyExistsFault_Exception {
        try {
            Points.getInstance().addUser(user);
        } catch (EmailAlreadyExistsException e) {
            Faults.emailAlreadyExists(e, Faults.msgAddUser(user));
        }
    }

    @Override
    public QCint getPoints(final String user) {
        return Points.getInstance().getPoints(user);
    }

    @Override
    public QCint getStartPoints() {
        return Points.getInstance().getStartPoints();
    }

    @Override
    public void setPoints(final String user, final QCint points) {
        Points.getInstance().setPoints(user, points);
    }

    // Control operations ----------------------------------------------------

    /** Diagnostic operation to check if service is running. */
    @Override
    public String ctrlPing(String inputMessage) {
	// If no input is received, return a default name.
	if (inputMessage == null || inputMessage.trim().length() == 0)
	    inputMessage = "friend";

	// If the park does not have a name, return a default.
	String wsName = endpointManager.getWsName();
	if (wsName == null || wsName.trim().length() == 0)
	    wsName = "Park";

	// Build a string with a message to return.
	final StringBuilder builder = new StringBuilder();
	builder.append("Hello ").append(inputMessage);
	builder.append(" from ").append(wsName);
	return builder.toString();
    }

    /** Return all variables to default values. */
    @Override
    public void ctrlClear() {
        Points.getInstance().ctrlClear();
    }

    /** Set variables with specific values. */
    @Override
    public void ctrlInit(final QCint startPoints) {
        Points.getInstance().ctrlInit(startPoints);
    }
}
