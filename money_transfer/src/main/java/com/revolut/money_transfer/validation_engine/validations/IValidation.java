package com.revolut.money_transfer.validation_engine.validations;

import com.revolut.money_transfer.request.InputRequest;

public interface IValidation<T extends InputRequest> {
	
	String getMessage();

	boolean apply(T inputRequest);

	
}
