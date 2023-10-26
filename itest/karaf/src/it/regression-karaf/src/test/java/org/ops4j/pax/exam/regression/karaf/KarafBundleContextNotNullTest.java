/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ops4j.pax.exam.regression.karaf;

import static org.junit.Assert.assertNotNull;
import static org.ops4j.pax.exam.regression.karaf.RegressionConfiguration.regressionDefaults;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.osgi.framework.BundleContext;

@RunWith(PaxExam.class)
public class KarafBundleContextNotNullTest extends TestBase {

    @Inject
    private BundleContext bundleContext;

    // TODO: PAXEXAM-288
    // @BeforeClass
    // public void beforeClass() {
    // System.out.println("=================================");
    // System.out.println("CLASS Before bundleContext validation CLASS");
    // System.out.println("=================================");
    // }

    @Before
    public void setUp() {
        System.out.println("=================================");
        System.out.println("Before bundleContext validation");
        System.out.println("=================================");
    }

    @Configuration
    public Option[] config() {
        return new Option[]{
            regressionDefaults(unpackDirectory())
        };
    }

    @Test
    public void test() throws Exception {
        assertNotNull(bundleContext);
    }

    @After
    public void tearDown() {
        System.out.println("=================================");
        System.out.println("After  bundleContext validation");
        System.out.println("=================================");
    }

    // TODO: PAXEXAM-288
    // @AfterClass
    // public void afterClass() {
    // System.out.println("=================================");
    // System.out.println("CLASS After bundleContext validation CLASS");
    // System.out.println("=================================");
    // }

}
