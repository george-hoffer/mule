/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.example.loanbroker.bpm;

import org.mule.example.loanbroker.AbstractLoanBrokerApp;
//import org.mule.tck.util.MuleDerbyTestUtils;

/**
 * Executes the LoanBroker BPM example.  This is a standalone app which assumes Mule is running somewhere.
 */
public class LoanBrokerApp extends AbstractLoanBrokerApp
{
    /*
    public LoanBrokerApp(String config) throws Exception
    {
        super(config);
    }
    */

    public LoanBrokerApp() throws Exception
    {
        super();
    }    
    
    public static void main(String[] args) throws Exception
    {
        //String configFile = "loan-broker-bpm-mule-config.xml";
    /*
        if (args != null && args.length > 0 
            // This is a hack for MULE-4082 which assumes that if the parameter is a 
            // Mule config file, it will contain a "." in the name
            && args[0].contains("."))
        {
            configFile = args[0];
        }
    */
        //LoanBrokerApp loanBrokerApp = new LoanBrokerApp(configFile);
        LoanBrokerApp loanBrokerApp = new LoanBrokerApp();
        loanBrokerApp.run(false);
    }

    @Override
    protected void init() throws Exception
    {
        super.init();
    }
}
