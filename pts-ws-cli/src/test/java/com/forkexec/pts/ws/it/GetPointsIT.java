package com.forkexec.pts.ws.it;

import org.junit.Assert;
import org.junit.Test;

public class GetPointsIT extends BaseIT {

    @Test
    public void success() {
        Assert.assertEquals(100, client.getPoints(EMAIL).getValue());
    }
}
