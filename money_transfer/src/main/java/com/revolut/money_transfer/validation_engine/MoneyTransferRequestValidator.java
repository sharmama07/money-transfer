package com.revolut.money_transfer.validation_engine;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.revolut.money_transfer.db.dao.IAccountDAO;
import com.revolut.money_transfer.request.MoneyTransferRequest;
import com.revolut.money_transfer.validation_engine.validations.AccountValidation;
import com.revolut.money_transfer.validation_engine.validations.IValidation;

public class MoneyTransferRequestValidator extends Validator<MoneyTransferRequest> {

	@Inject
	private IAccountDAO accountDAO;
	
	@Override
	protected List<IValidation<MoneyTransferRequest>> getValidations() {
		List<IValidation<MoneyTransferRequest>> validations = new ArrayList<>(1);
		validations.add(new AccountValidation(accountDAO));
		return validations;
	}

}
