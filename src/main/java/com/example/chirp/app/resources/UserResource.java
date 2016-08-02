package com.example.chirp.app.resources;

import com.example.chirp.app.kernel.Chirp;
import com.example.chirp.app.kernel.ChirpId;
import com.example.chirp.app.kernel.User;
import com.example.chirp.app.pub.PubChirp;
import com.example.chirp.app.pub.PubChirps;
import com.example.chirp.app.pub.PubUser;
import com.example.chirp.app.pub.PubUtils;
import com.example.chirp.app.stores.UserStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("/users")
// All classes with an @Provider or @Path annotation must now
// have the @Component annotation for RESTEasy-Spring to work properly
// This notifies Spring that this is an Injectable class
@Component
public class UserResource {

    @Context // "Dependency injection" of jaxrs resources
    private UriInfo uriInfo;

    private UserStore userStore;

    public UserResource(){}

    @Autowired  // Spring will search the config for an implementation of the userStore in the bean factory
    public void setUserStore(UserStore userStore){
        this.userStore = userStore;
    }

    @PUT
    @Path("/{username}")
    public Response createUser(@BeanParam CreateUserRequest request) {
        request.validate();

        userStore.createUser(request.getUsername(), request.getRealName());

       /*
        * method getBaseUriBuilder() Starts from end of server
        */
        URI uri = uriInfo.getBaseUriBuilder()
                .path("/users")
                .path(request.getUsername())
                .build();

        return Response.created(uri).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
    @Path("/{username}")
    public Response getUser(@PathParam("username") String username) {
        User user = userStore.getUser(username);
        PubUser pubUser = PubUtils.toPubUser(uriInfo, user);

        Response.ResponseBuilder builder = PubUtils.addLinks(Response.ok(pubUser), pubUser.getLinks());
        return builder.build();
    }

    @POST
    @Path("/{username}/chirps")
    public Response createChirp(@PathParam("username") String username,
                                String content) {
        User user = userStore.getUser(username);

        ChirpId chirpId = new ChirpId();
        Chirp chirp = new Chirp(chirpId, content, user);
        user.addChirp(chirp);

        userStore.updateUser(user);

        PubChirp pubChirp = PubUtils.toPubChirp(uriInfo, chirp);

        URI uri = pubChirp.getLinks().get("self");

        Response.ResponseBuilder builder = Response.created(uri).entity(pubChirp);
        PubUtils.addLinks(builder, pubChirp.getLinks());
        return builder.build();
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/{username}/chirps")
    public Response getChirps(@PathParam("username") String username,
                              @DefaultValue("5") @QueryParam("limit") String limit,
                              @DefaultValue("0") @QueryParam("offset") String offset,
                              @DefaultValue("false") @QueryParam("detail") String detail){

        User user = userStore.getUser(username);
        PubChirp pubChirp = null;

        PubChirps pubChirps = PubUtils.toPubChirps(uriInfo, user, limit, offset, detail);

        Response.ResponseBuilder builder = PubUtils.addLinks(Response.ok(pubChirps), pubChirps.get_links() );

        return builder.build();
    }
}

