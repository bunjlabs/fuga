package fuga.inject.support;

import fuga.common.Key;
import fuga.inject.Dependency;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.*;

class AttachmentFactory<T> implements InternalFactory<T>, Initializable {

    private final ReferenceQueue<T> queue = new ReferenceQueue<>();
    private final Map<AttachmentTarget<T>, AttachmentCollection> attachments = Collections.synchronizedMap(new IdentityHashMap<>());

    private final InternalFactory<T> internalFactory;
    private final List<? extends Key<Object>> attachmentsKeys;
    private final List<DependencyInjector<?>> attachmentsInjectors;

    AttachmentFactory(InternalFactory<T> internalFactory, List<? extends Key<Object>> attachmentsKeys) {
        this.internalFactory = internalFactory;
        this.attachmentsKeys = attachmentsKeys;
        this.attachmentsInjectors = new ArrayList<>(attachmentsKeys.size());
    }

    @Override
    public void initialize(InjectorImpl injector) {
        for (var key : attachmentsKeys) {
            var dependency = Dependency.of(key);
            var dependencyInjector = injector.getDependencyInjector(dependency);

            if (dependencyInjector != null) {
                attachmentsInjectors.add(dependencyInjector);
            }
        }
    }

    @Override
    public T get(InjectorContext context) throws InternalProvisionException {
        var instance = internalFactory.get(context);

        context.pushSource(instance);
        try {
            expungeAttachments();
            for (var dependencyInjector : attachmentsInjectors) {
                var target = new AttachmentTarget<>(instance, queue);
                attachments.computeIfAbsent(target, t -> new AttachmentCollection(attachmentsInjectors.size()))
                        .attach(dependencyInjector.inject(context));
            }
        } finally {
            context.popSource();
        }

        return instance;
    }

    private void expungeAttachments() {
        for (Object x; (x = queue.poll()) != null; ) {
            synchronized (queue) {
                @SuppressWarnings("unchecked")
                var target = (AttachmentTarget<T>) x;
                attachments.remove(target);
            }
        }
    }

    private static class AttachmentTarget<T> extends WeakReference<T> {

        public AttachmentTarget(T referent, ReferenceQueue<T> queue) {
            super(referent, queue);
        }
    }

    private static class AttachmentCollection {
        final List<Object> objects;

        AttachmentCollection(int capacity) {
            objects = new ArrayList<>(capacity);
        }

        void attach(Object object) {
            objects.add(object);
        }
    }
}
