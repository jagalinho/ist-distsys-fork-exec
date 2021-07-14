package com.forkexec.pts.ws.it;

import com.forkexec.pts.ws.QCint;
import org.junit.Assert;
import org.junit.Test;

public class SetPointsIT extends BaseIT {

    @Test
    public void success() {
        QCint qc = new QCint();
        qc.setValue(50);
        qc.setSequence(3);
        client.setPoints(EMAIL, qc);
        Assert.assertEquals(50, client.getPoints(EMAIL).getValue());
        Assert.assertEquals(3, client.getPoints(EMAIL).getSequence());
    }
}
