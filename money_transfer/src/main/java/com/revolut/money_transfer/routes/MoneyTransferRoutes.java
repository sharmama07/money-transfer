package com.revolut.money_transfer.routes;

import static spark.Spark.get;
import static spark.Spark.post;

import org.apache.http.HttpStatus;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.revolut.money_transfer.request.MoneyTransferRequest;
import com.revolut.money_transfer.response.StandardResponse;
import com.revolut.money_transfer.service.IAccountService;

/**
 * 
 * This class is created to initialize all the routes (REST API end points) which the application expose.
 * Here two routes are exposed,
 * 1. getAccount(accountId) : Return @Account using accountId
 * 2. transferAmount(fromAccountId, toAccountId, amount) : Return @StandardResponse 
 * 
 * @author mayur.sharma
 *
 */
public class MoneyTransferRoutes {

	@Inject
	private IAccountService accountService;
	
	public void initiallize() {
		
		// get account using account id
		get("/account/:id", (request, response) -> {
			Integer accountId = Integer.parseInt(request.params(":id"));
			response.type("application/json");
			return accountService.getAccount(accountId);
		});
		
		// money transfer url
		post("/money-transfer", (request, response) -> {
			MoneyTransferRequest moneyTransferRequest = new Gson().fromJson(request.body(), MoneyTransferRequest.class);
			
			boolean res = accountService.transferAmount(moneyTransferRequest.getFromAccountId(), 
						moneyTransferRequest.getToAccountId(), moneyTransferRequest.getAmount()*1.0);
			
			
			response.type("application/json");
			StandardResponse responeFromAPI;
			if(res) {
				response.status(200);
				responeFromAPI = new StandardResponse("Success", HttpStatus.SC_OK, "Money Transfer successful");
			} else {
				response.status(500);
				responeFromAPI = new StandardResponse("Failure", HttpStatus.SC_INTERNAL_SERVER_ERROR, "Money Transfer failed");
			}
			return new Gson().toJson(responeFromAPI);
		});
		
		
	}
	
}
