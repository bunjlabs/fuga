package fuga.settings;

import fuga.inject.Inject;
import fuga.inject.Injector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BasicTest {

    @Test
    void testBasics() {
        var i = Injector.create(c -> {
            c.depends(SettingsUnit.class);
            c.bind(TestSettings.class);
            c.bind(TestUser.class);
        });

        i.getInstance(TestUser.class);
    }

    @Settings
    public interface TestSettings {

        @SettingDefault("default")
        String value();
    }

    public static class TestUser {

        @Inject
        public TestUser(TestSettings settings) {
            assertNotNull(settings);
            assertEquals("default", settings.value());
        }
    }
}
