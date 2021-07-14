package com.forkexec.hub.ws.it;

import com.forkexec.hub.ws.Food;
import com.forkexec.hub.ws.InvalidFoodIdFault_Exception;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class GetFoodIT extends BaseIT {

    @Test(expected = InvalidFoodIdFault_Exception.class)
    public void nullFoodIdTest() throws InvalidFoodIdFault_Exception {
        client.getFood(null);
    }

    @Test(expected = InvalidFoodIdFault_Exception.class)
    public void nullMenuIdTest() throws InvalidFoodIdFault_Exception {
        client.getFood(buildFoodId(null, FOOD1_RESTAURANTID));
    }

    @Test(expected = InvalidFoodIdFault_Exception.class)
    public void nullRestaurantIdTest() throws InvalidFoodIdFault_Exception {
        client.getFood(buildFoodId(FOOD1_MENUID, null));
    }

    @Test(expected = InvalidFoodIdFault_Exception.class)
    public void emptyMenuIdTest() throws InvalidFoodIdFault_Exception {
        client.getFood(buildFoodId("", FOOD1_RESTAURANTID));
    }

    @Test(expected = InvalidFoodIdFault_Exception.class)
    public void emptyRestaurantIdTest() throws InvalidFoodIdFault_Exception {
        client.getFood(buildFoodId(FOOD1_MENUID, ""));
    }

    @Test
    public void getFoodIdTest() throws InvalidFoodIdFault_Exception {
        Food getFood1 = client.getFood(buildFoodId(FOOD1_MENUID, FOOD1_RESTAURANTID));
        assertTrue(isFood1(getFood1));
    }
}
