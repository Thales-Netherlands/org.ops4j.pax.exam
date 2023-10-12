/*
 * Copyright (C) 2011 Harald Wellmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.pax.exam.regression.multi.fragment;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.ops4j.pax.exam.CoreOptions.cleanCaches;
import static org.ops4j.pax.exam.CoreOptions.junitBundles;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.url;
import static org.ops4j.pax.exam.regression.multi.RegressionConfiguration.regressionDefaults;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Info;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.TestContainerException;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.swissbox.tracker.ServiceLookup;
import org.ops4j.pax.tinybundles.TinyBundle;
import org.ops4j.pax.tinybundles.TinyBundles;
import org.ops4j.store.Handle;
import org.ops4j.store.Store;
import org.ops4j.store.StoreFactory;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

@RunWith(PaxExam.class)
public class FragmentTest {

    @Inject
    private BundleContext bc;

    @Configuration()
    public Option[] config() {
        return options(regressionDefaults(),
            mavenBundle("org.ops4j.pax.exam.samples", "pax-exam-sample9-pde", Info.getPaxExamVersion()),
            url(createFragmentBundle().toExternalForm()).noStart(), junitBundles(), cleanCaches());
    }

    private URL createFragmentBundle() {
        TinyBundle bundle = TinyBundles.bundle()
            .setHeader(Constants.FRAGMENT_HOST, "org.ops4j.pax.exam.sample9.pde")
            .setHeader(Constants.BUNDLE_MANIFESTVERSION, "2")
            .setHeader(Constants.BUNDLE_SYMBOLICNAME, "org.ops4j.pax.exam.sample9.fragment")
            .addResource("messages.properties", getClass().getResource("/messages.properties"));

        try {
            Store<InputStream> store = StoreFactory.anonymousStore();
            Handle handle = store.store(bundle.build());
            return store.getLocation(handle).toURL();
        }
        catch (IOException e) {
            throw new TestContainerException(e);
        }
    }

    @Test
    public void getHelloService() {
        for (Bundle bundle : bc.getBundles()) {
            System.out.println(bundle.getSymbolicName() + " state = " + bundle.getState());
        }
        Object service = ServiceLookup.getService(bc,
            "org.ops4j.pax.exam.sample9.pde.HelloService");
        assertThat(service, is(notNullValue()));
    }
}
