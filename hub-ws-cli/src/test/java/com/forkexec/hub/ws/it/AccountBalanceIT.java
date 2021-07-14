package com.forkexec.hub.ws.it;

import com.forkexec.hub.ws.InvalidUserIdFault_Exception;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AccountBalanceIT extends BaseIT {

    @Before
    public void setup() throws Exception {
        client.activateAccount("test@mail");
    }

    @After
    public void clean() {
        client.ctrlClear();
    }

    @Test
    public void success() throws Exception {
        Assert.assertEquals(100, client.accountBalance("test@mail"));
    }

    @Test (expected = InvalidUserIdFault_Exception.class)
    public void nullUser() throws Exception {
        client.accountBalance(null);
    }
}
