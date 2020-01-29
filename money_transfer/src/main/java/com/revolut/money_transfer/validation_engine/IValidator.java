package com.revolut.money_transfer.validation_engine;

public interface IValidator<T> {
	
	boolean validate(T inputRequest);

}
