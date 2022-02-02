package fuga.inject.support;

import fuga.common.Key;
import fuga.lang.TypeLiteral;
import fuga.util.Matcher;

class BindingAttachment<T> extends BindingMatcher<T> {

    private final Key<?> attachmentKey;

    BindingAttachment(Matcher<? super TypeLiteral<T>> matcher, Key<?> attachmentKey) {
        super(matcher);
        this.attachmentKey = attachmentKey;
    }

    public Key<Object> getAttachmentKey() {
        return castedKey();
    }

    @SuppressWarnings("unchecked")
    private Key<Object> castedKey() {
        return (Key<Object>) attachmentKey;
    }
}
