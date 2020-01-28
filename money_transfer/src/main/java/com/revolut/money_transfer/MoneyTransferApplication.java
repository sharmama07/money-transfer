package com.revolut.money_transfer;

import java.util.logging.Logger;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.revolut.money_transfer.db.DBInitializer;
import com.revolut.money_transfer.module.MoneyTransferModule;
import com.revolut.money_transfer.routes.MoneyTransferRoutes;

/**
 * 
 * Money Transfer is a stand alone multi-threaded Java application to transfer amount from one account to another
 * This class is the main class which performs following operation in order
 * 1. Initialize google guice injector to create dependency graph as per the guice module @MoneyTransferModule
 * 2. Initialize DB table and sample data
 * 3. Create REST APIs routes to 
 * 		3.1 Transfer amount between two accounts, and 
 * 		3.2 Get account data 
 * 
 * Technologies & References
 * 1) Web server - Embedded Jetty Server URL http://localhost:4567/
 * 		SparkJava is used to create quick server and exposing REST API end points 
 * 2) Core java, Google guice, maven
 * 3) Database : H2 in memory database (URL jdbc:h2:~/testDB)
 * 4) Testing using JUnit
 * 
 * @author mayur.sharma
 *
 */
public class MoneyTransferApplication 
{
	@Inject
	private DBInitializer dbInitiallizer;
	@Inject
	private MoneyTransferRoutes routes;
	
	private static Logger LOG = Logger.getLogger(MoneyTransferApplication.class.getName());
	
    public static void main( String[] args )
    {
     
    	LOG.info(" Initiallizing Guice injector");
        Injector injector = Guice.createInjector(new MoneyTransferModule());
        LOG.info(" Initiallized Guice injector");
      
        MoneyTransferApplication application = injector.getInstance(MoneyTransferApplication.class);
        
        application.doStart();
                
    }
    
    private void doStart() {
    	LOG.info(" Initiallizing DB Tables");
    	dbInitiallizer.initiallize();
    	LOG.info(" Initiallized DB Tables");
    	
    	LOG.info(" Initiallizing Routes");
    	routes.initiallize();
    	LOG.info(" Initiallized Routes");
    	
    }
}
