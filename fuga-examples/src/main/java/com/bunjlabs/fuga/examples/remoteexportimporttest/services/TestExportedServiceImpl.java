/*
 * Copyright 2019 Bunjlabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bunjlabs.fuga.examples.remoteexportimporttest.services;

import com.bunjlabs.fuga.examples.remoteexportimporttest.settings.TextExportedServiceSettings;
import com.bunjlabs.fuga.inject.Inject;

public class TestExportedServiceImpl implements TestExportedService {

    private final TextExportedServiceSettings serviceSettings;

    @Inject
    public TestExportedServiceImpl(TextExportedServiceSettings serviceSettings) {
        this.serviceSettings = serviceSettings;
    }

    @Override
    public String test(int a, int b) {
        if (a == b) throw new RuntimeException("Oops");

        return "Result is:" + (a * b + serviceSettings.intValue()) + " - " + serviceSettings.stringValue();
    }
}
