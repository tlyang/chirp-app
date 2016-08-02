package com.example.chirp.app.resources;

import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class GreetingsResourceTest extends ResourceTestSupport {

    @Test
    public void testSayHello(){
        Response response = target("/greetings").request().get();
        int status = response.getStatus();
        assertEquals(200, status);

        String text = response.readEntity(String.class);
        assertEquals("Hello Dude!", text);
    }

    @Test
    public void testSayHelloWithQueryParam() throws Exception {
        Response response =  target("/greetings").queryParam("name", "Tom").request().get();
        int status = response.getStatus();

        assertEquals(200, status);

        String text = response.readEntity(String.class);
        assertEquals("Hello Tom!", text);
    }

    @Test
    public void testSayHelloWithPathParam() throws Exception {
        Response response =  target("/greetings").path("Tom").request().get();
        int status = response.getStatus();

        assertEquals(200, status);

        String text = response.readEntity(String.class);
        assertEquals("Hello Tom!", text);
    }

    @Test
    public void testEchoHeader() throws Exception {
        Response response =  target("/greetings").request().header("X-NewCircle-Echo", "Tom").get();
        int status = response.getStatus();

        assertEquals(200, status);

        String text = response.getHeaderString("X-NewCircle-Echo-Response");
        assertEquals("Tom Tom Tom", text);
    }
}
