package com.forkexec.hub.ws.it;

import com.forkexec.hub.ws.InvalidCreditCardFault_Exception;
import com.forkexec.hub.ws.InvalidMoneyFault_Exception;
import com.forkexec.hub.ws.InvalidUserIdFault_Exception;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoadAccountIT extends BaseIT {

    @Before
    public void setup() throws Exception {
        client.activateAccount("test@test");
    }

    @After
    public void clean() {
        client.ctrlClear();
    }

    @Test
    public void success() throws Exception {
        client.loadAccount("test@test", 10, 	"4864756987091521");
    }

    @Test (expected = InvalidCreditCardFault_Exception.class)
    public void invalidCreditCard() throws Exception {
        client.loadAccount("test@test", 20, 	"123");
    }

    @Test (expected = InvalidMoneyFault_Exception.class)
    public void invalidMoney() throws Exception {
        client.loadAccount("test@test", 15, "4864756987091521");
    }
}
