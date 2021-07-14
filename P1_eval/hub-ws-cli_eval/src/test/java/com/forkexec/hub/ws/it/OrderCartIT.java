package com.forkexec.hub.ws.it;

import com.forkexec.hub.ws.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrderCartIT extends BaseIT {
    @Before
    public void orderCartSetUp() throws InvalidUserIdFault_Exception, InvalidInitFault_Exception {
        client.ctrlInitUserPoints(FOOD1_PRICE);
        client.activateAccount(VALID_USER);
    }

    @Test
    public void orderCartOneItemTest() throws InvalidFoodIdFault_Exception, InvalidUserIdFault_Exception, InvalidFoodQuantityFault_Exception, EmptyCartFault_Exception, NotEnoughPointsFault_Exception {
        client.addFoodToCart(VALID_USER, FOOD1_ID, 1);
        FoodOrder order = client.orderCart(VALID_USER);
        assertNotNull(order.getFoodOrderId());
        assertNotNull(order.getFoodOrderId().getId());
        assertFalse(order.getFoodOrderId().getId().trim().isEmpty());
        assertEquals(1, order.getItems().size());
        assertEquals(FOOD1_ID.getMenuId(), order.getItems().get(0).getFoodId().getMenuId());
        assertEquals(FOOD1_ID.getRestaurantId(), order.getItems().get(0).getFoodId().getRestaurantId());
        assertEquals(1, order.getItems().get(0).getFoodQuantity());
        assertEquals(0, client.accountBalance(VALID_USER));
    }


    @Test
    public void orderTwoCartsTest() throws InvalidFoodIdFault_Exception, InvalidUserIdFault_Exception,
            InvalidFoodQuantityFault_Exception, EmptyCartFault_Exception, NotEnoughPointsFault_Exception,
            InvalidInitFault_Exception {
        client.ctrlInitUserPoints((FOOD2_QUANTITY+1)*FOOD2_PRICE);
        client.activateAccount(VALID_USER2);

        client.addFoodToCart(VALID_USER2, FOOD2_ID, FOOD2_QUANTITY-1);
        FoodOrder order1 = client.orderCart(VALID_USER2);
        assertNotNull(order1.getFoodOrderId());
        assertNotNull(order1.getFoodOrderId().getId());
        assertFalse(order1.getFoodOrderId().getId().trim().isEmpty());
        assertEquals(1, order1.getItems().size());
        assertEquals(FOOD2_ID.getMenuId(), order1.getItems().get(0).getFoodId().getMenuId());
        assertEquals(FOOD2_ID.getRestaurantId(), order1.getItems().get(0).getFoodId().getRestaurantId());
        assertEquals(1, order1.getItems().get(0).getFoodQuantity());
        assertEquals(FOOD2_PRICE*2, client.accountBalance(VALID_USER2));

        client.addFoodToCart(VALID_USER2, FOOD2_ID, 1);
        FoodOrder order2 = client.orderCart(VALID_USER2);
        assertNotNull(order2.getFoodOrderId());
        assertNotNull(order2.getFoodOrderId().getId());
        assertFalse(order2.getFoodOrderId().getId().trim().isEmpty());
        assertEquals(1, order2.getItems().size());
        assertEquals(FOOD2_ID.getMenuId(), order2.getItems().get(0).getFoodId().getMenuId());
        assertEquals(FOOD2_ID.getRestaurantId(), order2.getItems().get(0).getFoodId().getRestaurantId());
        assertEquals(1, order2.getItems().get(0).getFoodQuantity());
        assertEquals(FOOD2_PRICE, client.accountBalance(VALID_USER2));

        assertNotEquals(order1.getFoodOrderId().getId(), order2.getFoodOrderId().getId());
        /* this makes sense but it's kind of not specified...
        try {
            client.addFoodToCart(VALID_USER2, FOOD2_ID, 1);
            FoodOrder impossibleOrder = client.orderCart(VALID_USER2);
            fail("ordered food without stock");
        } catch (InvalidFoodQuantityFault_Exception e) {
            assertEquals(FOOD2_PRICE, client.accountBalance(VALID_USER2));
        }
        */
    }

    @Test(expected = EmptyCartFault_Exception.class)
    public void orderEmptyCartTest() throws InvalidUserIdFault_Exception, EmptyCartFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception {
        client.orderCart(VALID_USER);
    }

    @Test(expected = NotEnoughPointsFault_Exception.class)
    public void orderExpensiveCartTest() throws InvalidFoodIdFault_Exception, InvalidUserIdFault_Exception, InvalidFoodQuantityFault_Exception, EmptyCartFault_Exception, NotEnoughPointsFault_Exception {
        client.addFoodToCart(VALID_USER, FOOD1_ID, 1);
        client.addFoodToCart(VALID_USER, FOOD2_ID, 1);
        client.orderCart(VALID_USER);
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void nullUserTest() throws InvalidUserIdFault_Exception, EmptyCartFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception {
        client.orderCart(null);
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void emptyUserTest() throws InvalidUserIdFault_Exception, EmptyCartFault_Exception, NotEnoughPointsFault_Exception, InvalidFoodQuantityFault_Exception {
        client.orderCart("");
    }
}
