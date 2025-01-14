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
package org.ops4j.pax.exam.karaf.container;

import org.junit.Ignore;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.karaf.options.LogLevelOption.LogLevel;

import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.configureConsole;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

@Ignore
public class Karaf4EmbeddedTestContainerIT extends AbstractKarafTestContainerIT {

    @Configuration
    public Option[] config() {
        String karafVersion = karafVersion();
        return options(
            karafDistributionConfiguration().
                runEmbedded(true).
                frameworkUrl(KARAF_URL.version(karafVersion)).
                karafVersion(karafVersion).
                useDeployFolder(false).
                unpackDirectory(UNPACK_DIRECTORY),
            configureConsole().
                startLocalConsole().
                ignoreRemoteShell(),
            logLevel(LogLevel.DEBUG),
            keepRuntimeFolder()
        );
    }

}
