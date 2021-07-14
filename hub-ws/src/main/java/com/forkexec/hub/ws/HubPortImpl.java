package com.forkexec.hub.ws;

import java.util.Comparator;
import java.util.List;

import javax.jws.WebService;

import com.forkexec.hub.domain.Hub;
import com.forkexec.hub.domain.exceptions.*;

/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map" the Java class to the WSDL definitions.
 */
@WebService(endpointInterface = "com.forkexec.hub.ws.HubPortType",
            wsdlLocation = "HubService.wsdl",
            name ="HubWebService",
            portName = "HubPort",
            targetNamespace="http://ws.hub.forkexec.com/",
            serviceName = "HubService"
)
public class HubPortImpl implements HubPortType {

	/**
	 * The Endpoint manager controls the Web Service instance during its whole
	 * lifecycle.
	 */
	private HubEndpointManager endpointManager;

	/** Constructor receives a reference to the endpoint manager. */
	public HubPortImpl(HubEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}
	
	// Main operations -------------------------------------------------------
	
	@Override
	public void activateAccount(String userEmail) throws InvalidUserIdFault_Exception {
		try {
			Hub.getInstance().activateAccount(userEmail);
		} catch (InvalidUserIdException e) {
			Faults.invalidUserId(e, Faults.msgActivateAccount(userEmail));
		}
	}

	@Override
	public void loadAccount(String userEmail, int moneyToAdd, String creditCardNumber) throws InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception, InvalidUserIdFault_Exception {
		try {
			Hub.getInstance().loadAccount(userEmail, moneyToAdd, creditCardNumber);
		} catch (InvalidCreditCardException e) {
			Faults.invalidCreditCard(e, Faults.msgLoadAccount(userEmail, moneyToAdd));
		} catch (InvalidMoneyException e) {
			Faults.invalidMoney(e, Faults.msgLoadAccount(userEmail, moneyToAdd));
		} catch (InvalidUserIdException e) {
			Faults.invalidUserId(e, Faults.msgLoadAccount(userEmail, moneyToAdd));
		}
	}
	
	@Override
	public List<Food> searchDeal(String description) throws InvalidTextFault_Exception {
		try {
			return Hub.getInstance().searchMenus(description, Comparator.comparingInt(Food::getPrice));
		} catch (InvalidTextException e) {
			Faults.invalidText(e, Faults.msgSearch(description));
		}
		return null;
	}
	
	@Override
	public List<Food> searchHungry(String description) throws InvalidTextFault_Exception {
		try {
			return Hub.getInstance().searchMenus(description, Comparator.comparingInt(Food::getPreparationTime));
		} catch (InvalidTextException e) {
			Faults.invalidText(e, Faults.msgSearch(description));
		}
		return null;
	}

	
	@Override
	public void addFoodToCart(String userEmail, FoodId foodId, int foodQuantity) throws InvalidFoodIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidUserIdFault_Exception {
		try {
			Hub.getInstance().addFoodToCart(userEmail, foodId, foodQuantity);
		} catch (InvalidFoodIdException e) {
			Faults.invalidFoodId(e, Faults.msgAddFoodToCart(userEmail, foodId != null ? foodId.getMenuId() : "null", foodId != null ? foodId.getRestaurantId() : "null", foodQuantity));
		} catch (InvalidFoodQuantityException e) {
			Faults.invalidFoodQuantity(e, Faults.msgAddFoodToCart(userEmail, foodId != null ? foodId.getMenuId() : "null", foodId != null ? foodId.getRestaurantId() : "null", foodQuantity));
		} catch (InvalidUserIdException e) {
			Faults.invalidUserId(e, Faults.msgAddFoodToCart(userEmail, foodId != null ? foodId.getMenuId() : "null", foodId != null ? foodId.getRestaurantId() : "null", foodQuantity));
		}
	}

	@Override
	public void clearCart(String userEmail) throws InvalidUserIdFault_Exception {
		try {
			Hub.getInstance().clearCart(userEmail);
		} catch (InvalidUserIdException e) {
			Faults.invalidUserId(e, Faults.msgClearCart(userEmail));
		}
	}

	@Override
	public FoodOrder orderCart(String userEmail) throws EmptyCartFault_Exception, InvalidUserIdFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception {
		try {
			return Hub.getInstance().orderCart(userEmail);
		} catch (EmptyCartException e) {
			Faults.emptyCart(e, Faults.msgOrderCart(userEmail));
		} catch (InvalidUserIdException e) {
			Faults.invalidUserId(e, Faults.msgOrderCart(userEmail));
		} catch (NotEnoughPointsException e) {
			Faults.notEnoughPoints(e, Faults.msgOrderCart(userEmail));
		} catch (InvalidFoodQuantityException e) {
			Faults.invalidFoodQuantity(e, Faults.msgOrderCart(userEmail));
		}
		return null;
	}

	@Override
	public int accountBalance(String userEmail) throws InvalidUserIdFault_Exception {
	    try {
	    	return Hub.getInstance().accountBalance(userEmail);
		} catch (InvalidUserIdException e) {
			Faults.invalidUserId(e, Faults.msgAccountBalance(userEmail));
		}
		return 0;
	}

	@Override
	public Food getFood(FoodId foodId) throws InvalidFoodIdFault_Exception {
		try {
			return Hub.getInstance().getFood(foodId);
		} catch (InvalidFoodIdException e) {
			Faults.invalidFoodId(e, Faults.msgGetFood(foodId != null ? foodId.getMenuId() : "null", foodId != null ? foodId.getRestaurantId() : "null"));
		}
		return null;
	}

	@Override
	public List<FoodOrderItem> cartContents(String userEmail) throws InvalidUserIdFault_Exception {
		try {
			return Hub.getInstance().cartContents(userEmail);
		} catch (InvalidUserIdException e) {
			Faults.invalidUserId(e, Faults.msgCartContents(userEmail));
		}
		return null;
	}

	// Control operations ----------------------------------------------------

	/** Diagnostic operation to check if service is running. */
	@Override
	public String ctrlPing(String inputMessage) {
		// If no input is received, return a default name.
		if (inputMessage == null || inputMessage.trim().length() == 0)
			inputMessage = "friend";

		// If the service does not have a name, return a default.
		String wsName = endpointManager.getWsName();
		if (wsName == null || wsName.trim().length() == 0)
			wsName = "Hub";

		// Build a string with a message to return.
		StringBuilder builder = new StringBuilder();
		builder.append("Hello ").append(inputMessage);
		builder.append(" from ").append(wsName);
		return builder.toString();
	}

	/** Return all variables to default values. */
	@Override
	public void ctrlClear() {
		Hub.getInstance().ctrlClear();
	}

	/** Set variables with specific values. */
	@Override
	public void ctrlInitFood(List<FoodInit> initialFoods) throws InvalidInitFault_Exception {
		try {
			Hub.getInstance().ctrlInitFood(initialFoods);
		} catch (InvalidInitException e) {
			Faults.invalidInit(e, Faults.msgCtrlInitFood());
		}
	}
	
	@Override
	public void ctrlInitUserPoints(int startPoints) throws InvalidInitFault_Exception {
		try {
			Hub.getInstance().ctrlInitUserPoints(startPoints);
		} catch (InvalidInitException e) {
			Faults.invalidInit(e, Faults.msgCtrlInitUserPoints(startPoints));
		}
	}
}
