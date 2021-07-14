package com.forkexec.hub.ws.it;

import com.forkexec.hub.ws.Food;
import com.forkexec.hub.ws.InvalidTextFault_Exception;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SearchHungryIT extends BaseIT {

    @Test
    public void searchOneResultTest() throws InvalidTextFault_Exception {
        List<Food> menus = client.searchHungry(FOOD2_DESSERT);
        assertEquals(1, menus.size());
        assertTrue(isFood2(menus.get(0)));
    }

    @Test
    public void searchOnePlateTest() throws InvalidTextFault_Exception {
        List<Food> menus = client.searchHungry(FOOD1_PLATE_WORD);
        assertEquals(1, menus.size());
        assertTrue(isFood1(menus.get(0)));
    }

    @Test
    public void searchThreeResultsTest() throws InvalidTextFault_Exception {
        /* sorted by prep time, asc */
        List<Food> foods = client.searchHungry(COMMON_DESCRIPTION);
        assertEquals(3, foods.size());
        Food food3 = foods.get(0);
        Food food1 = foods.get(1);
        Food food2 = foods.get(2);
        assertTrue(isFood1(food1));
        assertTrue(isFood2(food2));
        assertTrue(isFood3(food3));
    }

    @Test
    public void searchNoResultsTest() throws InvalidTextFault_Exception {
        List<Food> menus = client.searchDeal(UNKNOWN_DESCRIPTION);
        assertEquals(0, menus.size());
    }

    @Test(expected = InvalidTextFault_Exception.class)
    public void searchEmptyDescriptionTest() throws InvalidTextFault_Exception {
        client.searchDeal("");
    }

    @Test(expected = InvalidTextFault_Exception.class)
    public void searchSpacesInDescriptionTest() throws InvalidTextFault_Exception {
        client.searchDeal("     ");
    }

    @Test(expected = InvalidTextFault_Exception.class)
    public void searchSpacesAndWordsInDescriptionTest() throws InvalidTextFault_Exception {
        /* "o texto de pesquisa nao pode ser vazio nem conter espacos" */
        client.searchDeal(" "+COMMON_DESCRIPTION);
    }

    @Test(expected = InvalidTextFault_Exception.class)
    public void searchTabsInDescriptionTest() throws InvalidTextFault_Exception {
        client.searchDeal("\t");
    }

    @Test(expected = InvalidTextFault_Exception.class)
    public void searchNewlineInDescriptionTest() throws InvalidTextFault_Exception {
        client.searchDeal("\n");
    }
}
