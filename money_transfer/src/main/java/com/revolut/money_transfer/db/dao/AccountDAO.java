package com.revolut.money_transfer.db.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revolut.money_transfer.exception.OperationCannotBePerformedException;
import com.revolut.money_transfer.model.Account;

/**
 * 
 * Data access object for Account entity
 * 
 * @author mayur.sharma
 *
 */
public class AccountDAO implements IAccountDAO {

	private static final String url = "jdbc:h2:~/testDB";
	
	private static final Logger LOG = LoggerFactory.getLogger(AccountDAO.class);
	
	private Connection connection;
	
	public AccountDAO() throws SQLException {
		connection = DriverManager.getConnection(url);
	}
	
	@Override
	public Account getAccount(Integer accountId) throws OperationCannotBePerformedException {
		String SQL = "select id, amount from ACCOUNT where id="+accountId+"";
		
		try(Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(SQL)){
			if (rs.next()) {
				return new Account(rs.getInt(1), rs.getDouble(2));
			}
			return null;
		} catch (SQLException e) {
			LOG.error("Cannot retrieve account from DB, reason: "+ e.getMessage());
			throw new OperationCannotBePerformedException("Cannot retrieve account from DB, reason: "+ e.getMessage(), e);
		}
		
	}

	/**
	 * OPTIMISTIC locking implementation
	 * Try to Update the account's amount. 
	 * 
	 * Update the account's amount only if account's amount in DB is same as @currentAmount passed in parameter (check where clause)
	 * If some other thread has changed the account's amount, so the amount in DB will not match with @currentAmount and hence does not update and return 0.
	 */
	@Override
	public int updateAccountAmount(Integer accountId, Double currentAmount, Double newAmount) throws OperationCannotBePerformedException {
		
		String SQL = "update ACCOUNT set amount=" + newAmount +" where id="+accountId+" and amount="+currentAmount+"";
		
		try(Statement statement = connection.createStatement()){
			int numberOfUpdates = statement.executeUpdate(SQL);
			return numberOfUpdates;
		} catch (SQLException e) {
			LOG.error("Cannot update account amount, reason: "+ e.getMessage());
			throw new OperationCannotBePerformedException("Cannot update account amount, reason: "+ e.getMessage(), e);
		}
	}

}
