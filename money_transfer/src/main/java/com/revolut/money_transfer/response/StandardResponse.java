package com.revolut.money_transfer.response;

import com.google.gson.Gson;

public class StandardResponse {

	private String status;
	private Integer httpStatus;
	private String message;

	public StandardResponse(String status, Integer httpStatus, String message) {
		this.status = status;
		this.httpStatus = httpStatus;
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(Integer httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
	
	

}
