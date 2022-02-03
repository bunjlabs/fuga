package fuga.settings.support;

class SettingsAgent {
    private final Class<?> proxiedClass;
    private final Object proxyObject;

    SettingsAgent(Class<?> proxiedClass, Object proxyObject) {
        this.proxiedClass = proxiedClass;
        this.proxyObject = proxyObject;
    }

    public Class<?> getProxiedClass() {
        return proxiedClass;
    }

    public Object getProxyObject() {
        return proxyObject;
    }
}
