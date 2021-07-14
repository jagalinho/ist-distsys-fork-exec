package com.forkexec.rst.ws.it;

import com.forkexec.rst.ws.BadMenuIdFault_Exception;
import com.forkexec.rst.ws.Menu;
import com.forkexec.rst.ws.MenuId;
import com.forkexec.rst.ws.MenuInit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

public class GetMenuIT extends BaseIT {

    @Before
    public void setup() throws Exception {
        MenuId id = new MenuId();
        id.setId("Menu");

        Menu menu = new Menu();
        menu.setId(id);
        menu.setDessert("Cheesecake");
        menu.setPlate("Pizza com ananás e atum"); //mau gosto nos sabemos
        menu.setEntree("Queijo");
        menu.setPreparationTime(20);
        menu.setPrice(25);

        MenuInit menuInit = new MenuInit();
        menuInit.setQuantity(5);
        menuInit.setMenu(menu);


        client.ctrlInit(Collections.singletonList(menuInit));
    }

    @After
    public void cleanup() {
        client.ctrlClear();
    }

    @Test (expected = BadMenuIdFault_Exception.class)
    public void nullMenuId() throws Exception{
        client.getMenu(null);
    }

    @Test (expected = BadMenuIdFault_Exception.class)
    public void nonExistingMenuId() throws Exception{
        MenuId id = new MenuId();
        id.setId("bad Menu");

        client.getMenu(id);
    }

    @Test
    public void gettingMenu() throws Exception {
        MenuId id = new MenuId();
        id.setId("Menu");

        Assert.assertEquals("Menu", client.getMenu(id).getId().getId());
    }
}
