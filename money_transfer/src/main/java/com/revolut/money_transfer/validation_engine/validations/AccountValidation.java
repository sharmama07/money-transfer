package com.revolut.money_transfer.validation_engine.validations;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.revolut.money_transfer.db.dao.IAccountDAO;
import com.revolut.money_transfer.exception.OperationCannotBePerformedException;
import com.revolut.money_transfer.request.MoneyTransferRequest;

public class AccountValidation implements IValidation<MoneyTransferRequest> {

	private static Logger LOG = Logger.getLogger(AccountValidation.class.getName());
		
	private String message = "";

	private IAccountDAO accountDAO;
	
	public AccountValidation(IAccountDAO accountDAO) {
		this.accountDAO = accountDAO;
	}

	@Override
	public boolean apply(MoneyTransferRequest inputRequest) {
		Integer fromAccountId = inputRequest.getFromAccountId();
		Integer toAccountId = inputRequest.getToAccountId();
		
		if(fromAccountId == null) {
			addMessage("FromAccount cannot be null");
			return false;
		}
		if(toAccountId == null) {
			addMessage("ToAccountId cannot be null");
			return false;
		}

		if(fromAccountId.equals(toAccountId)) {
			addMessage("FromAccountId cannot be equal to ToAccountId");
			return false;
		}
		
		try {
			return accountDAO.getAccount(fromAccountId)!=null && accountDAO.getAccount(toAccountId)!=null;
		} catch (OperationCannotBePerformedException e) {
			LOG.log(Level.SEVERE,"Cannot fetch account from DB", e.getMessage());
			addMessage("Cannot fetch account from DB, Error Message"+ e.getMessage());
			return false;
		} 
		
	}

	private void addMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}

}
