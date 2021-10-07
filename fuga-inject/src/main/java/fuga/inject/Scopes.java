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

package fuga.inject;

import fuga.common.Key;

public abstract class Scopes {

    public static final Scope SINGLETON = new SingletonScope();

    public static final Scope NO_SCOPE =
            new Scope() {
                @Override
                public <T> Provider<T> scope(Key<T> key, Provider<T> unscoped) {
                    return unscoped;
                }

                @Override
                public String toString() {
                    return "Scopes.NO_SCOPE";
                }
            };
}
