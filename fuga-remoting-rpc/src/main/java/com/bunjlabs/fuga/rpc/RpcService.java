/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.rpc;

import com.bunjlabs.fuga.channel.ChannelHandler;
import com.bunjlabs.fuga.channel.ChannelHandlerException;
import com.bunjlabs.fuga.inject.Inject;
import com.bunjlabs.fuga.rpc.annotations.*;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public class RpcService implements ChannelHandler<RpcRequest> {

    private final Map<String, RpcTarget> rpcTargets = new HashMap<>();

    @Inject
    public RpcService() {
    }

    public void onCreate() {
        Collection<Object> services = Collections.EMPTY_LIST;

        services.stream().forEach(service -> {
            Class serviceClass = service.getClass();

            Rpc rpcService = (Rpc) serviceClass.getAnnotation(Rpc.class);

            StringBuilder targetBuilder = new StringBuilder("/");

            MethodFamily methodFamily = rpcService.methodFamily();
            ParameterMappingType defaultParameterMappingType = rpcService.parameterMapping().type();

            if (methodFamily.policy() == MethodFamilyPolicy.CLASSNAME) {
                targetBuilder.append(serviceClass.getSimpleName());
            } else {
                targetBuilder.append(methodFamily.name());
            }

            targetBuilder.append("/");

            for (final Method method : serviceClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RpcMethod.class)) {
                    RpcMethod rpcMethod = (RpcMethod) method.getAnnotation(RpcMethod.class);

                    if (rpcMethod.name().isEmpty()) {
                        targetBuilder.append(method.getName());
                    } else {
                        targetBuilder.append(rpcMethod.name());
                    }

                    String target = targetBuilder.toString();
                    RpcTarget rpcTarget = new RpcTarget(target, service, method);

                    registerTarget(rpcTarget);
                }
            }
        });
    }

    private void registerTarget(RpcTarget rpcTarget) {
        this.rpcTargets.put(rpcTarget.getTarget(), rpcTarget);
    }

    private RpcTarget findTarget(String target) {
        return this.rpcTargets.get(target);
    }

    @Override
    public void handle(RpcRequest request) throws ChannelHandlerException {
        RpcTarget target = findTarget(request.getTarget());

        if (target == null) {
            throw new ChannelHandlerException("Target not found");
        }

        try {
            Object result = target.invoke();

            request.setResult(result);
        } catch (RpcTargetException e) {
            throw new ChannelHandlerException("Unable to invoke rpc target", e);
        }
    }
}
