package com.forkexec.rst.ws;

public class Faults {
    private Faults() {
    }

    public static String msgGetMenu(String menuId) {
        return String.format("Failed to get menu %s", menuId);
    }

    public static String msgSearchMenus(String desc) {
        return String.format("Failed to search menus with description %s", desc);
    }

    public static String msgOrderMenu(String menuId, int quantity) {
        return String.format("Failed to order %d of menu %s", quantity, menuId);
    }

    public static String msgCtrlInit() {
        return "Failed to initialize menus";
    }

    public static void badMenuId(Throwable cause, String desc) throws BadMenuIdFault_Exception {
        BadMenuIdFault info = new BadMenuIdFault();
        info.setMessage(desc);
        throw new BadMenuIdFault_Exception(desc, info, cause);
    }

    public static void badText(Throwable cause, String desc) throws BadTextFault_Exception {
        BadTextFault info = new BadTextFault();
        info.setMessage(desc);
        throw new BadTextFault_Exception(desc, info, cause);
    }

    public static void badQuantity(Throwable cause, String desc) throws BadQuantityFault_Exception {
        BadQuantityFault info = new BadQuantityFault();
        info.setMessage(desc);
        throw new BadQuantityFault_Exception(desc, info, cause);
    }

    public static void insufficientQuantity(Throwable cause, String desc) throws InsufficientQuantityFault_Exception {
        InsufficientQuantityFault info = new InsufficientQuantityFault();
        info.setMessage(desc);
        throw new InsufficientQuantityFault_Exception(desc, info, cause);
    }

    public static void badInit(Throwable cause, String desc) throws BadInitFault_Exception {
        BadInitFault info = new BadInitFault();
        info.setMessage(desc);
        throw new BadInitFault_Exception(desc, info, cause);
    }
}
