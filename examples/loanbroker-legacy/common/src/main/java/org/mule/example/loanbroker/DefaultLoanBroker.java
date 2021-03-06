/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.example.loanbroker;

import org.mule.example.loanbroker.messages.CreditProfile;
import org.mule.example.loanbroker.messages.Customer;
import org.mule.example.loanbroker.messages.CustomerQuoteRequest;
import org.mule.example.loanbroker.messages.LoanBrokerQuoteRequest;
import org.mule.example.loanbroker.messages.LoanQuote;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <code>LoanBroker</code> is the Service that starts the loan request process. The
 * broker also receives the final quote.
 */
public class DefaultLoanBroker implements LoanBrokerService
{
    /**
     * logger used by this class
     */
    protected final Log logger = LogFactory.getLog(getClass());

    private final AtomicInteger quotes = new AtomicInteger(0);
    private final AtomicInteger requests = new AtomicInteger(0);
    private final AtomicInteger profiles = new AtomicInteger(0);

    @Override
    public Object getLoanQuote(CustomerQuoteRequest request) throws LoanBrokerException
    {
        int requestNumber = incRequests();
        if (logger.isInfoEnabled())
        {
            String[] params = new String[] {
                String.valueOf(requestNumber),
                request.getCustomer().getName(),
                String.valueOf(request.getCustomer().getSsn()),
                String.valueOf(request.getLoanAmount()),
                String.valueOf(request.getLoanDuration())
            };

            logger.info("\n***** " + LocaleMessage.receivedRequest(params));
        }
        return request;
    }

    public LoanBrokerQuoteRequest receiveLoanBrokerQuoteRequest(LoanBrokerQuoteRequest request)
    {
        // Just pass through
        return request;
    }

    public Customer receiveCustomer(Customer customer)
    {
        // Just pass through
        return customer;
    }

    public Object receiveCreditProfile(CreditProfile profile)
    {
        int profileNumber = incProfiles();
        if (logger.isInfoEnabled())
        {
            String[] params = new String[] {
                String.valueOf(profileNumber),
                String.valueOf(profile.getCreditScore()),
                String.valueOf(profile.getCreditHistory())
            };

            logger.info("\n***** " + LocaleMessage.receivedProfile(params));
        }
        return profile;
    }

    @Override
    public Object receiveQuote(LoanQuote quote)
    {
        int quoteNumber = incQuotes();
        if (logger.isInfoEnabled())
        {
            String[] params = new String[] {
                String.valueOf(quoteNumber),
                quote.toString()
            };

            logger.info("\n***** " + LocaleMessage.receivedQuote(params));
        }
        return quote;
    }

    protected int incQuotes()
    {
        return quotes.incrementAndGet();
    }

    protected int incRequests()
    {
        return requests.incrementAndGet();
    }

    protected int incProfiles()
    {
        return profiles.incrementAndGet();
    }
}
