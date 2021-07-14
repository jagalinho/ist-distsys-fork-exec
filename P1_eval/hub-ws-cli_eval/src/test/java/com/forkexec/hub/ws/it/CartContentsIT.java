package com.forkexec.hub.ws.it;

import com.forkexec.hub.ws.FoodOrderItem;
import com.forkexec.hub.ws.InvalidFoodIdFault_Exception;
import com.forkexec.hub.ws.InvalidFoodQuantityFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class CartContentsIT extends BaseIT {

    private void checkFoodOrderItemList(List<FoodOrderItem> contents){
        for(FoodOrderItem item : contents)
            switch (item.getFoodId().getMenuId()) {
                case FOOD1_MENUID:
                    assertEquals(FOOD1_QUANTITY, item.getFoodQuantity());
                    assertEquals(FOOD1_RESTAURANTID, item.getFoodId().getRestaurantId());
                    break;
                case FOOD2_MENUID:
                    assertEquals(FOOD2_QUANTITY, item.getFoodQuantity());
                    assertEquals(FOOD2_RESTAURANTID, item.getFoodId().getRestaurantId());
                    break;
                case FOOD3_MENUID:
                    assertEquals(FOOD3_QUANTITY, item.getFoodQuantity());
                    assertEquals(FOOD3_RESTAURANTID, item.getFoodId().getRestaurantId());
                    break;
                default:
                    assert false;
            }
    }

    @Test
    public void OneFoodItemTest() throws InvalidUserIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidFoodIdFault_Exception {
        client.addFoodToCart(VALID_USER, FOOD1_ID, FOOD1_QUANTITY);
        List<FoodOrderItem> contents = client.cartContents(VALID_USER);

        assertEquals(1, contents.size());
        assertEquals(FOOD1_ID.getMenuId(), contents.get(0).getFoodId().getMenuId());
        checkFoodOrderItemList(contents);
    }

    @Test
    public void TwoFoodItemTest() throws InvalidUserIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidFoodIdFault_Exception {
        client.addFoodToCart(VALID_USER, FOOD1_ID, FOOD1_QUANTITY);
        client.addFoodToCart(VALID_USER, FOOD2_ID, FOOD2_QUANTITY);
        List<FoodOrderItem> contents = client.cartContents(VALID_USER);

        assertEquals(2, contents.size());
        checkFoodOrderItemList(contents);
    }

    @Test
    public void ThreeFoodItemTest() throws InvalidUserIdFault_Exception, InvalidFoodQuantityFault_Exception, InvalidFoodIdFault_Exception {
        client.addFoodToCart(VALID_USER, FOOD1_ID, FOOD1_QUANTITY);
        client.addFoodToCart(VALID_USER, FOOD2_ID, FOOD2_QUANTITY);
        client.addFoodToCart(VALID_USER, FOOD3_ID, FOOD3_QUANTITY);
        List<FoodOrderItem> contents = client.cartContents(VALID_USER);

        assertEquals(3, contents.size());
        checkFoodOrderItemList(contents);
    }

    /* unspecified behaviour: https://piazza.com/class/jrxd1a0vx0i30?cid=167
    @Test
    public void addDuplicateFoodTest() throws InvalidFoodIdFault_Exception, InvalidUserIdFault_Exception, InvalidFoodQuantityFault_Exception {
        client.addFoodToCart(VALID_USER, FOOD2_ID, FOOD2_QUANTITY);
        client.addFoodToCart(VALID_USER, FOOD2_ID, FOOD2_QUANTITY);
        List<FoodOrderItem> contents = client.cartContents(VALID_USER);

        assertEquals(1, contents.size());
        assertEquals(FOOD2_QUANTITY + FOOD2_QUANTITY, contents.get(0).getFoodQuantity());
    }
    */

    @Test
    public void clearCartTest() throws InvalidUserIdFault_Exception {
        assertEquals(0, client.cartContents(VALID_USER).size());
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void nullUserTest() throws InvalidUserIdFault_Exception{
        client.cartContents(null);
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void emptyUserTest() throws InvalidUserIdFault_Exception{
        client.cartContents("");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void spacesUserTest() throws InvalidUserIdFault_Exception{
        client.cartContents("\t\n\r");
    }

}
