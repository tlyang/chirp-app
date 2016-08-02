package com.example.chirp.app.resources;

import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/greetings")
// All classes with an @Provider or @Path annotation must now
// have the @Component annotation for RESTEasy-Spring to work properly
// This notifies Spring that this is an Injectable class
@Component
public class GreetingsResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response getGreeting(@DefaultValue("Dude") @QueryParam("name") String name,
                                @HeaderParam("X-NewCircle-Echo") String headerValue){
        String greeting = String.format("Hello %s!", name);
        String echo = headerValue + " " + headerValue + " " + headerValue;
        return Response.ok(greeting)
                .header("X-NewCircle-Echo-Response", echo)
                .build();
    }

    @GET
    @Path("/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getGreetingWithPath(@PathParam("name") String name){
        return String.format("Hello %s!", name);
    }
}
