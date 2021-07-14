package com.forkexec.rst.ws.it;

import com.forkexec.rst.ws.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

public class OrderMenuIT extends BaseIT {

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
        client.orderMenu(null, 1);
    }

    @Test (expected = BadMenuIdFault_Exception.class)
    public void nonExistingMenuId() throws Exception{
        MenuId id = new MenuId();
        id.setId("Bad Menu");

        client.orderMenu(id, 0);
    }

    @Test (expected = BadQuantityFault_Exception.class)
    public void zeroQuantityOrdered() throws Exception{
        MenuId id = new MenuId();
        id.setId("Menu");

        client.orderMenu(id, 0);
    }

    @Test (expected = InsufficientQuantityFault_Exception.class)
    public void OverExistingQuantityOrdered() throws Exception {
        MenuId id = new MenuId();
        id.setId("Menu");

        client.orderMenu(id, 6);
    }

    @Test
    public void MaxQuantityMenu() throws Exception{
        MenuId id = new MenuId();
        id.setId("Menu");

        Assert.assertEquals(5, client.orderMenu(id, 5).getMenuQuantity());
    }

    @Test
    public void MinQuantityMenu() throws Exception{
        MenuId id = new MenuId();
        id.setId("Menu");

        Assert.assertEquals(1, client.orderMenu(id, 1).getMenuQuantity());
    }

}
