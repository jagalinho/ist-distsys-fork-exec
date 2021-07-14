package com.forkexec.pts.ws.it;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Class that tests Ping operation
 */
public class PingIT extends BaseIT {

	// tests
	// assertEquals(expected, actual);

	// public String ping(String x)

	@Test
	public void pingEmptyTest() {
		assertNotNull(client.ctrlPing("test"));
	}

}
