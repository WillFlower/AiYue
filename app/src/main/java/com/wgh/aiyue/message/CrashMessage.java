package com.wgh.aiyue.message;

/**
 * Created by   : WGH.
 */
public class CrashMessage {
    public int what;
    public String key;
    public Thread thread;
    public Throwable throwable;
    public Thread.UncaughtExceptionHandler exceptionHandler;
}
