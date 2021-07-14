package com.forkexec.pts.ws.it;

import com.forkexec.pts.ws.QCint;
import org.junit.Assert;
import org.junit.Test;

public class GetStartPointsIT extends BaseIT {

    @Test
    public void success() {
        Assert.assertEquals(100, client.getStartPoints().getValue());
        Assert.assertEquals(0, client.getStartPoints().getSequence());

        QCint qc = new QCint();
        qc.setValue(50);
        qc.setSequence(3);
        client.ctrlInit(qc);
        Assert.assertEquals(50, client.getStartPoints().getValue());
        Assert.assertEquals(3, client.getStartPoints().getSequence());
    }
}
