package fuga.inject.support;

import fuga.common.errors.ErrorMessages;
import fuga.inject.KeyedWatching;
import fuga.inject.KeyedWatchingVisitor;
import fuga.inject.MatchedWatching;
import fuga.inject.MatchedWatchingVisitor;
import fuga.inject.watchings.ProvisionListenerKeyWatching;
import fuga.inject.watchings.ProvisionListenerWatching;
import fuga.inject.watchings.TypeListenerKeyWatching;
import fuga.inject.watchings.TypeListenerWatching;
import fuga.util.Matchers;

class DefaultWatchingProcessor extends AbstractWatchingProcessor {

    DefaultWatchingProcessor(Container container, ErrorMessages errorMessages) {
        super(container, errorMessages);
    }

    @Override
    public <T> boolean process(KeyedWatching<T> watching) {
        return watching.acceptVisitor(new KeyedWatchingVisitor<>() {
            @Override
            public Boolean visit(TypeListenerWatching<T> watching) {
                var interceptor = new TypeWatchingInterceptor<>(watching.getTypeListener());
                putBinding(new TypeListenerWatchingImpl<>(watching.getKey(), watching.getTypeListener(), interceptor));
                return true;
            }

            @Override
            public Boolean visit(TypeListenerKeyWatching<T> watching) {
                var listenerKey = watching.getTypeListenerKey();
                var interceptor = new TypeKeyWatchingInterceptor<>(listenerKey);
                putBinding(new TypeListenerKeyWatchingImpl<>(watching.getKey(), listenerKey, interceptor));
                return true;
            }
        });
    }

    @Override
    public boolean process(MatchedWatching watching) {
        return watching.acceptVisitor(new MatchedWatchingVisitor<Boolean>() {
            @Override
            public Boolean visit(ProvisionListenerWatching watching) {
                var interceptor = new ProvisionWatchingInterceptor(watching.getProvisionListener());
                putBinding(new ProvisionListenerWatchingImpl(watching.getMatcher(), watching.getProvisionListener(), interceptor));
                return true;
            }

            @Override
            public Boolean visit(ProvisionListenerKeyWatching watching) {
                var listenerKey = watching.getProvisionListenerKey();
                var interceptor = new ProvisionKeyWatchingInterceptor(listenerKey);
                var exceptSelf = Matchers.not(Matchers.exact(listenerKey.getFullType()));
                putBinding(new ProvisionListenerKeyWatchingImpl(watching.getMatcher().and(exceptSelf), listenerKey, interceptor));
                return true;
            }
        });
    }
}
