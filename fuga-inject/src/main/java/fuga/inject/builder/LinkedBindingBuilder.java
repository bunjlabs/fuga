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

package fuga.inject.builder;

import fuga.common.Key;
import fuga.inject.Composer;
import fuga.inject.Provider;

import java.lang.reflect.Constructor;

public interface LinkedBindingBuilder<T> extends ScopedBindingBuilder {

    ScopedBindingBuilder to(Class<? extends T> target);

    ScopedBindingBuilder to(Key<? extends T> target);

    void toInstance(T instance);

    <S extends T> ScopedBindingBuilder toConstructor(Constructor<S> constructor);

    ScopedBindingBuilder toProvider(Provider<? extends T> provider);

    ScopedBindingBuilder toProvider(Class<? extends Provider<? extends T>> provider);

    ScopedBindingBuilder toProvider(Key<? extends Provider<? extends T>> provider);

    ScopedBindingBuilder toComposer(Composer composer);

    ScopedBindingBuilder toComposer(Class<? extends Composer> composer);

    ScopedBindingBuilder toComposer(Key<? extends Composer> composer);
}
