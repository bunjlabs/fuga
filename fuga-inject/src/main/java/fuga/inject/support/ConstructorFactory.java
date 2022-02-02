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

import fuga.inject.ConfigurationException;
import fuga.inject.InjectionPoint;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

class ConstructorFactory<T> implements InternalFactory<T>, Initializable {

    private final InjectionPoint injectionPoint;
    private final ConstructionProxy<T> constructionProxy;
    private final List<DependencyInjector<?>> dependencyInjectors = new ArrayList<>();

    ConstructorFactory(InjectionPoint injectionPoint, ConstructionProxy<T> constructionProxy) {
        this.injectionPoint = injectionPoint;
        this.constructionProxy = constructionProxy;
    }

    @Override
    public void initialize(InjectorImpl injector) {
        for (var dependency : injectionPoint.getDependencies()) {
            var dependencyInjector = injector.getDependencyInjector(dependency);

            if (!dependency.isNullable() && dependencyInjector == null) {
                throw new ConfigurationException("Unsatisfied construction dependency: " + dependency);
            }

            dependencyInjectors.add(dependencyInjector);
        }
    }

    @Override
    public T get(InjectorContext context) throws InternalProvisionException {
        ConstructionContext<T> constructionContext = context.getConstructionContext(this);

        if (constructionContext.isConstructing()) {
            throw InternalProvisionException.circularDependencies(context.getDependency().getKey().getRawType());
        }

        var instance = constructionContext.getReference();
        if (instance != null) {
            throw InternalProvisionException.circularDependencies(context.getDependency().getKey().getRawType());
        }

        instance = construct(context, constructionContext);

        try {
            constructionContext.setReference(instance);

            // TODO: inject to members

            return instance;
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
        var parameters = new Object[dependencyInjectors.size()];

        for (int i = 0; i < parameters.length; i++) {
            parameters[i] = dependencyInjectors.get(i).inject(context);
        }

        return parameters;
    }
}
