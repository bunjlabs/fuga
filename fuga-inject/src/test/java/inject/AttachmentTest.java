package inject;

import fuga.inject.*;
import org.junit.jupiter.api.Test;

import static fuga.inject.InjectHint.Target.ATTRIBUTE;
import static fuga.inject.InjectHint.Target.SOURCE;
import static org.junit.jupiter.api.Assertions.*;

public class AttachmentTest {


    @Test
    void testBasic() {
        var attribute = new AttributeA();
        var sampleA = new SampleA();
        var i = Injector.create(c -> {
            c.bind(SampleA.class).toInstance(sampleA);
            c.bind(Attachment.class).in(Singleton.class);
            c.match(SampleA.class).attach(Attachment.class);
            c.match(SampleA.class).configure(b -> b.setAttribute(AttributeA.class, attribute));
        });

        assertSame(sampleA, i.getInstance(SampleA.class));

        var attachment = i.getInstance(Attachment.class);

        assertSame(sampleA, attachment.source);
        assertSame(attribute, attachment.attribute);
    }

    @Test
    void testQualifiedAttachment() {
        var sampleA = new SampleA();
        var i = Injector.create(c -> {
            c.bind(SampleA.class).toInstance(sampleA);
            c.bind(QualifiedAttachment.class).in(Singleton.class);
            c.match(SampleA.class).attach(QualifiedAttachment.class);
        });

        assertSame(sampleA, i.getInstance(SampleA.class));
        var attachment = i.getInstance(QualifiedAttachment.class);
        assertSame(sampleA, attachment.source);
    }

    @Test
    void testBadAttachment() {
        var sampleA = new SampleA();
        var i = Injector.create(c -> {
            c.bind(SampleA.class).toInstance(sampleA);
            c.bind(BadAttachment.class).in(Singleton.class);
            c.match(SampleA.class).attach(BadAttachment.class);
        });
        assertThrows(ProvisionException.class, () -> i.getInstance(SampleA.class));
    }

    @Test
    void testAttachmentGc()  {
        var i = Injector.create(c -> {
            c.bind(SampleA.class).toInstance(new SampleA());
            c.bind(AttachmentA.class);
            c.bind(AttachmentB.class);
            c.bind(AttachmentC.class);
            c.match(SampleA.class).attach(AttachmentA.class);
            c.match(SampleA.class).attach(AttachmentB.class);
            c.match(SampleA.class).attach(AttachmentC.class);
        });
        assertNotNull(i.getInstance(SampleA.class));
        assertNotNull(i.getInstance(SampleA.class));
        assertNotNull(i.getInstance(SampleA.class));

        assertEquals(3, AttachmentA.constructed);
        assertEquals(3, AttachmentB.constructed);
        assertEquals(3, AttachmentC.constructed);
    }

    public static class SampleA {

    }

    public static class SampleB {

    }

    public static class AttributeA {

    }

    public static class Attachment {
        final AttributeA attribute;
        final Object source;

        @Inject
        public Attachment(
                @InjectHint(target = ATTRIBUTE) AttributeA attribute,
                @InjectHint(target = SOURCE) Object source) {
            this.attribute = attribute;
            this.source = source;
        }
    }

    public static class QualifiedAttachment {
        final SampleA source;

        @Inject
        public QualifiedAttachment(@InjectHint(target = SOURCE) SampleA source) {
            this.source = source;
        }
    }

    public static class BadAttachment {
        final SampleB source;

        @Inject
        public BadAttachment(@InjectHint(target = SOURCE) SampleB source) {
            this.source = source;
        }
    }


    public static class AttachmentA {
        public static int constructed = 0;

        public AttachmentA() {
            constructed++;
        }
    }

    public static class AttachmentB {
        public static int constructed = 0;

        public AttachmentB() {
            constructed++;
        }

    }

    public static class AttachmentC {
        public static int constructed = 0;

        public AttachmentC() {
            constructed++;
        }
    }
}
