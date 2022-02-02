package inject;

import fuga.inject.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DependentInjectorTest {

    @Test
    void testBasicDepends() {
        assertNotNull(Injector.create(c -> c.depends(UnitForA.class)).getInstance(SampleA.class));
        assertNotNull(Injector.create(c -> c.depends(UnitOneForDeepA.class)).getInstance(SampleA.class));
        assertNotNull(Injector.create(c -> {
            c.depends(UnitOneForDeepA.class);
            c.depends(UnitTwoForDeepA.class);
        }).getInstance(SampleA.class));
    }

    @Test
    void testDependsInstalled() {
        assertNotNull(Injector.create(c -> {
            c.depends(UnitOneForDeepA.class);
            c.install(new UnitForA());
        }).getInstance(SampleA.class));
    }

    @Test
    void testUnitNotProvided() {
        assertThrows(ConfigurationException.class, () -> Injector.create(c -> c.depends(UnitForA.class)).getInstance(UnitForA.class));
    }

    @Test
    void testCrossDepends() {
        var injector = Injector.create(c -> {
            c.depends(UnitOneForDeepA.class);
            c.depends(UnitTwoForDeepA.class);
        });

        assertSame(injector.getInstance(SampleInject.class).sampleA, injector.getInstance(SampleInject.class).sampleA);
    }

    @Test
    void testDependsCrossUnits() {
        var injector = Injector.create(
                c -> c.depends(UnitForA.class),
                c -> c.install(new UnitForA())
        );

        assertEquals(1, injector.getAllBindings(SampleA.class).size());
    }

    public static class SampleA {
    }

    public static class SampleInject {

        SampleA sampleA;

        @Inject
        public SampleInject(SampleA sampleA) {
            this.sampleA = sampleA;
        }
    }


    public static class UnitForA implements Unit {

        @Override
        public void setup(Configuration c) {
            c.bind(SampleA.class).toInstance(new SampleA());
            c.bind(SampleInject.class);
        }
    }

    public static class UnitOneForDeepA implements Unit {

        @Override
        public void setup(Configuration c) {
            c.depends(UnitForA.class);
        }
    }

    public static class UnitTwoForDeepA implements Unit {

        @Override
        public void setup(Configuration c) {
            c.depends(UnitForA.class);
        }
    }
}
