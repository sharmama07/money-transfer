package com.revolut.money_transfer.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.revolut.money_transfer.MoneyTransferApplication;
import com.revolut.money_transfer.db.dao.IAccountDAO;
import com.revolut.money_transfer.exception.OperationCannotBePerformedException;
import com.revolut.money_transfer.model.Account;

/**
 * Implementation to the contract that account service holds; key points to note here are
 * 
 * Transfer the amount without fail and 
 * 
 * 1. <b> Without locking (Optimistic locking) </b>
 * 2. <b> Without any chance of Deadlock </b>
 * 3. <b> Without any thread suspension </b>
 * 4. <b> With Reduced thread latency </b>
 * 
 * Amount transfered is guaranteed if the account holder have sufficient balance
 * 
 * @author mayur.sharma
 *
 */

public class AccountService implements IAccountService {
	
	private static Logger LOG = Logger.getLogger(MoneyTransferApplication.class.getName());

	@Inject
	private IAccountDAO accountDAO;

	/** -------------- Let's Talk about Debit operation while loop ---------------------
	 * 1. Run the while loop till the variable updated becomes true.
	 * 2. In while loop, 
	 * 		2.1 read the latest amount of account
	 * 		2.2 check for sufficient balance, If sufficient balance not found, whole transferAmoutnmethod exits with false value
	 * 		2.3 If sufficient balance is present, try to update the account's amount only if the account's amount is same as 
	 * 					we have read amount in #2.1. This is OPTIMISTIC LOCKING. 
	 * 		2.4 If the account's amount is same in DB as we have read in #2.1, update the amount with new amount. Returns 1 and updated become true. All good.
	 * PLEASE NOTE,the update query will not update account's amount if amount is changed by some other thread. And it will return 0.
	 * 
	 *      2.5 If the update query returns 0, i.e no record updated, 
	 *      		loop will continue (@2); read the account amount again from DB and try to update amount again using latest amount. 
	 */

	
	@Override
	public boolean transferAmount(Integer fromAccountId, Integer toAccountId, Double amount) {
		
		// Debit operation Steps
		boolean updated = false;
		try {
			while(!updated) { 
				Account fromAccount = accountDAO.getAccount(fromAccountId);
				
				if(fromAccount.getAmount()-amount < 0) {throw new OperationCannotBePerformedException("Insufficient balance!");}
				
				int recordsUpdated = accountDAO.updateAccountAmount(fromAccount.getId(), fromAccount.getAmount(), 
							fromAccount.getAmount()-amount);				
				
				updated = (recordsUpdated==1);
				
			}
		}catch (OperationCannotBePerformedException e) {
			LOG.log(Level.SEVERE, "Debit Operation cannot be performed, because " + e.getMessage());
		}
		
		if(updated) {
			// Credit operation, only if debit is successful
			updated = false;
			try {
				while(!updated) {
					Account toAccount = accountDAO.getAccount(toAccountId);
					int recordsUpdated = accountDAO.updateAccountAmount(toAccount.getId(), toAccount.getAmount(), toAccount.getAmount()+amount);
					updated = (recordsUpdated==1);
				}
			}catch (OperationCannotBePerformedException e) {
				LOG.log(Level.SEVERE, "Credit Operation cannot be performed, because " + e.getMessage());
				// revert debit transaction, if credit operation fails 
				revertDebittransaction(fromAccountId, amount);
			}
		}
		
		
		return updated;
	}

	private void revertDebittransaction(Integer fromAccountId, Double amount) {
		boolean updated = false;
		try {
			while(!updated) {
				Account fromAccount = accountDAO.getAccount(fromAccountId);
				int recordsUpdated = accountDAO.updateAccountAmount(fromAccount.getId(), fromAccount.getAmount(), 
						fromAccount.getAmount()+amount);
				updated = (recordsUpdated==1);
			}
		}catch (Exception e) {
			// TODO: handle exception
			LOG.log(Level.SEVERE, "Cannot revert debit trannsaction, inconsistency present!!");
		}
	}

	@Override
	public Account getAccount(Integer accountId) {
		Account account = null;
		try {
			account = accountDAO.getAccount(accountId);
		}catch (OperationCannotBePerformedException e) {
			LOG.log(Level.SEVERE, "Debit Operation cannot be performed, because " + e.getMessage());
		}
		return account;
	}	
	
}
