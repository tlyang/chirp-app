package com.example.chirp.app.resources;

import com.example.chirp.app.kernel.Chirp;
import com.example.chirp.app.pub.PubChirp;
import com.example.chirp.app.pub.PubChirps;
import com.example.chirp.app.pub.PubUser;
import com.example.chirp.app.stores.UserStoreUtils;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

import static org.junit.Assert.*;

public class UserResourceTest extends ResourceTestSupport {

    @Before
    public void before() {
        getUserStore().clear();
    }

    @Test
    public void testCreateUser() {
        String username = "student";
        String realName = "Bob Student";

        Form user = new Form().param("realName", realName);
        Entity entity = Entity.form(user);

        Response response = target("/users").path(username).request().put(entity);

        // The below will return 'http://localhost:9998/users/student'
        String location = response.getHeaderString("Location");

        // The below will return '/users/student'
        String location2 = response.getLocation().getPath();

        assertEquals(201, response.getStatus());
        assertTrue(location.endsWith("/users/student"));
        assertEquals("/users/student", location2);
        assertNotNull(getUserStore().getUser(username));
    }

    @Test
    public void testGetUserPlain(){
        getUserStore().createUser("mickey.mouse", "Mickey Mouse");

        String username = "mickey.mouse";
        Response response = target("/users")
                                .path(username)
                                .request(MediaType.TEXT_PLAIN_TYPE)
                                .get();

        assertEquals(200, response.getStatus());

        String retUsername = response.readEntity(String.class);
        assertEquals("Mickey Mouse", retUsername);
    }

    @Test
    public void testGetUserJson(){
        getUserStore().createUser("mickey.mouse", "Mickey Mouse");

        String username = "mickey.mouse";
        Response response = target("/users")
                .path(username)
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(200, response.getStatus());

        PubUser pubUser = response.readEntity(PubUser.class);
        assertEquals("Mickey Mouse", pubUser.getRealName());

        URI uri1 = response.getLink("self").getUri();
        assertNotNull(uri1);
        assertEquals("/users/mickey.mouse", uri1.getPath());

        URI uri2 = response.getLink("chirps").getUri();
        assertNotNull(uri2);
        assertEquals("/users/mickey.mouse/chirps", uri2.getPath());
    }


    @Test
    public void testGetUserXml(){
        getUserStore().createUser("mickey.mouse", "Mickey Mouse");

        String username = "mickey.mouse";
        Response response = target("/users")
                .path(username)
                .request(MediaType.APPLICATION_XML)
                .get();

        assertEquals(200, response.getStatus());

        PubUser pubUser = response.readEntity(PubUser.class);
        assertEquals("Mickey Mouse", pubUser.getRealName());

        URI uri1 = response.getLink("self").getUri();
        assertNotNull(uri1);
        assertEquals("/users/mickey.mouse", uri1.getPath());

        URI uri2 = response.getLink("chirps").getUri();
        assertNotNull(uri2);
        assertEquals("/users/mickey.mouse/chirps", uri2.getPath());
    }

    @Test
    public void testCreateDuplicateUser() {
        Form user = new Form().param("realName", "Mickey Mouse");
        Entity entity = Entity.form(user);

        Response response = target("/users").path("mickey").request().put(entity);
        assertEquals(201, response.getStatus());

        response = target("/users").path("mickey").request().put(entity);
        assertEquals(403, response.getStatus());
    }

    @Test
    public void testGetWrongUser(){
        Response response = target("/users").path("donald.duck").request().get();
        assertEquals(404, response.getStatus());
    }

    @Test
    public void testCreateUserWithBadName(){
        String username = "stu dent";
        String realName = "Bob Student";

        Form user = new Form().param("realName", realName);
        Entity entity = Entity.form(user);

        Response response = target("/users").path(username).request().put(entity);

        assertEquals(400, response.getStatus());
    }

    @Test
    public void testGetUserXmlExt(){
        getUserStore().createUser("mickey.mouse", "Mickey Mouse");

        String username = "mickey.mouse.xml";
        Response response = target("/users")
                .path(username)
                .request("image/png")
                .get();

        assertEquals(200, response.getStatus());

        String contentType = response.getHeaderString("Content-Type");
        assertEquals(MediaType.APPLICATION_XML, contentType);
    }

    @Test
    public void testGetUserTextExt(){
        getUserStore().createUser("mickey.mouse", "Mickey Mouse");

        String username = "mickey.mouse.txt";
        Response response = target("/users")
                .path(username)
                .request("image/png")
                .get();

        assertEquals(200, response.getStatus());

        String contentType = response.getHeaderString("Content-Type");
        assertEquals(MediaType.TEXT_PLAIN, contentType);
    }

    @Test
    public void testGetUserJsonExt(){
        getUserStore().createUser("mickey.mouse", "Mickey Mouse");

        String username = "mickey.mouse.json";
        Response response = target("/users")
                .path(username)
                .request("image/png")
                .get();

        assertEquals(200, response.getStatus());

        String contentType = response.getHeaderString("Content-Type");
        assertEquals(MediaType.APPLICATION_JSON, contentType);
    }

    @Test
    public void testCreateChirp(){
        UserStoreUtils.resetAndSeedRepository(getUserStore());
        Entity<String> entity = Entity.text("Test");

        Response response = target("users")
                .path("yoda")
                .path("chirps")
                .request()
                .post(entity);

        assertEquals(201, response.getStatus());

        PubChirp pubChirp = response.readEntity(PubChirp.class);

        String location = response.getLocation().getPath();
        assertEquals("/chirps/" + pubChirp.getId(), location);

        Chirp chirp = getUserStore().getChirp(pubChirp.getId());
        assertNotNull(chirp);
        assertEquals(chirp.getId().getId(), pubChirp.getId());
        assertEquals(chirp.getContent(), pubChirp.getContent());


        String selfPath = pubChirp.getLinks().get("self").getPath();
        assertEquals("/chirps/" + pubChirp.getId(), selfPath);

        String chirpsPath = pubChirp.getLinks().get("chirps").getPath();
        assertEquals("/users/yoda/chirps", chirpsPath);

        String userPath = pubChirp.getLinks().get("user").getPath();
        assertEquals("/users/yoda", userPath);
    }

    @Test
    public void testGetChirps(){
        UserStoreUtils.resetAndSeedRepository(getUserStore());

        Response response = target("users")
                .path("yoda")
                .path("chirps")
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(200, response.getStatus());

        PubChirps chirps = response.readEntity(PubChirps.class);
        assertEquals(2, chirps.getCount());

        String selfLink = response.getLink("self").getUri().getPath();
        assertEquals("/users/yoda/chirps", selfLink);

        String userLink = response.getLink("user").getUri().getPath();
        assertEquals("/users/yoda", userLink);
    }

    @Test
    public void testGetUserAviExt() {
        UserStoreUtils.resetAndSeedRepository(getUserStore());
        Response response = target("/users").path("yoda.avi").request().get();
        assertEquals(406, response.getStatus());
    }

    @Test
    public void testGetUserCsvExt() {
        UserStoreUtils.resetAndSeedRepository(getUserStore());
        Response response = target("/users").path("yoda.csv").request().get();
        assertEquals(404, response.getStatus());
    }
}
