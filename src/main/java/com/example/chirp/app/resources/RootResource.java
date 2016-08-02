package com.example.chirp.app.resources;

import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.text.SimpleDateFormat;

@Path("/")
// All classes with an @Provider or @Path annotation must now
// have the @Component annotation for RESTEasy-Spring to work properly
// This notifies Spring that this is an Injectable class
@Component
public class RootResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getStatus() {
        java.util.Date now = new java.util.Date();
        String when = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss").format(now);
        return String.format("As of %s, everything is OK.", when);
    }
}
