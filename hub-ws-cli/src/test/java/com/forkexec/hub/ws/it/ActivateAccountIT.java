package com.forkexec.hub.ws.it;

import com.forkexec.hub.ws.InvalidUserIdFault_Exception;
import org.junit.After;
import org.junit.Test;

public class ActivateAccountIT extends BaseIT {

    @After
    public void clean() {
        client.ctrlClear();
    }

    @Test
    public void success() throws Exception {
        client.activateAccount("test@mail");
    }

    @Test (expected = InvalidUserIdFault_Exception.class)
    public void duplicateMailActivateAccount() throws Exception {
        client.activateAccount("test@mail");
        client.activateAccount("test@mail");
    }

    @Test (expected = InvalidUserIdFault_Exception.class)
    public void invalidEmail() throws Exception {
        client.activateAccount("tes t-mai l");
    }

    @Test (expected = InvalidUserIdFault_Exception.class)
    public void nullEmail() throws Exception {
        client.activateAccount(null);
    }
}
