/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extensions.internal;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mule.extensions.introspection.api.Extension;
import org.mule.extensions.introspection.api.capability.XmlCapability;
import org.mule.extensions.introspection.spi.ExtensionDescriber;
import org.mule.tck.junit4.AbstractMuleTestCase;
import org.mule.tck.size.SmallTest;

import com.google.common.collect.ImmutableList;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultExtensionsManagerTestCase extends AbstractMuleTestCase
{

    private DefaultExtensionsManager extensionsManager;

    private static final String EXTENSION1_NAME = "extension1";
    private static final String EXTENSION2_NAME = "extension2";
    private static final String EXTENSION1_VERSION = "3.6.0";
    private static final String EXTENSION2_VERSION = "3.6.0";
    private static final String NEWER_VERSION = "4.0";
    private static final String OLDER_VERSION = "3.5.1";

    @Mock
    private ExtensionDiscoverer discoverer;

    @Mock
    private Extension extension1;

    @Mock
    private Extension extension2;

    private ClassLoader classLoader;

    @Before
    public void before()
    {
        extensionsManager = new DefaultExtensionsManager();
        extensionsManager.setExtensionsDiscoverer(discoverer);

        when(extension1.getName()).thenReturn(EXTENSION1_NAME);
        when(extension2.getName()).thenReturn(EXTENSION2_NAME);

        when(extension1.getVersion()).thenReturn(EXTENSION1_VERSION);
        when(extension2.getVersion()).thenReturn(EXTENSION2_VERSION);

        classLoader = getClass().getClassLoader();

        when(discoverer.discover(same(classLoader), any(ExtensionDescriber.class))).thenAnswer(new Answer<List<Extension>>()
        {
            @Override
            public List<Extension> answer(InvocationOnMock invocation) throws Throwable
            {
                return getTestExtensions();
            }
        });
    }

    @Test
    public void discover()
    {
        List<Extension> extensions = extensionsManager.discoverExtensions(classLoader);
        verify(discoverer).discover(same(classLoader), any(ExtensionDescriber.class));
        testEquals(getTestExtensions(), extensions);
    }

    @Test
    public void getExtensions()
    {
        discover();
        testEquals(getTestExtensions(), extensionsManager.getExtensions());
    }

    @Test
    public void getExtensionsCapableOf()
    {
        when(extension1.isCapableOf(XmlCapability.class)).thenReturn(true);
        when(extension2.isCapableOf(XmlCapability.class)).thenReturn(false);

        discover();
        List<Extension> extensions = extensionsManager.getExtensionsCapableOf(XmlCapability.class);

        assertEquals(1, extensions.size());
        testEquals(extension1, extensions.get(0));
    }

    @Test
    public void noExtensionsCapableOf()
    {
        when(extension1.isCapableOf(XmlCapability.class)).thenReturn(false);
        when(extension2.isCapableOf(XmlCapability.class)).thenReturn(false);

        discover();
        List<Extension> extensions = extensionsManager.getExtensionsCapableOf(XmlCapability.class);

        assertTrue(extensions.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void extensionsCapableOfNull()
    {
        extensionsManager.getExtensionsCapableOf(null);
    }

    @Test
    public void hotUpdateNewerExtension()
    {
        discover();

        extension1 = mock(Extension.class);
        when(extension1.getName()).thenReturn(EXTENSION1_NAME);
        when(extension1.getVersion()).thenReturn(NEWER_VERSION);

        List<Extension> extensions = extensionsManager.discoverExtensions(classLoader);
        assertEquals(1, extensions.size());
        testEquals(extension1, extensions.get(0));
    }

    @Test
    public void hotUpdateOlderVersion()
    {
        discover();

        extension1 = mock(Extension.class);
        when(extension1.getName()).thenReturn(EXTENSION1_NAME);
        when(extension1.getVersion()).thenReturn(OLDER_VERSION);

        List<Extension> extensions = extensionsManager.discoverExtensions(classLoader);
        assertTrue(extensions.isEmpty());
    }

    @Test
    public void hotUpdateWithNoChanges()
    {
        discover();
        List<Extension> extensions = extensionsManager.discoverExtensions(classLoader);
        assertTrue(extensions.isEmpty());
    }

    @Test
    public void hotUpdateWithInvalidVersion()
    {
        discover();

        extension1 = mock(Extension.class);
        when(extension1.getName()).thenReturn(EXTENSION1_NAME);
        when(extension1.getVersion()).thenReturn("brandnew");

        List<Extension> extensions = extensionsManager.discoverExtensions(classLoader);
        assertTrue(extensions.isEmpty());
    }

    @Test
    public void hotUpdateWithOneInvalidVersion()
    {
        discover();

        extension1 = mock(Extension.class);
        when(extension1.getName()).thenReturn(EXTENSION1_NAME);
        when(extension1.getVersion()).thenReturn("brandnew");

        extension2 = mock(Extension.class);
        when(extension2.getName()).thenReturn(EXTENSION2_NAME);
        when(extension2.getVersion()).thenReturn(NEWER_VERSION);

        List<Extension> extensions = extensionsManager.discoverExtensions(classLoader);
        assertEquals(1, extensions.size());
        testEquals(extension2, extensions.get(0));
    }

    @Test
    public void contextClassLoaderKept()
    {
        discover();
        assertSame(classLoader, Thread.currentThread().getContextClassLoader());
    }

    @Test
    public void contextClassLoaderKeptAfterException()
    {
        when(discoverer.discover(same(classLoader), any(ExtensionDescriber.class))).thenThrow(RuntimeException.class);
        try
        {
            discover();
            fail("was expecting an exception");
        }
        catch (RuntimeException e)
        {
            assertSame(classLoader, Thread.currentThread().getContextClassLoader());
        }
    }

    private List<Extension> getTestExtensions()
    {
        return ImmutableList.<Extension>builder()
                .add(extension1)
                .add(extension2)
                .build();
    }

    private void testEquals(List<Extension> expected, List<Extension> obtained)
    {
        assertEquals(expected.size(), obtained.size());

        for (int i = 0; i < expected.size(); i++)
        {
            testEquals(expected.get(i), obtained.get(i));
        }
    }

    private void testEquals(Extension expected, Extension obtained)
    {
        assertEquals(expected.getName(), obtained.getName());
        assertEquals(expected.getVersion(), obtained.getVersion());
    }

}
