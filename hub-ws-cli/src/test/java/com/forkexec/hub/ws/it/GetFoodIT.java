package com.forkexec.hub.ws.it;

import com.forkexec.hub.ws.Food;
import com.forkexec.hub.ws.FoodId;
import com.forkexec.hub.ws.FoodInit;
import com.forkexec.hub.ws.InvalidFoodIdFault_Exception;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class GetFoodIT extends BaseIT {
    @Before
    public void setup() throws Exception{
        client.activateAccount("test@test");

        Food food1 = new Food();

        FoodId foodId1 = new FoodId();
        foodId1.setMenuId("Barato");
        foodId1.setRestaurantId("T28_Restaurant1");
        food1.setId(foodId1);

        food1.setEntree("Nuggets");
        food1.setPlate("Nuggets");
        food1.setDessert("Nuggets");
        food1.setPrice(2);
        food1.setPreparationTime(10);

        FoodInit foodinit1 = new FoodInit();
        foodinit1.setFood(food1);
        foodinit1.setQuantity(5);

        client.ctrlInitFood(Arrays.asList(foodinit1));
    }

    @After
    public void clean() {
        client.ctrlClear();
    }

    @Test
    public void success() throws Exception {
        FoodId foodId = new FoodId();
        foodId.setMenuId("Barato");
        foodId.setRestaurantId("T28_Restaurant1");

        Food food = client.getFood(foodId);

        Assert.assertEquals("Barato", food.getId().getMenuId());
        Assert.assertEquals("T28_Restaurant1", food.getId().getRestaurantId());
        Assert.assertEquals("Nuggets", food.getEntree());
        Assert.assertEquals("Nuggets", food.getPlate());
        Assert.assertEquals("Nuggets", food.getDessert());
        Assert.assertEquals(2, food.getPrice());
        Assert.assertEquals(10, food.getPreparationTime());
    }

    @Test (expected = InvalidFoodIdFault_Exception.class)
    public void invalidFoodId() throws Exception {
        FoodId foodId = new FoodId();
        foodId.setMenuId("Baratoooooooo");
        foodId.setRestaurantId("T28_Restaurant1");

        client.getFood(foodId);
    }

    @Test (expected = InvalidFoodIdFault_Exception.class)
    public void nonExistingRestaurant() throws Exception {
        FoodId foodId = new FoodId();
        foodId.setMenuId("Barato");
        foodId.setRestaurantId("T28_Restauranttttttt");

        client.getFood(foodId);
    }

    @Test (expected = InvalidFoodIdFault_Exception.class)
    public void nullFoodId() throws Exception {
        client.getFood(null);
    }

}
