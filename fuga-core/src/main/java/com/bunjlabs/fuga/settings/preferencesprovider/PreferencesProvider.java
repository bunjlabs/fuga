package com.bunjlabs.fuga.settings.preferencesprovider;

import java.io.InputStream;
import java.util.prefs.Preferences;

public interface PreferencesProvider {

    Preferences getPreferences(InputStream is);
}
