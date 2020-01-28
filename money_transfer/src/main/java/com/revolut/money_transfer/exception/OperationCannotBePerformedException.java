package com.revolut.money_transfer.exception;

@SuppressWarnings("serial")
public class OperationCannotBePerformedException extends Exception {

	private final String message;

	public OperationCannotBePerformedException(String message) {
		super(message);
		this.message = message;
	}

	public OperationCannotBePerformedException(String message, Throwable throwable) {
		super(message, throwable);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
