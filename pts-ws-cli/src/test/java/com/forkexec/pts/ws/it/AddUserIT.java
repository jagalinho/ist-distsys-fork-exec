package com.forkexec.pts.ws.it;

import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
import org.junit.Assert;
import org.junit.Test;

public class AddUserIT extends BaseIT {

    @Test
    public void success() throws Exception {
        client.addUser(EMAIL);
        Assert.assertEquals(100, client.getPoints(EMAIL).getValue());
        Assert.assertEquals(0, client.getPoints(EMAIL).getSequence());
    }

    @Test (expected = EmailAlreadyExistsFault_Exception.class)
    public void duplicateEmails() throws Exception {
        client.addUser(EMAIL);
        client.addUser(EMAIL);
    }
}
