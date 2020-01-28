package com.revolut.money_transfer;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.revolut.money_transfer.db.DBInitializer;
import com.revolut.money_transfer.model.Account;
import com.revolut.money_transfer.request.MoneyTransferRequest;
import com.revolut.money_transfer.response.StandardResponse;

import spark.Spark;

public class MoneyTransferFunctionalTest {
	
	private DBInitializer dbInitializer;

	@BeforeClass
	public static void testMainApp() {
		MoneyTransferApplication.main(new String[2]);
	}
	
	@Before
	public void setup() {
		dbInitializer = new DBInitializer();
	}

	@AfterClass
	public static void tearDown() {
		Spark.stop();
	}
	

	@Test
	public void testGetAccount() throws ClientProtocolException, IOException, SQLException {
		
		dbInitializer.insertAccount(3,  1000);
		
		HttpUriRequest request = new HttpGet("http://localhost:4567/account/" + 3);
		 
	    // When
	    HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
	    
	    String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());
	    Account account = new Gson().fromJson(jsonFromResponse, Account.class);
	    
	    System.out.println("Account amount "+ account.getAmount());
	    Assert.assertEquals(200, httpResponse.getStatusLine().getStatusCode());
	    Assert.assertEquals(3, account.getId().intValue());
	    Assert.assertTrue(account.getAmount()>=0.0);
	    Assert.assertEquals("application/json", ContentType.getOrDefault(httpResponse.getEntity()).getMimeType());
	}
		
	@Test
	public void testTransferAmountWithOneRequest() throws ClientProtocolException, IOException, SQLException {
		
		dbInitializer.insertAccount(4,  1000);
		dbInitializer.insertAccount(5,  1000);
		
		HttpPost request = new HttpPost("http://localhost:4567/money-transfer");
		request.addHeader("Content-Type", "application/json");
		MoneyTransferRequest reqBody = new MoneyTransferRequest(4,5,50);
		request.setEntity(new StringEntity(reqBody.toString()));
		 
	    // When
	    HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
	 
	    Assert.assertEquals(200, httpResponse.getStatusLine().getStatusCode());
	    
	    String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());
	    StandardResponse response = new Gson().fromJson(jsonFromResponse, StandardResponse.class);
	    
	    Assert.assertEquals("Success", response.getStatus());
	    Assert.assertEquals(200, response.getHttpStatus().intValue());
	    Assert.assertEquals("Money Transfer successful", response.getMessage());
	    Assert.assertEquals("application/json", ContentType.getOrDefault(httpResponse.getEntity()).getMimeType());
	    
	    Account acc4 = getAccount(4);Account acc5 = getAccount(5);
	    Assert.assertEquals(950, acc4.getAmount().intValue());
	    Assert.assertEquals(1050, acc5.getAmount().intValue());

	}	
	
	@Test
	public void testTransferAmountWithOneSideTransactionFlowAndFullAmountTransferedWithMultipleThreads() throws ClientProtocolException, IOException, InterruptedException, SQLException {
		
		dbInitializer.insertAccount(6,  1000);
		dbInitializer.insertAccount(7,  1000);
		
		Integer numberOfTransferRequest = 10;
		
		Thread [] threads = new Thread[numberOfTransferRequest];
		for(int i=0 ; i<numberOfTransferRequest ; i++) {
			threads[i] = getNewTransaction(6,7,100);
		}
		long start = System.currentTimeMillis();
		for(int i=0 ; i<numberOfTransferRequest ; i++) {
			threads[i].start();
		}

		for(int i=0 ; i<numberOfTransferRequest ; i++) {
			threads[i].join();
		}
		
		System.out.println("Total time taken to process "+numberOfTransferRequest+" request to transfer "+ numberOfTransferRequest*100+" amount : " +
										(System.currentTimeMillis() - start));
		
	    Account acc6 = getAccount(6);Account acc7 = getAccount(7);
	    Assert.assertEquals(0, acc6.getAmount().intValue());
	    Assert.assertEquals(2000, acc7.getAmount().intValue());		
	}
	
	@Test
	public void testTransferAmountWithThreeAccounts() throws ClientProtocolException, IOException, InterruptedException, SQLException {
		
		dbInitializer.insertAccount(8, 1000);
		dbInitializer.insertAccount(9, 1000);
		dbInitializer.insertAccount(10, 1000);
		
		Integer numberOfTransferRequest = 10;
		
		Thread [] threads = new Thread[numberOfTransferRequest];
		for(int i =0;i<numberOfTransferRequest; i++) {
			if(i%2==0) {
				threads[i] = getNewTransaction(8,9,10);
			} else { 
				threads[i] = getNewTransaction(9,10,10);
			}
			
		}
		long start = System.currentTimeMillis();
		for(int i =0;i<numberOfTransferRequest; i++) {
			threads[i].start();
		}

		for(int i =0;i<numberOfTransferRequest; i++) {
			threads[i].join();
		}
		
		System.out.println("Total time to transfer amount within 3 accounts : " + (System.currentTimeMillis() - start));
		
	    Account acc8 = getAccount(8);Account acc9 = getAccount(9);Account acc10 = getAccount(10);
	    Assert.assertEquals(950, acc8.getAmount().intValue());
	    Assert.assertEquals(1000, acc9.getAmount().intValue());
	    Assert.assertEquals(1050, acc10.getAmount().intValue());
	}
	
	@Test
	public void testTransferAmountWithThreeAccountsInACircle() throws ClientProtocolException, IOException, InterruptedException, SQLException {
		
		dbInitializer.insertAccount(11, 1000);
		dbInitializer.insertAccount(12, 1000);
		dbInitializer.insertAccount(13, 1000);
		
		Integer numberOfTransferRequest = 10;
		
		Thread [] threads = new Thread[numberOfTransferRequest];
		
		for(int i =0;i<numberOfTransferRequest; i++) {
			if(i%2==0) { // 5 threads 0,2,4,6,8
				threads[i] = getNewTransaction(11,12,10);
			} else if(i%3 ==0) { // 2 threads 3, 9
				threads[i] = getNewTransaction(12,13,10);
			} else { // 3 threads 1, 5, 7
				threads[i] = getNewTransaction(13,11,10);
			}
			
		}
		
		long start = System.currentTimeMillis();
		for(int i =0;i<numberOfTransferRequest; i++) {
			threads[i].start();
		}

		for(int i =0;i<numberOfTransferRequest; i++) {
			threads[i].join();
		}
		
		System.out.println("Total time to transfer amount within 3 accounts in a circle : " + (System.currentTimeMillis() - start));
		
	    Account acc11 = getAccount(11);Account acc12 = getAccount(12);Account acc13 = getAccount(13);
	    Assert.assertEquals(980, acc11.getAmount().intValue());
	    Assert.assertEquals(1030, acc12.getAmount().intValue());
	    Assert.assertEquals(990, acc13.getAmount().intValue());
	}
	
	private Thread getNewTransaction(Integer fromAccountId, Integer toAccountId, Integer amount) {
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					HttpPost request = new HttpPost("http://localhost:4567/money-transfer");
					request.addHeader("Content-Type", "application/json");
					MoneyTransferRequest reqBody = new MoneyTransferRequest(fromAccountId,toAccountId,amount);
					request.setEntity(new StringEntity(reqBody.toString()));
					 
				    // When
				    HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
				 
				    Assert.assertEquals(200, httpResponse.getStatusLine().getStatusCode());
				    
				    String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());
				    StandardResponse response = new Gson().fromJson(jsonFromResponse, StandardResponse.class);
				    
				    Assert.assertEquals("Success", response.getStatus());
				    Assert.assertEquals(200, response.getHttpStatus().intValue());
				    Assert.assertEquals("Money Transfer successful", response.getMessage());
				    Assert.assertEquals("application/json", ContentType.getOrDefault(httpResponse.getEntity()).getMimeType());
				}catch(Exception e) {
					System.out.println("Error occurred " + e.getMessage());
				}
			}
		};
		
		return t;
	}
	
	private Account getAccount(Integer accountId) throws ClientProtocolException, IOException, SQLException {
		
		HttpUriRequest request = new HttpGet("http://localhost:4567/account/" + accountId);
		 
	    // When
	    HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );
	    
	    String jsonFromResponse = EntityUtils.toString(httpResponse.getEntity());
	    return new Gson().fromJson(jsonFromResponse, Account.class);
}

	
	
}
