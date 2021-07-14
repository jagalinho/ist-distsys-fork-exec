package com.forkexec.rst.ws.it;

import com.forkexec.rst.ws.BadTextFault_Exception;
import com.forkexec.rst.ws.Menu;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SearchMenusIT extends BaseIT {

    @Test
    public void searchOneResultTest() throws BadTextFault_Exception {
        List<Menu> menus = client.searchMenus(MENU2_ENTREE);
        assertEquals(1, menus.size());
        assertTrue(isMenu2(menus.get(0)));
    }

    @Test
    public void searchTwoResultsTest() throws BadTextFault_Exception {
        List<Menu> menus = client.searchMenus(SHARED_DESSERT);
        assertEquals(2, menus.size());
        Menu menu1 = menus.get(0);
        Menu menu2 = menus.get(1);
        if (isMenu1(menu1)) {
            assertTrue(isMenu3(menu2));
        } else {
            assertTrue(isMenu3(menu1));
            assertTrue(isMenu1(menu2));
        }
    }

    @Test
    public void searchTwoResultsInDifferentFieldsTest() throws BadTextFault_Exception {
        List<Menu> menus = client.searchMenus(SHARED_WORD);
        assertEquals(2, menus.size());
        Menu menu1 = menus.get(0);
        Menu menu2 = menus.get(1);
        if (isMenu4(menu1)) {
            assertTrue(isMenu2(menu2));
        } else {
            assertTrue(isMenu2(menu1));
            assertTrue(isMenu4(menu2));
        }
    }

    @Test
    public void searchPartialPlateTest() throws BadTextFault_Exception {
        List<Menu> menus = client.searchMenus(MENU4_PLATE_PARTIAL);
        assertEquals(1, menus.size());
        assertTrue(isMenu4(menus.get(0)));
    }

    @Test
    public void searchNoResultsTest() throws BadTextFault_Exception {
        List<Menu> menus = client.searchMenus(UNKNOWN_MENU_ENTREE);
        assertEquals(0, menus.size());
    }

    @Test(expected = BadTextFault_Exception.class)
    public void searchEmptyDescriptionTest() throws BadTextFault_Exception {
        client.searchMenus("");
    }

    @Test(expected = BadTextFault_Exception.class)
    public void searchSpacesInDescriptionTest() throws BadTextFault_Exception {
        client.searchMenus("     ");
    }

    @Test(expected = BadTextFault_Exception.class)
    public void searchTabsInDescriptionTest() throws BadTextFault_Exception {
        client.searchMenus("\t");
    }

    @Test(expected = BadTextFault_Exception.class)
    public void searchNewlineInDescriptionTest() throws BadTextFault_Exception {
        client.searchMenus("\n");
    }

    @Test(expected = BadTextFault_Exception.class)
    public void searchLineFeedInDescriptionTest() throws BadTextFault_Exception {
        client.searchMenus("\r");
    }
}
