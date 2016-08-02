

package com.example.chirp.app.providers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
// All classes with an @Provider or @Path annotation must now
// have the @Component annotation for RESTEasy-Spring to work properly
// This notifies Spring that this is an Injectable class
@Component
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public Response toResponse(WebApplicationException exception) {
        int status = exception.getResponse().getStatus();
        String message = (exception.getMessage() != null) ?
                            exception.getMessage()
                            : exception.getClass().getName();

        if (status < 500) {
            log.info(message, exception);
        } else {
            log.error(message, exception);
        }

        ExceptionInfo exceptionInfo = new ExceptionInfo(status, message);

        return Response.status(status)
                        .entity(exceptionInfo)
                        .build();
    }
}
