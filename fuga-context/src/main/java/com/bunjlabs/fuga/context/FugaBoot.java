package com.bunjlabs.fuga.context;

import com.bunjlabs.fuga.inject.Unit;

public final class FugaBoot {

    public static ApplicationContext start(Unit appUnit) {
        ApplicationContext context = new ApplicationContextBuilder().withUnits(appUnit).build();

        context.start();

        return context;
    }
}
