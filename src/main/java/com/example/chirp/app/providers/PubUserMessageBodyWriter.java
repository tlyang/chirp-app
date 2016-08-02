package com.example.chirp.app.providers;

import com.example.chirp.app.pub.PubUser;
import org.springframework.stereotype.Component;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * This class creates a provider to allow performing a GET for a user
 *  with MediaType.TEXT_PLAIN
 */
@Provider
@Produces(MediaType.TEXT_PLAIN)
// All classes with an @Provider or @Path annotation must now
// have the @Component annotation for RESTEasy-Spring to work properly
// This notifies Spring that this is an Injectable class
@Component
public class PubUserMessageBodyWriter implements MessageBodyWriter<PubUser> {

    @Override
    public void writeTo(PubUser pubUser,
                        Class<?> aClass,
                        Type type,
                        Annotation[] annotations,
                        MediaType mediaType,
                        MultivaluedMap<String, Object> multivaluedMap,
                        OutputStream entityStream
    ) throws IOException, WebApplicationException {
        String content = pubUser.getRealName();
        entityStream.write(content.getBytes());
    }

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    /**
     * This method is left over from JAX-RS 1.0.
     * Just return -1
     * @param pubUser
     * @param aClass
     * @param type
     * @param annotations
     * @param mediaType
     * @return
     */
    @Override
    public long getSize(PubUser pubUser, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }
}