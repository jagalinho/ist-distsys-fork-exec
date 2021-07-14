package com.forkexec.cc.sw.it;

import org.junit.Assert;
import org.junit.Test;

public class ValidateNumberIT extends BaseIT {

    @Test
    public void success() {
        Assert.assertTrue(client.validateNumber("4864756987091521"));
    }

    @Test
    public void invalidNumber() {
        Assert.assertFalse(client.validateNumber("123"));
    }

}
