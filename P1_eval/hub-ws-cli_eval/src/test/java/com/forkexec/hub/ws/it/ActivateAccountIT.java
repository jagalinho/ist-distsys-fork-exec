package com.forkexec.hub.ws.it;

import com.forkexec.hub.ws.InvalidInitFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ActivateAccountIT extends BaseIT {

    @Before
    public void pointsSetup() throws InvalidInitFault_Exception {
        client.ctrlInitUserPoints(USER_POINTS);
    }

    @Test
    public void createUserValidTest() throws InvalidUserIdFault_Exception {
        client.activateAccount(VALID_USER);
        assertEquals(USER_POINTS, client.accountBalance(VALID_USER));
    }

    @Test
    public void createUserDotValidTest() throws InvalidUserIdFault_Exception {
        String email = "sd.teste@tecnico";
        client.activateAccount(email);
        assertEquals(USER_POINTS, client.accountBalance(email));
    }

    @Test
    public void createShortUserValidTest() throws InvalidUserIdFault_Exception {
        String email = "sd@tecnico";
        client.activateAccount(email);
        assertEquals(USER_POINTS, client.accountBalance(email));
    }


    @Test(expected = InvalidUserIdFault_Exception.class)
    public void createUserDuplicateTest() throws InvalidUserIdFault_Exception {
        client.activateAccount(VALID_USER);
        client.activateAccount(VALID_USER);
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void createUserInvalidEmail1Test() throws InvalidUserIdFault_Exception {
        String email = "@tecnico.ulisboa";
        client.activateAccount(email);
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void createUserInvalidEmail2Test() throws InvalidUserIdFault_Exception {
        String email = "teste";
        client.activateAccount(email);
    }


    @Test(expected = InvalidUserIdFault_Exception.class)
    public void createUserInvalidEmail3Test() throws InvalidUserIdFault_Exception {
        String email = "teste@tecnico.";
        client.activateAccount(email);
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void createUserInvalidEmail4Test() throws InvalidUserIdFault_Exception {
        String email = "sd.@tecnico";
        client.activateAccount(email);
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void createUserInvalidEmail5Test() throws InvalidUserIdFault_Exception {
        String email = "teste@@tecnico.pt";
        client.activateAccount(email);
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void createUserInvalidEmail6Test() throws InvalidUserIdFault_Exception {
        String email = "teste@ tecnico.pt";
        client.activateAccount(email);
    }


    @Test(expected = InvalidUserIdFault_Exception.class)
    public void createUserInvalidEmail7Test() throws InvalidUserIdFault_Exception {
        client.activateAccount("#a#@&/.com");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void createUserNullEmailTest() throws InvalidUserIdFault_Exception {
        client.activateAccount(null);
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void createUserEmptyEmailTest() throws InvalidUserIdFault_Exception {
        client.activateAccount("");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void createUserEmptyEmail2Test() throws InvalidUserIdFault_Exception {
        client.activateAccount("  \t");
    }

}
