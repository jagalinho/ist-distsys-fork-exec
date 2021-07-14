package com.forkexec.rst.ws.it;

import com.forkexec.rst.ws.BadMenuIdFault_Exception;
import com.forkexec.rst.ws.Menu;
import com.forkexec.rst.ws.MenuId;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class GetMenuIT extends BaseIT {
    private MenuId createMenuId(String id) {
        MenuId menuId = new MenuId();
        menuId.setId(id);
        return menuId;
    }

    @Test
    public void getMenu1Test() throws BadMenuIdFault_Exception {
        Menu menu = client.getMenu(createMenuId(MENU1_ID));
        assertTrue(isMenu1(menu));
    }

    @Test
    public void getMenu3Test() throws BadMenuIdFault_Exception {
        Menu menu = client.getMenu(createMenuId(MENU3_ID));
        assertTrue(isMenu3(menu));
    }

    @Test
    public void getMenu4Test() throws BadMenuIdFault_Exception {
        Menu menu = client.getMenu(createMenuId(MENU4_ID));
        assertTrue(isMenu4(menu));
    }

    @Test(expected = BadMenuIdFault_Exception.class)
    public void getUnknownMenuTest() throws BadMenuIdFault_Exception {
        client.getMenu(createMenuId(UNKNOWN_MENU_ID));
    }

    @Test(expected = BadMenuIdFault_Exception.class)
    public void getNullMenuTest() throws BadMenuIdFault_Exception {
        client.getMenu(null);
    }

    @Test(expected = BadMenuIdFault_Exception.class)
    public void getNullMenuIdTest() throws BadMenuIdFault_Exception {
        client.getMenu(createMenuId(null));
    }
}
