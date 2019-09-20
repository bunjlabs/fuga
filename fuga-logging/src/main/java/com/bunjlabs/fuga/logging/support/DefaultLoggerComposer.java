package com.bunjlabs.fuga.logging.support;

import com.bunjlabs.fuga.inject.Key;
import com.bunjlabs.fuga.inject.ProvisionException;
import com.bunjlabs.fuga.logging.LoggerComposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultLoggerComposer implements LoggerComposer {
    @Override
    public <T> T get(Key<T> requester, Key<T> requested) throws ProvisionException {
        if (!Logger.class.equals(requested.getType())) {
            throw new ProvisionException("This composer can create org.slf4j.Logger only");
        }

        return doGet(requester.getType(), requested.getType());
    }

    @SuppressWarnings("unchecked")
    private <T> T doGet(Class<?> requester, Class<T> cast) {
        return (T) LoggerFactory.getLogger(requester);
    }
}
