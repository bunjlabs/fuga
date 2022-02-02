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
import fuga.inject.builder.BindingBuilder;
import fuga.inject.builder.MatchBuilder;
import fuga.lang.TypeLiteral;
import fuga.util.Matcher;
import fuga.util.Matchers;

public interface Binder {

    default <T> MatchBuilder<T> match(Class<T> type) {
        return match(Matchers.only(type));
    }

    default <T> MatchBuilder<T> match(Key<T> key) {
        return match(key.getType());
    }

    default <T> MatchBuilder<T> match(TypeLiteral<T> type) {
        return match(Matchers.only(type));
    }

    <T> MatchBuilder<T> match(Matcher<? super TypeLiteral<T>> matcher);

    default <T> BindingBuilder<T> bind(Class<T> type) {
        return bind(Key.of(type));
    }

    <T> BindingBuilder<T> bind(Key<T> type);
}
