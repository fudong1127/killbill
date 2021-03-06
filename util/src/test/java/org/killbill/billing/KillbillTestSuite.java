/*
 * Copyright 2010-2012 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.killbill.billing;

import java.lang.reflect.Method;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import org.killbill.billing.util.callcontext.CallContext;
import org.killbill.billing.util.callcontext.CallOrigin;
import org.killbill.billing.callcontext.InternalCallContext;
import org.killbill.billing.util.callcontext.InternalCallContextFactory;
import org.killbill.billing.util.callcontext.UserType;
import org.killbill.clock.Clock;
import org.killbill.clock.ClockMock;

public class KillbillTestSuite {

    // Use the simple name here to save screen real estate
    protected static final Logger log = LoggerFactory.getLogger(KillbillTestSuite.class.getSimpleName());

    private boolean hasFailed = false;

    protected Clock clock = new ClockMock();

    protected final InternalCallContext internalCallContext = new InternalCallContext(InternalCallContextFactory.INTERNAL_TENANT_RECORD_ID, 1687L, UUID.randomUUID(),
                                                                                      UUID.randomUUID().toString(), CallOrigin.TEST,
                                                                                      UserType.TEST, "Testing", "This is a test",
                                                                                      clock.getUTCNow(), clock.getUTCNow());
    protected final CallContext callContext = internalCallContext.toCallContext(null);

    @BeforeMethod(alwaysRun = true)
    public void startTestSuite(final Method method) throws Exception {
        log.info("***************************************************************************************************");
        log.info("*** Starting test {}:{}", method.getDeclaringClass().getName(), method.getName());
        log.info("***************************************************************************************************");
    }

    @AfterMethod(alwaysRun = true)
    public void endTestSuite(final Method method, final ITestResult result) throws Exception {
        log.info("***************************************************************************************************");
        log.info("***   Ending test {}:{} {} ({} s.)", new Object[]{method.getDeclaringClass().getName(), method.getName(),
                result.isSuccess() ? "SUCCESS" : "!!! FAILURE !!!",
                (result.getEndMillis() - result.getStartMillis()) / 1000});
        log.info("***************************************************************************************************");
        if (!hasFailed && !result.isSuccess()) {
            hasFailed = true;
        }
    }

    public boolean hasFailed() {
        return hasFailed;
    }
}
