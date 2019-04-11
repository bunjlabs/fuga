/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bunjlabs.fuga.app;

import java.lang.reflect.Constructor;

/**
 * @author Artem Shurygin <artem.shurygin@bunjlabs.com>
 */
public abstract class FugaApp {


    public FugaApp() {
    }

    /**
     * Helper method that starts the application by given application class.
     *
     * @param appClass Aapplication class.
     */
    public static void launch(Class<? extends FugaApp> appClass) {
        try {
            Constructor<? extends FugaApp> constructor = appClass.getConstructor();
            FugaApp app = constructor.newInstance();
            app.start();
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void start() throws Exception {
        prepare();
    }

    public abstract void prepare() throws Exception;
}
