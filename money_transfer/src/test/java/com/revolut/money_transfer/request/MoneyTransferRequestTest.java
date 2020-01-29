package com.revolut.money_transfer.request;

import org.junit.Assert;
import org.junit.Test;

public class MoneyTransferRequestTest {

	@Test
	public void testMoneyTransferRequest() {
		MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest();
		Assert.assertNotNull(moneyTransferRequest);
	}
	
	@Test
	public void testFromAccountId() {
		MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest();
		
		moneyTransferRequest.setFromAccountId(1);
		Assert.assertEquals(1, moneyTransferRequest.getFromAccountId().intValue());
	}
	
	@Test
	public void testToAccountId() {
		MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest();
		
		moneyTransferRequest.setToAccountId(2);
		Assert.assertEquals(2, moneyTransferRequest.getToAccountId().intValue());
	}
	
	@Test
	public void testAmount() {
		MoneyTransferRequest moneyTransferRequest = new MoneyTransferRequest();
		
		moneyTransferRequest.setAmount(50.0);
		Assert.assertEquals(new Double(50.0), moneyTransferRequest.getAmount());
	}
}
