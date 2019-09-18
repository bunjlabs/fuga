package com.bunjlabs.fuga.logging.support;

import com.bunjlabs.fuga.inject.ProvisionException;
import com.bunjlabs.fuga.logging.LoggerComposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultLoggerComposer implements LoggerComposer {
    @Override
    public <T> T get(Class<?> requester, Class<T> required) throws ProvisionException {
        if (!Logger.class.equals(required)) {
            throw new ProvisionException("This composer can create org.slf4j.Logger only");
        }

        return doGet(requester, required);
    }

    @SuppressWarnings("unchecked")
    private <T> T doGet(Class<?> requester, Class<T> required) {
        return (T) LoggerFactory.getLogger(requester);
    }
}
