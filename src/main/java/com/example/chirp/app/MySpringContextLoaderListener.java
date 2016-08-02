package com.example.chirp.app;

import org.jboss.resteasy.plugins.spring.SpringContextLoaderListener;

import javax.servlet.ServletContextEvent;

/**
 * Override the default SpringContextLoaderListener.<br>
 * This will catch any exception that occurs during startup and print the stacktrace.
 */
public class MySpringContextLoaderListener extends SpringContextLoaderListener{

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            super.contextInitialized(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
