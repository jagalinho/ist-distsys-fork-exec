package com.forkexec.hub.ws.it;

import com.forkexec.hub.ws.InvalidUserIdFault_Exception;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ClearCartIT extends BaseIT {

    @After
    public void clean() {
        client.ctrlClear();
    }

    @Before
    public void setup() throws Exception {
        client.activateAccount("test@mail");
    }

    @Test
    public void success() throws Exception{
        client.clearCart("test@mail");
    }

    @Test (expected = InvalidUserIdFault_Exception.class)
    public void nullUser() throws Exception {
        client.clearCart(null);
    }

    @Test
    public void nonActivatedUser() throws Exception {
        client.clearCart("wtv@wtv.org");
    }
}
