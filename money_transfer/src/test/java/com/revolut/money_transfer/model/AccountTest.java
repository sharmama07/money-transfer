package com.revolut.money_transfer.model;

import org.junit.Assert;
import org.junit.Test;

public class AccountTest {

	@Test
	public void testAccount() {
		Account account = new Account();
		Assert.assertNotNull(account);
		
		account = new Account(1, 100.0);
		Assert.assertNotNull(account);
		Assert.assertNotNull(account.getId());
		Assert.assertNotNull(account.getAmount());		
	}
	
	@Test
	public void testAccountId() {
		Account account = new Account();
		Assert.assertEquals(null, account.getId());
		
		account.setId(1);
		Assert.assertEquals(1, account.getId().intValue());
	}
	
	@Test
	public void testAccountAmount() {
		Account account = new Account();
		Assert.assertEquals(null, account.getAmount());
		
		account.setAmount(10.0);
		Assert.assertEquals(10.0, account.getAmount().doubleValue(), 0.0);
	}
	
}
