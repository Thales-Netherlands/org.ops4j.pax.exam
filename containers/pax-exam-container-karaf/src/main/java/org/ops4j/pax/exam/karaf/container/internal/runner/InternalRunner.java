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
package org.ops4j.pax.exam.karaf.container.internal.runner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.ops4j.io.Pipe;

public class InternalRunner {

    private Process frameworkProcess;
    private final Object frameworkProcessMonitor = new Object();
    private Thread shutdownHook;
    private final Object shutdownHookMonitor = new Object();

    public synchronized void exec(CommandLineBuilder commandLine, final File workingDirectory,
        final String[] envOptions) {
        if (frameworkProcess != null) {
            throw new IllegalStateException("Platform already started");
        }

        try {
            frameworkProcess = Runtime.getRuntime().exec(commandLine.toArray(),
                createEnvironmentVars(envOptions), workingDirectory);
        }
        catch (IOException e) {
            throw new IllegalStateException("Could not start up the process", e);
        }

        shutdownHook = createShutdownHook(frameworkProcess);
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        waitForExit();
    }

    private String[] createEnvironmentVars(String[] envOptions) {
        List<String> env = new ArrayList<String>();
        Map<String, String> getenv = System.getenv();
        for (String key : getenv.keySet()) {
            env.add(key + "=" + getenv.get(key));
        }
        if (envOptions != null) {
            Collections.addAll(env, envOptions);
        }
        return env.toArray(new String[env.size()]);
    }

    public void shutdown() {
        try {
            if (shutdownHook != null) {
                synchronized (shutdownHookMonitor) {
                    if (shutdownHook != null) {
                        Runtime.getRuntime().removeShutdownHook(shutdownHook);
                        frameworkProcess = null;
                        shutdownHook.run();
                        shutdownHook = null;
                    }
                }
            }
        }
        catch (IllegalStateException ignore) {
            // just ignore
        }
    }

    /**
     * Wait till the framework process exits.
     */
    public void waitForExit() {
        if (shutdownHook != null) {
            synchronized (shutdownHookMonitor) {
                if (shutdownHook != null) {
                    synchronized (frameworkProcessMonitor) {
                        try {
                            frameworkProcess.waitFor();
                            shutdown();
                        // CHECKSTYLE:SKIP
                        } catch (Throwable e) {
                            shutdown();
                        }
                    }
                }
            }
        }
    }

    /**
     * Create helper thread to safely shut down the external framework process
     * 
     * @param process
     *            framework process
     * @return stream handler
     */
    private Thread createShutdownHook(final Process process) {
        final Pipe errPipe = new Pipe(process.getErrorStream(), System.err).start("Error pipe");
        final Pipe outPipe = new Pipe(process.getInputStream(), System.out).start("Out pipe");
        final Pipe inPipe = new Pipe(process.getOutputStream(), System.in).start("In pipe");

        return new Thread(new Runnable() {

            @Override
            public void run() {
                inPipe.stop();
                outPipe.stop();
                errPipe.stop();

                try {
                    process.destroy();
                }
                // CHECKSTYLE:SKIP
                catch (Exception e) {
                    // ignore if already shutting down
                }
            }
        }, "Karaf InternalRunner Shutdown Hook");
    }

}
