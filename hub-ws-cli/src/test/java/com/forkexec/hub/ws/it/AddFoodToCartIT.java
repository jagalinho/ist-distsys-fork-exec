package com.forkexec.hub.ws.it;

import com.forkexec.hub.ws.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class AddFoodToCartIT extends BaseIT{
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


        Food food2 = new Food();

        FoodId foodId2 = new FoodId();
        foodId2.setMenuId("Barato2");
        foodId2.setRestaurantId("T28_Restaurant2");
        food2.setId(foodId2);

        food2.setEntree("Queijo");
        food2.setPlate("Nuggets");
        food2.setDessert("Queijo");
        food2.setPrice(10);
        food2.setPreparationTime(5);

        FoodInit foodinit2 = new FoodInit();
        foodinit2.setFood(food2);
        foodinit2.setQuantity(5);


        client.ctrlInitFood(Arrays.asList(foodinit1, foodinit2));
    }

    @After
    public void clean() {
        client.ctrlClear();
    }

    @Test
    public void success() throws Exception {
        FoodId foodId1 = new FoodId();
        foodId1.setMenuId("Barato");
        foodId1.setRestaurantId("T28_Restaurant1");

        client.addFoodToCart("test@test", foodId1, 2);

        FoodId foodId2 = new FoodId();
        foodId2.setMenuId("Barato2");
        foodId2.setRestaurantId("T28_Restaurant2");

        client.addFoodToCart("test@test", foodId2, 1);
    }

    @Test (expected = InvalidUserIdFault_Exception.class)
    public void nullUserId() throws Exception {
        FoodId foodId = new FoodId();
        foodId.setMenuId("Barato");
        foodId.setRestaurantId("T28_Restaurant1");

        client.addFoodToCart(null, foodId, 2);
    }

    @Test
    public void nonActivatedUserId() throws Exception {
        FoodId foodId = new FoodId();
        foodId.setMenuId("Barato");
        foodId.setRestaurantId("T28_Restaurant1");

        client.addFoodToCart("badtest@test", foodId, 2);
    }

    @Test (expected = InvalidFoodIdFault_Exception.class)
    public void nonExistingRestaurant() throws Exception {
        FoodId foodId = new FoodId();
        foodId.setMenuId("Barato");
        foodId.setRestaurantId("T28_Restauranttttttt");

        client.addFoodToCart("test@test", foodId, 1);
    }

    @Test (expected = InvalidFoodIdFault_Exception.class)
    public void nullFoodId() throws Exception {
        client.addFoodToCart("test@test", null, 2);
    }

    @Test (expected = InvalidFoodIdFault_Exception.class)
    public void invalidFoodId() throws Exception {
        FoodId foodId = new FoodId();
        foodId.setMenuId("Baratoooooooo");
        foodId.setRestaurantId("T28_Restaurant1");

        client.addFoodToCart("test@test", foodId, 1);
    }

    @Test (expected = InvalidFoodQuantityFault_Exception.class)
    public void zeroQuantity() throws Exception {
        FoodId foodId = new FoodId();
        foodId.setMenuId("Barato");
        foodId.setRestaurantId("T28_Restaurant1");

        client.addFoodToCart("test@test", foodId, 0);
    }
}
