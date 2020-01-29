package com.revolut.money_transfer.validation_engine.validations;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.revolut.money_transfer.request.MoneyTransferRequest;

public class AmountValidation implements IValidation<MoneyTransferRequest> {

	private static final Logger LOG = Logger.getLogger(AmountValidation.class.getName());
	
	private String message = "" ;

	@Override
	public String getMessage() {
		return message ;
	}

	@Override
	public boolean apply(MoneyTransferRequest inputRequest) {
		Double amount = inputRequest.getAmount();
		
		if(amount!=null && amount>0.0) {
			return true;
		}
		addMessage("Amount cannot be negative or zero");
		LOG.log(Level.SEVERE,"Amount cannot be negative or zero");
		return false;
	}

	private void addMessage(String message) {
		this.message = message;
	}

}
