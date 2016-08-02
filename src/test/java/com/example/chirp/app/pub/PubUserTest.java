package com.example.chirp.app.pub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.Test;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class PubUserTest {

    @Test
    public void testJsonTranslation() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        Map<String, URI> links = new LinkedHashMap<>();
        links.put("self", URI.create("http://whatever/a"));
        links.put("chirps", URI.create("http://whatever/b"));

        PubUser oldUser = new PubUser(links, "mickey", "Mickey Mouse");
        String json = mapper.writeValueAsString(oldUser);
        PubUser newUser = mapper.readValue(json, PubUser.class);

        assertEquals(oldUser.getUsername(), newUser.getUsername());
        assertEquals(oldUser.getRealName(), newUser.getRealName());
        assertEquals(oldUser.getLinks(), newUser.getLinks());
    }

    @Test
    public void testXmlTranslation() throws Exception {
        XmlMapper mapper = new XmlMapper();

        Map<String, URI> links = new LinkedHashMap<>();
        links.put("self", URI.create("http://whatever/a"));
        links.put("chirps", URI.create("http://whatever/b"));

        PubUser oldUser = new PubUser(links, "mickey", "Mickey Mouse");
        String json = mapper.writeValueAsString(oldUser);
        PubUser newUser = mapper.readValue(json, PubUser.class);

        assertEquals(oldUser.getUsername(), newUser.getUsername());
        assertEquals(oldUser.getRealName(), newUser.getRealName());
        assertEquals(oldUser.getLinks(), newUser.getLinks());
    }
}
