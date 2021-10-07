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

import fuga.inject.Scope;
import fuga.inject.Scopes;

import java.lang.annotation.Annotation;

abstract class Scoping {

    static final Scoping UNSCOPED =
            new Scoping() {
                @Override
                public Scope getScopeInstance() {
                    return Scopes.NO_SCOPE;
                }

                @Override
                public String toString() {
                    return Scopes.NO_SCOPE.toString();
                }
            };

    static Scoping forAnnotation(final Class<? extends Annotation> annotationType) {
        return new Scoping() {
            @Override
            public Class<? extends Annotation> getScopeAnnotation() {
                return annotationType;
            }

            @Override
            public String toString() {
                return annotationType.getName();
            }
        };
    }

    static Scoping forInstance(final Scope scope) {
        return new Scoping() {
            @Override
            public Scope getScopeInstance() {
                return scope;
            }

            @Override
            public String toString() {
                return scope.toString();
            }
        };
    }

    Scope getScopeInstance() {
        return null;
    }

    Class<? extends Annotation> getScopeAnnotation() {
        return null;
    }
}
