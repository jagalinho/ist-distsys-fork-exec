package com.forkexec.hub.ws;

public class Faults {
    private Faults() {
    }

    public static String msgActivateAccount(String userEmail) {
        return String.format("Failed to activate account %s", userEmail);
    }

    public static String msgLoadAccount(String userEmail, int moneyToAdd) {
        return String.format("Failed to load account %s with %d (EUR)", userEmail, moneyToAdd);
    }

    public static String msgSearch(String description) {
        return String.format("Failed to search deal with description %s", description);
    }

    public static String msgAddFoodToCart(String userEmail, String menuId, String restaurantId, int foodQuantity) {
        return String.format("Failed to add %d of menu %s from %s to cart of user %s", foodQuantity, menuId, restaurantId, userEmail);
    }

    public static String msgClearCart(String userEmail) {
        return String.format("Failed to clear cart of user %s", userEmail);
    }

    public static String msgOrderCart(String userEmail) {
        return String.format("Failed to order cart of user %s", userEmail);
    }

    public static String msgAccountBalance(String userEmail) {
        return String.format("Failed to check balance of user %s", userEmail);
    }

    public static String msgGetFood(String menuId, String restaurantId) {
        return String.format("Failed to get menu %s from %s", menuId, restaurantId);
    }

    public static String msgCartContents(String userEmail) {
        return String.format("Failed to check cart contents of user %s", userEmail);
    }

    public static String msgCtrlInitFood() {
        return "Failed to initialize foods";
    }

    public static String msgCtrlInitUserPoints(int startPoints) {
        return String.format("Failed to set default initial balance to %d points", startPoints);
    }

    public static void invalidUserId(Throwable cause, String desc) throws InvalidUserIdFault_Exception {
        InvalidUserIdFault info = new InvalidUserIdFault();
        info.setMessage(desc);
        throw new InvalidUserIdFault_Exception(desc, info, cause);
    }

    public static void invalidCreditCard(Throwable cause, String desc) throws InvalidCreditCardFault_Exception {
        InvalidCreditCardFault info = new InvalidCreditCardFault();
        info.setMessage(desc);
        throw new InvalidCreditCardFault_Exception(desc, info, cause);
    }

    public static void invalidMoney(Throwable cause, String desc) throws InvalidMoneyFault_Exception {
        InvalidMoneyFault info = new InvalidMoneyFault();
        info.setMessage(desc);
        throw new InvalidMoneyFault_Exception(desc, info, cause);
    }

    public static void invalidText(Throwable cause, String desc) throws InvalidTextFault_Exception {
        InvalidTextFault info = new InvalidTextFault();
        info.setMessage(desc);
        throw new InvalidTextFault_Exception(desc, info, cause);
    }

    public static void invalidFoodId(Throwable cause, String desc) throws InvalidFoodIdFault_Exception {
        InvalidFoodIdFault info = new InvalidFoodIdFault();
        info.setMessage(desc);
        throw new InvalidFoodIdFault_Exception(desc, info, cause);
    }

    public static void invalidFoodQuantity(Throwable cause, String desc) throws InvalidFoodQuantityFault_Exception {
        InvalidFoodQuantityFault info = new InvalidFoodQuantityFault();
        info.setMessage(desc);
        throw new InvalidFoodQuantityFault_Exception(desc, info, cause);
    }

    public static void emptyCart(Throwable cause, String desc) throws EmptyCartFault_Exception {
        EmptyCartFault info = new EmptyCartFault();
        info.setMessage(desc);
        throw new EmptyCartFault_Exception(desc, info, cause);
    }

    public static void notEnoughPoints(Throwable cause, String desc) throws NotEnoughPointsFault_Exception {
        NotEnoughPointsFault info = new NotEnoughPointsFault();
        info.setMessage(desc);
        throw new NotEnoughPointsFault_Exception(desc, info, cause);
    }

    public static void invalidInit(Throwable cause, String desc) throws InvalidInitFault_Exception {
        InvalidInitFault info = new InvalidInitFault();
        info.setMessage(desc);
        throw new InvalidInitFault_Exception(desc, info, cause);
    }
}
