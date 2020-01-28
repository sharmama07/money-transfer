package com.revolut.money_transfer.response;

import org.junit.Assert;
import org.junit.Test;

public class StandardResponseTest {

	@Test
	public void testStandardResponse() {
		StandardResponse response = new StandardResponse("",0,"");
		Assert.assertNotNull(response);
	}
	
	@Test
	public void testStatus() {
		StandardResponse response = new StandardResponse("SUCCESS",200,"Operation completed");	
		Assert.assertEquals("SUCCESS", response.getStatus());
	}
	
	@Test
	public void testHttpStatus() {
		StandardResponse response = new StandardResponse("SUCCESS",200,"Operation completed");	
		Assert.assertEquals(200, response.getHttpStatus().intValue());
	}
	
	@Test
	public void testMessage() {
		StandardResponse response = new StandardResponse("SUCCESS",200,"Operation completed");	
		Assert.assertEquals("Operation completed", response.getMessage());
	}
	
}
