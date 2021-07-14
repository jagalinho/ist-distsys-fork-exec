package com.forkexec.hub.ws.it;

import com.forkexec.hub.ws.InvalidUserIdFault_Exception;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AccountBalanceIT extends BaseIT {
    @Before
    public void accountSetUp() {
        try {
            client.activateAccount(VALID_USER);
        } catch (InvalidUserIdFault_Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void initialBalanceTest() throws InvalidUserIdFault_Exception {
        // initial balance as specified in the project statement
        assertEquals(DEFAULT_USER_POINTS, client.accountBalance(VALID_USER));
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void nullEmailTest() throws InvalidUserIdFault_Exception {
        client.accountBalance(null);
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void emptyEmailTest() throws InvalidUserIdFault_Exception {
        client.accountBalance("");
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void spacesEmailTest() throws InvalidUserIdFault_Exception {
        client.accountBalance("  \t");
    }

}
