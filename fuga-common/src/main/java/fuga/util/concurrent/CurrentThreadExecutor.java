package fuga.util.concurrent;

import java.util.concurrent.Executor;

public class CurrentThreadExecutor implements Executor {
    public static final Executor INSTANCE = new CurrentThreadExecutor();

    public void execute(Runnable r) {
        r.run();
    }
}
