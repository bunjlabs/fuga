package com.bunjlabs.fuga.logging;

import com.bunjlabs.fuga.inject.Composer;
import com.bunjlabs.fuga.inject.ProvisionException;

public interface LoggerComposer extends Composer {

    @Override
    <T> T get(Class<?> requester, Class<T> required) throws ProvisionException;
    
}
