package com.example.chirp.app.resources;

import com.example.chirp.app.kernel.Chirp;
import com.example.chirp.app.pub.PubChirp;
import com.example.chirp.app.pub.PubUtils;
import com.example.chirp.app.stores.UserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/chirps")
// All classes with an @Provider or @Path annotation must now
// have the @Component annotation for RESTEasy-Spring to work properly
@Component
public class ChirpResource {

    @Context // "Dependency injection" of jaxrs resources
    private UriInfo uriInfo;

    private UserStore userStore;

    @Autowired
    public ChirpResource(UserStore userStore) {
        this.userStore = userStore;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/{chirpId}")
    public Response getUser(@PathParam("chirpId") String chirpId){
        Chirp chirp = userStore.getChirp(chirpId);

        PubChirp pubChirp = PubUtils.toPubChirp(uriInfo, chirp);

        Response.ResponseBuilder builder = PubUtils.addLinks(Response.ok(pubChirp), pubChirp.getLinks());
        return builder.build();
    }
}
