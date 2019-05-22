package com.bunjlabs.fuga.remoting;

public interface RemoteExporter {
    RemoteCallResult handleCall(RemoteCall call) throws NoSuchMethodException, IllegalAccessException;
}
