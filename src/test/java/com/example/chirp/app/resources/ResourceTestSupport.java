package com.example.chirp.app.resources;

import javax.ws.rs.core.Application;

import com.example.chirp.app.stores.UserStore;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import ch.qos.logback.classic.Level;

import com.example.chirp.app.support.LogbackUtil;
import com.fasterxml.jackson.jaxrs.xml.JacksonXMLProvider;

public abstract class ResourceTestSupport extends JerseyTest {

    private Application application;

    public static UserStore userStore;

    @Override
    protected Application configure() {
        // Set the TEST spring profile
        System.getProperties().setProperty("spring.profiles.active", "test");

        LogbackUtil.initLogback(Level.WARN);

        application = new ChirpApplication();
        ResourceConfig resourceConfig = ResourceConfig.forApplication(application);
        resourceConfig.packages("com.example.chirp.app");

        // Use the TEST spring config file
        resourceConfig.property("contextConfigLocation", "classpath:/chirp-test-spring.xml");

        return resourceConfig;
    }

    @Override
    protected void configureClient(ClientConfig config) {
        config.register(JacksonXMLProvider.class);
        super.configureClient(config);
    }

    public UserStore getUserStore() {
        return userStore;
    }
}
