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

package com.bunjlabs.fuga.examples.remoteexportimporttest.units;

import com.bunjlabs.fuga.examples.remoteexportimporttest.services.TestExportedService;
import com.bunjlabs.fuga.inject.Configuration;
import com.bunjlabs.fuga.inject.Inject;
import com.bunjlabs.fuga.inject.Unit;

public class RemoteExportUnit implements Unit {

    private TestExportedService service;

    @Inject
    public RemoteExportUnit(TestExportedService service) {
        this.service = service;
    }

    @Override
    public void setup(Configuration configuration) {
    }
}
