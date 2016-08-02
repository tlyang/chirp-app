package com.example.chirp.app.resources;

import com.example.chirp.app.pub.PubChirp;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class ChirpResourceTest extends ResourceTestSupport{

    @Test
    public void testGetChirpJson() {
        testGetChirp(MediaType.APPLICATION_JSON);
    }

    @Test
    public void testGetChirpXml() {
        testGetChirp(MediaType.APPLICATION_XML);
    }

    public void testGetChirp(String mediaType) {
        Response response =  target("chirps")
                                .path("wars01")
                                .request(mediaType)
                                .get();

        assertEquals(200, response.getStatus());
        assertEquals(mediaType, response.getHeaderString("Content-Type"));

        PubChirp chirp = response.readEntity(PubChirp.class);
        assertEquals("wars01", chirp.getId());
        assertEquals("Do or do not. There is no try.", chirp.getContent());

        String selfPath = chirp.getLinks().get("self").getPath();
        assertEquals("/chirps/wars01", selfPath);

        String chirpsPath = chirp.getLinks().get("chirps").getPath();
        assertEquals("/users/yoda/chirps", chirpsPath);

        String userPath = chirp.getLinks().get("user").getPath();
        assertEquals("/users/yoda", userPath);
    }
}
