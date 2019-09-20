package com.bunjlabs.fuga.logging;

import com.bunjlabs.fuga.inject.Composer;
import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.ProvisionException;

public interface LoggerComposer extends Composer {

    @Override
    <T> T get(Key<T> requester, Key<T> requested) throws ProvisionException;
    
}
