package org.kai.od.core;

public class OdContext {



    private OdContext() {}

    public static OdContext getContext() {
        return OdContextHolder.SINGLETON.odContext;
    }

    private enum OdContextHolder {
        SINGLETON;

        OdContext odContext;

        OdContextHolder() {
            odContext = new OdContext();
        }
    }
}
