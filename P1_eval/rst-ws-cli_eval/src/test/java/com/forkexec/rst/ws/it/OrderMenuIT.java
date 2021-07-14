package com.forkexec.rst.ws.it;

import com.forkexec.rst.ws.*;
import org.junit.Test;

public class OrderMenuIT extends BaseIT {
    private void orderMenu(String id, int quantity) throws BadMenuIdFault_Exception, InsufficientQuantityFault_Exception, BadQuantityFault_Exception {
        MenuId menuId = new MenuId();
        menuId.setId(id);
        MenuOrder order = client.orderMenu(menuId, quantity);
        checkMenuOrder(order, quantity, id);
    }

    @Test
    public void successfulOrderTest() throws BadMenuIdFault_Exception, InsufficientQuantityFault_Exception, BadQuantityFault_Exception {
        orderMenu(MENU1_ID, 1);
    }

    @Test
    public void successfulOrder2Test() throws BadMenuIdFault_Exception, InsufficientQuantityFault_Exception, BadQuantityFault_Exception {
        orderMenu(MENU2_ID, 1);
    }

    @Test
    public void successfulOrder3Test() throws BadMenuIdFault_Exception, InsufficientQuantityFault_Exception, BadQuantityFault_Exception {
        orderMenu(MENU3_ID, 1);
    }

    @Test
    public void successfulOrder4Test() throws BadMenuIdFault_Exception, InsufficientQuantityFault_Exception, BadQuantityFault_Exception {
        orderMenu(MENU4_ID, MENU4_QUANTITY);
    }

    @Test(expected = BadQuantityFault_Exception.class)
    public void negativeOrderTest() throws BadMenuIdFault_Exception, InsufficientQuantityFault_Exception, BadQuantityFault_Exception {
        orderMenu(MENU1_ID, -1);
    }

    @Test(expected = BadQuantityFault_Exception.class)
    public void veryNegativeOrderTest() throws BadMenuIdFault_Exception, InsufficientQuantityFault_Exception, BadQuantityFault_Exception {
        orderMenu(MENU1_ID, -100);
    }

    @Test(expected = BadQuantityFault_Exception.class)
    public void zeroOrderTest() throws BadMenuIdFault_Exception, InsufficientQuantityFault_Exception, BadQuantityFault_Exception {
        orderMenu(MENU1_ID, 0);
    }

    @Test(expected = InsufficientQuantityFault_Exception.class)
    public void orderTooBigTest() throws BadMenuIdFault_Exception, InsufficientQuantityFault_Exception, BadQuantityFault_Exception {
        orderMenu(MENU1_ID, MENU1_QUANTITY + 1);
    }

    @Test(expected = InsufficientQuantityFault_Exception.class)
    public void order4TooBigTest() throws BadMenuIdFault_Exception, InsufficientQuantityFault_Exception, BadQuantityFault_Exception {
        orderMenu(MENU4_ID, MENU4_QUANTITY+1);
    }

    @Test(expected = InsufficientQuantityFault_Exception.class)
    public void orderWayTooBigTest() throws BadMenuIdFault_Exception, InsufficientQuantityFault_Exception, BadQuantityFault_Exception {
        orderMenu(MENU1_ID, (MENU1_QUANTITY + 3) * 10);
    }

    @Test(expected = BadMenuIdFault_Exception.class)
    public void orderUnknownMenuTest() throws BadMenuIdFault_Exception, InsufficientQuantityFault_Exception, BadQuantityFault_Exception {
        orderMenu(UNKNOWN_MENU_ID, 1);
    }

    @Test(expected = BadMenuIdFault_Exception.class)
    public void orderNullMenuTest() throws BadMenuIdFault_Exception, InsufficientQuantityFault_Exception, BadQuantityFault_Exception {
        orderMenu(null, 1);
    }

    @Test(expected = BadMenuIdFault_Exception.class)
    public void orderNullMenuIdTest() throws BadMenuIdFault_Exception, InsufficientQuantityFault_Exception, BadQuantityFault_Exception {
        client.orderMenu(null, 1);
    }
}
