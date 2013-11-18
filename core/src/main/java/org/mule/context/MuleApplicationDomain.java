/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.context;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.api.lifecycle.Disposable;
import org.mule.api.lifecycle.Initialisable;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.api.lifecycle.Startable;
import org.mule.api.lifecycle.Stoppable;

public class MuleApplicationDomain
{

    private final MuleContext muleContext;
    private String domain;
    private Object context;

    public MuleApplicationDomain(String domain, MuleContext muleContext, Object context)
    {
        this.domain = domain;
        this.muleContext = muleContext;
        this.context = context;
    }

    public String getName()
    {
        return domain;
    }

    public Object getContext()
    {
        return context;
    }

    public MuleContext getMuleContext()
    {
        return muleContext;
    }
}