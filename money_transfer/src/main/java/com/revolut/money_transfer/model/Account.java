package com.revolut.money_transfer.model;

import com.google.gson.Gson;

public class Account {
	
	private Integer id;
	
	private Double amount;

	public Account() {}
	
	public Account(Integer id, Double amount ) {
		this.id = id;
		this.amount = amount;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
