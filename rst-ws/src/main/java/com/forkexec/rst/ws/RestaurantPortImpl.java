package com.forkexec.rst.ws;

import java.util.List;

import javax.jws.WebService;

import com.forkexec.rst.domain.Restaurant;
import com.forkexec.rst.domain.exceptions.*;

/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map" the Java class to the WSDL definitions.
 */
@WebService(endpointInterface = "com.forkexec.rst.ws.RestaurantPortType",
    wsdlLocation = "RestaurantService.wsdl",
    name = "RestaurantWebService",
    portName = "RestaurantPort",
    targetNamespace = "http://ws.rst.forkexec.com/",
    serviceName = "RestaurantService"
)
public class RestaurantPortImpl implements RestaurantPortType {

    /**
     * The Endpoint manager controls the Web Service instance during its whole
     * lifecycle.
     */
    private RestaurantEndpointManager endpointManager;

    /**
     * Constructor receives a reference to the endpoint manager.
     */
    public RestaurantPortImpl(RestaurantEndpointManager endpointManager) {
        this.endpointManager = endpointManager;
    }

    // Main operations -------------------------------------------------------

    @Override
    public Menu getMenu(MenuId menuId) throws BadMenuIdFault_Exception {
        try {
            return Restaurant.getInstance().getMenu(menuId);
        } catch (BadMenuIdException e) {
            Faults.badMenuId(e, Faults.msgGetMenu(menuId != null ? menuId.getId() : "null"));
        }
        return null;
    }

    @Override
    public List<Menu> searchMenus(String descriptionText) throws BadTextFault_Exception {
        try {
            return Restaurant.getInstance().searchMenus(descriptionText);
        } catch (BadTextException e) {
            Faults.badText(e, Faults.msgSearchMenus(descriptionText != null ? descriptionText : "null"));
        }
        return null;
    }

    @Override
    public MenuOrder orderMenu(MenuId menuId, int quantity) throws BadMenuIdFault_Exception, BadQuantityFault_Exception, InsufficientQuantityFault_Exception {
        try {
            return Restaurant.getInstance().orderMenu(menuId, quantity);
        } catch (BadMenuIdException e) {
            Faults.badMenuId(e, Faults.msgOrderMenu(menuId != null ? menuId.getId() : "null", quantity));
        } catch (BadQuantityException e) {
            Faults.badQuantity(e, Faults.msgOrderMenu(menuId != null ? menuId.getId() : "null", quantity));
        } catch (InsufficientQuantityException e) {
            Faults.insufficientQuantity(e, Faults.msgOrderMenu(menuId != null ? menuId.getId() : "null", quantity));
        }
        return null;
    }


    // Control operations ----------------------------------------------------

    /**
     * Diagnostic operation to check if service is running.
     */
    @Override
    public String ctrlPing(String inputMessage) {
        // If no input is received, return a default name.
        if (inputMessage == null || inputMessage.trim().length() == 0)
            inputMessage = "friend";

        // If the park does not have a name, return a default.
        String wsName = endpointManager.getWsName();
        if (wsName == null || wsName.trim().length() == 0)
            wsName = "Restaurant";

        // Build a string with a message to return.
        StringBuilder builder = new StringBuilder();
        builder.append("Hello ").append(inputMessage);
        builder.append(" from ").append(wsName);
        return builder.toString();
    }

    /**
     * Return all variables to default values.
     */
    @Override
    public void ctrlClear() {
        Restaurant.getInstance().ctrlClear();
    }

    /**
     * Set variables with specific values.
     */
    @Override
    public void ctrlInit(List<MenuInit> initialMenus) throws BadInitFault_Exception {
        try {
            Restaurant.getInstance().ctrlInit(initialMenus);
        } catch (BadInitException e) {
            Faults.badInit(e, Faults.msgCtrlInit());
        }
    }
}
