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

package com.bunjlabs.fuga.inject;

import com.bunjlabs.fuga.inject.bindings.*;

public interface BindingVisitor<T, V> {

    V visit(InstanceBinding<? extends T> binding);

    V visit(ConstructorBinding<? extends T> binding);

    V visit(ProviderKeyBinding<? extends T> binding);

    V visit(ProviderBinding<? extends T> binding);

    V visit(ComposerKeyBinding<? extends T> binding);

    V visit(ComposerBinding<? extends T> binding);

    V visit(LinkedKeyBinding<? extends T> binding);

    V visit(AutoBinding<? extends T> binding);

    V visit(UntargettedBinding<? extends T> binding);
}