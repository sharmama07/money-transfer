package com.revolut.money_transfer.module;

import com.google.inject.AbstractModule;
import com.revolut.money_transfer.MoneyTransferApplication;
import com.revolut.money_transfer.db.dao.AccountDAO;
import com.revolut.money_transfer.db.dao.IAccountDAO;
import com.revolut.money_transfer.routes.MoneyTransferRoutes;
import com.revolut.money_transfer.service.AccountService;
import com.revolut.money_transfer.service.IAccountService;

/**
 * 
 * This class is a google guice module which provides all the bindings for the dependencies in the application
 * 
 * @author mayur.sharma
 *
 */
public class MoneyTransferModule extends AbstractModule {

	@Override
	protected void configure() {
		
		bind(IAccountDAO.class).to(AccountDAO.class);
		bind(IAccountService.class).to(AccountService.class);
		bind(MoneyTransferApplication.class);
		bind(MoneyTransferRoutes.class);
		
	}

}
