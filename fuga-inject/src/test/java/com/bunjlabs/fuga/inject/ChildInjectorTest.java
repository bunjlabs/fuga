package com.bunjlabs.fuga.inject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChildInjectorTest {

    private static Injector createInjector(Unit... unit) {
        return new InjectorBuilder().withUnits(unit).build();
    }

    @Test
    public void testBindingsInherited() {
        var a = new A();
        var b = new B();
        var child = createInjector(c -> c.bind(A.class).toInstance(a))
                .createChildInjector(c -> c.bind(B.class).toInstance(b));
        assertSame(a, child.getInstance(A.class));
        assertSame(b, child.getInstance(B.class));
    }

    @Test
    public void testParentBindingCollision() {
        var a1 = new A();
        var a2 = new A();
        var child = createInjector(c -> c.bind(A.class).toInstance(a1))
                .createChildInjector(c -> c.bind(A.class).toInstance(a2));
        assertSame(a2, child.getInstance(A.class));
    }

    @Test
    public void testGetParent() {
        var first = createInjector();
        var second = first.createChildInjector();
        var third = second.createChildInjector();
        var root = first.getParent();
        assertNotNull(first.getParent());
        assertSame(first, second.getParent());
        assertSame(second, third.getParent());
        assertNull(root.getParent());
    }


    public static class A {

    }

    public static class B {

    }

    public static class C {

        private B b;

        @Inject
        public C(B b) {
            this.b = b;
        }
    }
}
