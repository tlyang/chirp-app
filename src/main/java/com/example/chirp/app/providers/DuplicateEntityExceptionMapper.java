package com.example.chirp.app.providers;

import com.example.chirp.app.kernel.exceptions.DuplicateEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Single Exception mapper for DuplicateEntityException.<br>
 * This class will automatically return HTTP 403 if the DuplicateEntityException is thrown.
 */
@Provider
// All classes with an @Provider or @Path annotation must now
// have the @Component annotation for RESTEasy-Spring to work properly
// This notifies Spring that this is an Injectable class
@Component
public class DuplicateEntityExceptionMapper implements ExceptionMapper<DuplicateEntityException> {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public Response toResponse(DuplicateEntityException exception) {
        String message = (exception.getMessage() != null) ?
                exception.getMessage() : exception.getClass().getName();
        log.info(message, exception);
        return Response.status(403).entity(message).build();
    }
}