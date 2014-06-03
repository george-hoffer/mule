/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extensions.introspection.api;

import java.util.List;

/**
 * An access point to a sub set of {@link org.mule.extensions.introspection.api.MuleExtensionOperation}s
 * available in a {@link org.mule.extensions.introspection.api.MuleExtension}.
 * <p/>
 * The configuration can also imply different implicit behaviors not strictly attached to the operations
 * (e.g.: A connector supporting both stateful connections an OAuth2 authentication. Depending on the
 * configuration used, the same connector will have different reconnection strategies).
 * <p/>
 * The configuration is also the place in which cross operation, extension level attributes are configured.
 * <p/>
 * Every {@link org.mule.extensions.introspection.api.MuleExtension} is required to have at least one configuration.
 * That configuration is defined as the &quot;default configuration&quot;
 *
 * @since 1.0
 */
public interface MuleExtensionConfiguration extends Described
{

    /**
     * Default name for the default configuration. This is just a default name. The default configuration
     * is not required to be named like this
     */
    public static final String DEFAULT_NAME = "config";

    /**
     * A default description for the default configuration
     */
    public static final String DEFAULT_DESCRIPTION = "Default config";

    /**
     * Returns the {@link org.mule.extensions.introspection.api.MuleExtensionParameter}s
     * available for this configuration
     *
     * @return a immutable {@link java.util.List} with {@link org.mule.extensions.introspection.api.MuleExtensionParameter}
     * instances. It might be empty but it will never be {@code null}
     */
    List<MuleExtensionParameter> getParameters();

}
