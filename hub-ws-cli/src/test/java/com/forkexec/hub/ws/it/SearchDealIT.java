package com.forkexec.hub.ws.it;

import com.forkexec.hub.ws.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;


public class SearchDealIT extends BaseIT {

    @Before
    public void setup() throws  Exception {
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
        List<Food> food = client.searchDeal("Nuggets");

        Assert.assertEquals(2, food.size());

        Assert.assertEquals("Barato", food.get(0).getId().getMenuId());
        Assert.assertEquals("T28_Restaurant1", food.get(0).getId().getRestaurantId());
        Assert.assertEquals("Barato2", food.get(1).getId().getMenuId());
        Assert.assertEquals("T28_Restaurant2", food.get(1).getId().getRestaurantId());
    }

    @Test (expected = InvalidTextFault_Exception.class)
    public void nullTextDescription() throws Exception {
        client.searchDeal(null);
    }

    @Test (expected = InvalidTextFault_Exception.class)
    public void invalidTextDescription() throws Exception {
        client.searchDeal("Nuggets ");
    }

    @Test (expected = InvalidTextFault_Exception.class)
    public void emptyTextDescription() throws Exception {
        client.searchDeal("");
    }
}
