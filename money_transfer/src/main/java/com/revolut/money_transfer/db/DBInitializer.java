package com.revolut.money_transfer.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.revolut.money_transfer.MoneyTransferApplication;

/**
 * 
 * This class is created to initialize the data store (H2 database), create ACCOUNT table with sample data
 * 
 * @author mayur.sharma
 *
 */
public class DBInitializer {
	
	private static final Logger LOG = Logger.getLogger(MoneyTransferApplication.class.getName());
	private static final String url = "jdbc:h2:~/testDB"; //DB_CLOSE_ON_EXIT=FALSE
    private static final String insertSQL = "INSERT INTO ACCOUNT(id, amount) VALUES"+"(?,?)";
	
	public void initiallize() {
        try {
            dropAccountTable();
            
            createAccountTable();
            
            insertAccount(1, 10000);
            insertAccount(2, 10000);
        } catch (SQLException ex) {
        	LOG.log(Level.SEVERE, ex.getMessage(), ex);
        	LOG.log(Level.SEVERE, "Cannot initiallize DB, hence exiting the system");
        	System.exit(-1);
        }

	}

	private void dropAccountTable() {

		try (Connection con = DriverManager.getConnection(url);
	             Statement stm = con.createStatement()) {
		
			String DROP_ACCOUNT_TABLE= "Drop table ACCOUNT"; 
	        stm.executeUpdate(DROP_ACCOUNT_TABLE);
		
		}catch (Exception ex) {
			LOG.log(Level.SEVERE, ex.getMessage(), ex);	
		}		
	}

	private void createAccountTable() {
		
		try (Connection con = DriverManager.getConnection(url);
	             Statement stm = con.createStatement()) {
			// AUTO_INCREMENT
			String sql = "CREATE TABLE ACCOUNT(id INT PRIMARY KEY, amount INT)";
	        stm.executeUpdate(sql);
	        LOG.info("Table ACCOUNT Created.");
		}catch(Exception ex) {
			LOG.log(Level.SEVERE, ex.getMessage(), ex);
		}
		
	}

	public void insertAccount(Integer accountId, Integer amount) throws SQLException {
		try (Connection con = DriverManager.getConnection(url);
	             PreparedStatement stm = con.prepareStatement(insertSQL)) {
			
			stm.setInt(1, accountId);
			stm.setInt(2, amount);
			stm.executeUpdate();	
		}
		
	}
	
}
