/*
 * Copyright 2019-2020 Bunjlabs
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

package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.InjectionPoint;

import java.lang.reflect.InvocationTargetException;

class ConstructorInjector<T> {

    private final InjectionPoint injectionPoint;
    private final ConstructionProxy<T> constructionProxy;

    ConstructorInjector(InjectionPoint injectionPoint, ConstructionProxy<T> constructionProxy) {
        this.injectionPoint = injectionPoint;
        this.constructionProxy = constructionProxy;
    }

    T construct(InjectorContext context) throws InternalProvisionException {
        ConstructionContext<T> constructionContext = context.getConstructionContext(this);

        if (constructionContext.isConstructing()) {
            throw InternalProvisionException.circularDependencies(context.getDependency().getKey().getRawType());
        }

        var t = constructionContext.getReference();
        if (t != null) {
            throw InternalProvisionException.circularDependencies(context.getDependency().getKey().getRawType());
        }

        t = construct(context, constructionContext);

        try {
            constructionContext.setReference(t);

            // TODO: inject to members

            return t;
        } finally {
            constructionContext.removeReference();
        }
    }

    private T construct(InjectorContext context, ConstructionContext<T> constructionContext) throws InternalProvisionException {
        constructionContext.startConstruction();
        try {
            var parameters = resolveDependencies(context);
            return constructionProxy.newInstance(parameters);
        } catch (InvocationTargetException e) {
            throw InternalProvisionException.errorInjectingConstructor(e);
        } finally {
            constructionContext.finishConstruction();
        }
    }

    private Object[] resolveDependencies(InjectorContext context) throws InternalProvisionException {
        var dependencies = injectionPoint.getDependencies();
        var objects = new Object[dependencies.size()];

        for (int i = 0; i < objects.length; i++) {
            var injector = context.getInjector().getDependencyInjector(dependencies.get(i));
            objects[i] = injector.inject(context);
        }

        return objects;
    }
}
