package com.bunjlabs.fuga.settings;

import com.bunjlabs.fuga.inject.Composer;
import com.bunjlabs.fuga.inject.Key;

public interface SettingsComposer extends Composer {

    @Override
    <T> T get(Key<T> requester, Key<T> requested) throws SettingsException;

}
