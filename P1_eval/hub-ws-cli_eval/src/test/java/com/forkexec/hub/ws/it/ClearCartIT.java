package com.forkexec.hub.ws.it;

import com.forkexec.hub.ws.InvalidFoodIdFault_Exception;
import com.forkexec.hub.ws.InvalidFoodQuantityFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClearCartIT extends BaseIT {
    @Test
    public void clearEmptyCartTest() throws InvalidUserIdFault_Exception {
        assertEquals(0, client.cartContents(VALID_USER).size());
        client.clearCart(VALID_USER);
        assertEquals(0, client.cartContents(VALID_USER).size());
    }

    @Test
    public void clearOccupiedCartTest() throws InvalidFoodIdFault_Exception, InvalidUserIdFault_Exception, InvalidFoodQuantityFault_Exception {
        client.addFoodToCart(VALID_USER, FOOD1_ID, 1);
        assertEquals(1, client.cartContents(VALID_USER).size());
        client.clearCart(VALID_USER);
        assertEquals(0, client.cartContents(VALID_USER).size());
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void clearNullUserCartTest() throws InvalidUserIdFault_Exception {
        client.clearCart(null);
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void clearEmptyUserCartTest() throws InvalidUserIdFault_Exception {
        client.clearCart("");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void clearUserSpacesCartTest() throws InvalidUserIdFault_Exception {
        client.clearCart("\t ");
    }

}
