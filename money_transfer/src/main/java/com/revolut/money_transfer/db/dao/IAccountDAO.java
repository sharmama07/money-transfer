package com.revolut.money_transfer.db.dao;

import com.revolut.money_transfer.exception.OperationCannotBePerformedException;
import com.revolut.money_transfer.model.Account;

public interface IAccountDAO {

	Account getAccount(Integer accountId) throws OperationCannotBePerformedException;

	int updateAccountAmount(Integer accountId, Double currentAmount, Double newAmount) throws OperationCannotBePerformedException;
	
}
