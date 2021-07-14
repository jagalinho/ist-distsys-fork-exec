package com.forkexec.hub.ws.it;

import com.forkexec.hub.ws.InvalidFoodIdFault_Exception;
import com.forkexec.hub.ws.InvalidFoodQuantityFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;
import org.junit.Test;

public class AddFoodToCartIT extends BaseIT {
    @Test
    public void addOneFoodItemTest() throws InvalidFoodIdFault_Exception, InvalidUserIdFault_Exception, InvalidFoodQuantityFault_Exception {
        client.addFoodToCart(VALID_USER, FOOD1_ID, 2);
    }
    
    @Test
    public void addDuplicateFoodTest() throws InvalidFoodIdFault_Exception, InvalidUserIdFault_Exception, InvalidFoodQuantityFault_Exception {
        client.addFoodToCart(VALID_USER, FOOD2_ID, 1);
        client.addFoodToCart(VALID_USER, FOOD2_ID, 1);
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void nullUserFoodTest() throws InvalidFoodIdFault_Exception, InvalidUserIdFault_Exception, InvalidFoodQuantityFault_Exception {
        client.addFoodToCart(null, FOOD1_ID, 1);
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void emptyUserFoodTest() throws InvalidFoodIdFault_Exception, InvalidUserIdFault_Exception, InvalidFoodQuantityFault_Exception {
        client.addFoodToCart("", FOOD1_ID, 1);
    }

    @Test(expected = InvalidFoodIdFault_Exception.class)
    public void nullFoodIdTest() throws InvalidFoodIdFault_Exception, InvalidUserIdFault_Exception, InvalidFoodQuantityFault_Exception {
        client.addFoodToCart(VALID_USER, null, 1);
    }

    @Test(expected = InvalidFoodIdFault_Exception.class)
    public void nullMenuIdTest() throws InvalidFoodIdFault_Exception, InvalidUserIdFault_Exception, InvalidFoodQuantityFault_Exception {
        client.addFoodToCart(VALID_USER, buildFoodId(null, FOOD1_RESTAURANTID), 1);
    }

    @Test(expected = InvalidFoodIdFault_Exception.class)
    public void nullRestaurantIdTest() throws InvalidFoodIdFault_Exception, InvalidUserIdFault_Exception, InvalidFoodQuantityFault_Exception {
        client.addFoodToCart(VALID_USER, buildFoodId(FOOD1_MENUID, null), 1);
    }

    @Test(expected = InvalidFoodIdFault_Exception.class)
    public void emptyMenuIdTest() throws InvalidFoodIdFault_Exception, InvalidUserIdFault_Exception, InvalidFoodQuantityFault_Exception {
        client.addFoodToCart(VALID_USER, buildFoodId("", FOOD1_RESTAURANTID), 1);
    }

    @Test(expected = InvalidFoodIdFault_Exception.class)
    public void emptyRestaurantIdTest() throws InvalidFoodIdFault_Exception, InvalidUserIdFault_Exception, InvalidFoodQuantityFault_Exception {
        client.addFoodToCart(VALID_USER, buildFoodId(FOOD1_MENUID, ""), 1);
    }

    @Test(expected = InvalidFoodQuantityFault_Exception.class)
    public void negativeFoodQuantityTest() throws InvalidFoodIdFault_Exception, InvalidUserIdFault_Exception, InvalidFoodQuantityFault_Exception {
        client.addFoodToCart(VALID_USER, FOOD1_ID, -1);
    }

    @Test(expected = InvalidFoodQuantityFault_Exception.class)
    public void addVeryNegativeFoodQuantityTest() throws InvalidFoodIdFault_Exception, InvalidUserIdFault_Exception, InvalidFoodQuantityFault_Exception {
        client.addFoodToCart(VALID_USER, FOOD1_ID, -100);
    }
}
