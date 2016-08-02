

package com.example.chirp.app.pub;

import com.example.chirp.app.kernel.User;
import org.junit.Test;

import java.net.URI;

import static org.junit.Assert.assertEquals;

public class PubUtilsTest {

    @Test
    public void testToPubUser() {
        User user = new User("mickey", "Mickey Mouse");
        PubUser pubUser = PubUtils.toPubUser(new MockUriInfo(), user);

        assertEquals(user.getUsername(), pubUser.getUsername());
        assertEquals(user.getRealName(), pubUser.getRealName());
        assertEquals(URI.create("http://mock.com/users/mickey"), pubUser.getLinks().get("self"));
        assertEquals(URI.create("http://mock.com/users/mickey/chirps"), pubUser.getLinks().get("chirps"));
    }
}
