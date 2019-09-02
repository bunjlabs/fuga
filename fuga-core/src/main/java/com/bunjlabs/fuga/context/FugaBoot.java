package com.bunjlabs.fuga.context;

import com.bunjlabs.fuga.inject.Unit;

public final class FugaBoot {

    public static ApplicationContext start(Unit appUnit) {
        return ApplicationContext.fromUnits(appUnit);
    }
}
