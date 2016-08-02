package com.example.chirp.app.resources;

import com.example.chirp.app.stores.InMemoryUserStore;
import com.example.chirp.app.stores.UserStore;
import com.fasterxml.jackson.jaxrs.xml.JacksonXMLProvider;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

// @ApplicationPath("/")
public class ChirpApplication extends Application {
    private Set<Class<?>> classes = new HashSet<>();

    public ChirpApplication() {
        classes.add(JacksonXMLProvider.class);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}
