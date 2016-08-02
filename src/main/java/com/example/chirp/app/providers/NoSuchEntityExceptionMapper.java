package com.example.chirp.app.providers;

import com.example.chirp.app.kernel.exceptions.NoSuchEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
// All classes with an @Provider or @Path annotation must now
// have the @Component annotation for RESTEasy-Spring to work properly
// This notifies Spring that this is an Injectable class
@Component
public class NoSuchEntityExceptionMapper implements ExceptionMapper<NoSuchEntityException> {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public Response toResponse(NoSuchEntityException exception) {
        String message = (exception.getMessage() != null) ?
                exception.getMessage()
                : exception.getClass().getName();
        log.info(message, exception);
        return Response.status(404).entity(message).build();
    }
}
