/*
 * Copyright 2011 Harald Wellmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.exam.invoker.junit.internal;

import org.ops4j.pax.exam.ProbeInvoker;
import org.ops4j.pax.exam.ProbeInvokerFactory;
import org.ops4j.pax.exam.util.Injector;
import org.ops4j.pax.swissbox.tracker.ServiceLookup;
import org.osgi.framework.BundleContext;


/**
 * A {@link ProbeInvokerFactory} which creates {@link JUnitProbeInvoker}s.
 *
 * @author Harald Wellmann
 */
public class JUnitProbeInvokerFactory implements ProbeInvokerFactory {

	/**
	 * Default timeout used for service lookup when no explicit timeout is specified.
	 */
	public static final long DEFAULT_TIMEOUT;

	static {
		final String DEFAULT_TIMEOUT_VALUE_PROPERTY =
				"org.ops4j.pax.exam.invoker.junit.internal.JUnitProbeInvokerFactory.DEFAULT_TIMEOUT";
		final int DEFAULT_TIMEOUT_VALUE = 10000;
		DEFAULT_TIMEOUT = Long.getLong(DEFAULT_TIMEOUT_VALUE_PROPERTY, DEFAULT_TIMEOUT_VALUE);
	}

	public ProbeInvoker createProbeInvoker(Object context, String expr) {
		BundleContext ctx = (BundleContext) context;
		Injector injector = ServiceLookup.getService(ctx, Injector.class, DEFAULT_TIMEOUT);
		return new JUnitProbeInvoker(expr, ctx, injector);
	}
}
