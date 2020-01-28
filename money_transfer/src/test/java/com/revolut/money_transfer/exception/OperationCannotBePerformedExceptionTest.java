package com.revolut.money_transfer.exception;

import org.junit.Assert;
import org.junit.Test;

public class OperationCannotBePerformedExceptionTest {

	@Test
	public void testOperationCannotBePerformedException() {
		OperationCannotBePerformedException exc = new OperationCannotBePerformedException("Operation Not Permitted");
		Assert.assertNotNull(exc);
		Assert.assertNotNull(exc.getMessage());
	}
	
	@Test
	public void testExceptionMessage() {
		OperationCannotBePerformedException exc = new OperationCannotBePerformedException("Operation Not Permitted");
		Assert.assertEquals("Operation Not Permitted", exc.getMessage());
	}
	
	@Test
	public void testExceptionMessageWithThrowable() {
		OperationCannotBePerformedException exc = new OperationCannotBePerformedException("Operation Not Permitted", new Exception());
		Assert.assertNotNull(exc.getCause());
		Assert.assertEquals("Operation Not Permitted", exc.getMessage());
	}
	
}
