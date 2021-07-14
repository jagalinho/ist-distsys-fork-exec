package com.forkexec.rst.ws.it;

import com.forkexec.rst.ws.BadTextFault_Exception;
import com.forkexec.rst.ws.Menu;
import com.forkexec.rst.ws.MenuId;
import com.forkexec.rst.ws.MenuInit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SearchMenusIT extends BaseIT {

    @Before
    public void setup() throws Exception {

        MenuId id1 = new MenuId();
        id1.setId("Menu1");

        Menu menu1 = new Menu();
        menu1.setId(id1);
        menu1.setDessert("Cheesecake");
        menu1.setPlate("Pizza com ananás e atum"); //mau gosto nos sabemos
        menu1.setEntree("Queijo");
        menu1.setPreparationTime(20);
        menu1.setPrice(25);

        MenuInit menuInit1 = new MenuInit();
        menuInit1.setQuantity(5);
        menuInit1.setMenu(menu1);


        MenuId id2 = new MenuId();
        id2.setId("Menu2");

        Menu menu2 = new Menu();
        menu2.setId(id2);
        menu2.setDessert("Nuggets");   //
        menu2.setPlate("Douradinhos de atum"); // dont think about it, just accept it
        menu2.setEntree("Nuggets");    //
        menu2.setPreparationTime(10);
        menu2.setPrice(490);

        MenuInit menuInit2 = new MenuInit();
        menuInit2.setQuantity(5);
        menuInit2.setMenu(menu2);


        client.ctrlInit(Arrays.asList(menuInit1, menuInit2));
    }

    @After
    public void cleanup() {
        client.ctrlClear();
    }

    @Test(expected = BadTextFault_Exception.class)
    public void nullDescriptionText() throws Exception {
        client.searchMenus(null);
    }

    @Test(expected = BadTextFault_Exception.class)
    public void emptyDescriptionText() throws Exception {
        client.searchMenus("");
    }

    @Test(expected = BadTextFault_Exception.class)
    public void blankDescriptionText() throws Exception {
        client.searchMenus(" ");
    }

    @Test
    public void oneCorrespondence() throws Exception {
        Assert.assertEquals(1, client.searchMenus("Cheesecake").size());
    }

    @Test
    public void doubleCorrespondence() throws Exception {
        Assert.assertEquals(2, client.searchMenus("atum").size());
    }

}
