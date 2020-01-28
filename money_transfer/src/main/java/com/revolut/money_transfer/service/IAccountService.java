package com.revolut.money_transfer.service;

import com.revolut.money_transfer.model.Account;

/**
 * This is a contract that account service holds
 * 
 * @author mayur.sharma
 *
 */
public interface IAccountService {

	boolean transferAmount(Integer fromAccount, Integer toAccount, Double amount);

	Account getAccount(Integer accountId);
	
}
