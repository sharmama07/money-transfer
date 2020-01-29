package com.revolut.money_transfer.request;

import com.google.gson.Gson;

public class MoneyTransferRequest {
	
	private Integer fromAccountId;
	
	private Integer toAccountId;
	
	private Double amount;
	
	public MoneyTransferRequest() {
		super();
	}
	
	public MoneyTransferRequest(Integer fromAccountId, Integer toAccountId, Double amount) {
		this.fromAccountId = fromAccountId;
		this.toAccountId = toAccountId;
		this.amount = amount;
	}

	public Integer getFromAccountId() {
		return fromAccountId;
	}

	public void setFromAccountId(Integer fromAccountId) {
		this.fromAccountId = fromAccountId;
	}

	public Integer getToAccountId() {
		return toAccountId;
	}

	public void setToAccountId(Integer toAccountId) {
		this.toAccountId = toAccountId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
	

}
