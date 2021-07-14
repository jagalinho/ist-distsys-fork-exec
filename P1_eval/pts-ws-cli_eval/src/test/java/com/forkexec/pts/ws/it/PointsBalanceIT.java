package com.forkexec.pts.ws.it;

import com.forkexec.pts.ws.BadInitFault_Exception;
import com.forkexec.pts.ws.EmailAlreadyExistsFault_Exception;
import com.forkexec.pts.ws.InvalidEmailFault_Exception;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PointsBalanceIT extends BaseIT {
    @Before
    public void setUp() throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception, BadInitFault_Exception {
        client.ctrlInit(USER_POINTS);
        client.activateUser(VALID_USER);
    }

    @After
    public void tearDown() {
        pointsTestClear();
    }

    @Test
    public void initialBalanceTest() throws InvalidEmailFault_Exception {
        assertEquals(USER_POINTS, client.pointsBalance(VALID_USER));
    }

    @Test(expected = InvalidEmailFault_Exception.class)
    public void unknownUserTest() throws InvalidEmailFault_Exception {
        client.pointsBalance(UNKNOWN_USER);
    }

    @Test(expected = InvalidEmailFault_Exception.class)
    public void nullEmailTest() throws InvalidEmailFault_Exception {
        client.pointsBalance(null);
    }

}
