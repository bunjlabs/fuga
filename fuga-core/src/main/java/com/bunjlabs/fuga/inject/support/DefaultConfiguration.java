package com.bunjlabs.fuga.inject.support;

import com.bunjlabs.fuga.inject.Configuration;
import com.bunjlabs.fuga.inject.Unit;

import java.util.LinkedList;
import java.util.List;

public class DefaultConfiguration extends DefaultMapBinder implements Configuration {

    private List<Unit> installedUnits = new LinkedList<>();

    List<Unit> getInstalledUnits() {
        return installedUnits;
    }

    @Override
    public void install(Unit unit) {
        installedUnits.add(unit);
    }
}
