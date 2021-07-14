package com.forkexec.hub.ws.it;

import com.forkexec.hub.ws.InvalidCreditCardFault_Exception;
import com.forkexec.hub.ws.InvalidInitFault_Exception;
import com.forkexec.hub.ws.InvalidMoneyFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class LoadAccountIT extends BaseIT {
    @Before
    public void accountSetUp() {
        try {
            client.ctrlInitUserPoints(USER_POINTS);
            client.activateAccount(VALID_USER);
        } catch (InvalidUserIdFault_Exception|InvalidInitFault_Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void independentAccountsTest() throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception {
        client.activateAccount(VALID_USER2);
        client.loadAccount(VALID_USER, 10, VALID_CC);
        assertEquals(USER_POINTS + 1000, client.accountBalance(VALID_USER));
        assertEquals(USER_POINTS, client.accountBalance(VALID_USER2));
    }

    @Test
    public void add10EurosTest() throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception {
        client.loadAccount(VALID_USER, 10, VALID_CC2);
        assertEquals(USER_POINTS + 1000, client.accountBalance(VALID_USER));
    }

    @Test
    public void add20EurosTest() throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception {
        client.loadAccount(VALID_USER, 20, VALID_CC3);
        assertEquals(USER_POINTS + 2100, client.accountBalance(VALID_USER));
    }

    @Test
    public void add30EurosTest() throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception {
        client.loadAccount(VALID_USER, 30, VALID_CC4);
        assertEquals(USER_POINTS + 3300, client.accountBalance(VALID_USER));
    }

    @Test
    public void add50EurosTest() throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception {
        client.loadAccount(VALID_USER, 50, VALID_CC5);
        assertEquals(USER_POINTS + 5500, client.accountBalance(VALID_USER));
    }

    @Test
    public void noPaymentTest() throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception {
        try {
            client.loadAccount(VALID_USER, 0, VALID_CC);
            fail();
        } catch (InvalidMoneyFault_Exception e) {
            assertEquals(USER_POINTS, client.accountBalance(VALID_USER));
        }
    }

    @Test
    public void negativeMoneyTest() throws InvalidCreditCardFault_Exception, InvalidUserIdFault_Exception {
        try {
            client.loadAccount(VALID_USER, -10, VALID_CC);
            fail();
        } catch (InvalidMoneyFault_Exception e) {
            assertEquals(USER_POINTS, client.accountBalance(VALID_USER));
        }
    }

    @Test
    public void invalidMoneyTest() throws InvalidCreditCardFault_Exception, InvalidUserIdFault_Exception {
        try {
            client.loadAccount(VALID_USER, 90, VALID_CC);
            fail();
        } catch (InvalidMoneyFault_Exception e) {
            assertEquals(USER_POINTS, client.accountBalance(VALID_USER));
        }
    }

    @Test(expected = InvalidUserIdFault_Exception.class)
    public void nullUserIdTest() throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception {
        client.loadAccount(null, 10, VALID_CC);
    }

    @Test(expected = InvalidCreditCardFault_Exception.class)
    public void invalidCreditCardTest() throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception {
        client.loadAccount(VALID_USER, 10, INVALID_CC);
    }

    @Test(expected = InvalidCreditCardFault_Exception.class)
    public void invalidCreditCard2Test() throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception {
        client.loadAccount(VALID_USER, 10, "1");
    }

    @Test(expected = InvalidCreditCardFault_Exception.class)
    public void invalidCreditCard3Test() throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception {
        client.loadAccount(VALID_USER, 10, "hio3jhejhsdf");
    }

    @Test(expected = InvalidCreditCardFault_Exception.class)
    public void nullCreditCardTest() throws InvalidUserIdFault_Exception, InvalidCreditCardFault_Exception, InvalidMoneyFault_Exception {
        client.loadAccount(VALID_USER, 10, null);
    }
}
