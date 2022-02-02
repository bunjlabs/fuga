/*
 * Copyright 2019-2021 Bunjlabs
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

package fuga.inject.support;

import fuga.common.Key;
import fuga.inject.Composer;
import fuga.inject.ConfigurationException;
import fuga.inject.Dependency;

class DelegatedComposerFactory<T> implements InternalFactory<T>, Initializable {

    private final Key<T> targetKey;
    private final Key<? extends Composer> composerKey;
    private DependencyInjector<? extends Composer> composerInjector;

    DelegatedComposerFactory(Key<T> targetKey, Key<? extends Composer> composerKey) {
        this.targetKey = targetKey;
        this.composerKey = composerKey;
    }

    @Override
    public void initialize(InjectorImpl injector) {
        var dependency = Dependency.of(composerKey);

        composerInjector = injector.getDependencyInjector(dependency);
        if (composerInjector == null) {
            throw new ConfigurationException("Unsatisfied composer dependency: " + dependency);
        }
    }

    @Override
    public T get(InjectorContext context) throws InternalProvisionException {
        var composer = composerInjector.inject(context);

        try {
            return composer.get(context.getDependency().getKey(), targetKey);
        } catch (RuntimeException e) {
            throw InternalProvisionException.errorInComposer(e);
        }
    }
}
