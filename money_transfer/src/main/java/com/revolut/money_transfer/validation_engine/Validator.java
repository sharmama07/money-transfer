package com.revolut.money_transfer.validation_engine;

import java.util.List;

import com.revolut.money_transfer.request.InputRequest;
import com.revolut.money_transfer.request.MoneyTransferRequest;
import com.revolut.money_transfer.validation_engine.validations.IValidation;

public abstract class Validator<T extends InputRequest> implements IValidator<MoneyTransferRequest> {

	@Override
	public boolean validate(MoneyTransferRequest inputRequest) {
		
		List<IValidation<MoneyTransferRequest>> validations = getValidations();
		
		return validations.stream().allMatch(validation -> validation.apply(inputRequest));
		
	}

	protected abstract List<IValidation<MoneyTransferRequest>> getValidations();
	
}
