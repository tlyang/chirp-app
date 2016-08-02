package com.example.chirp.app.providers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Map;

/**
 * This class filters the URL for any *.xml, *.json, *.txt
 * and adds a request accept header for that file type.
 */
@Provider
@PreMatching    //Request filter before matching to a resource
@Priority(Priorities.HEADER_DECORATOR)
// All classes with an @Provider or @Path annotation must now
// have the @Component annotation for RESTEasy-Spring to work properly
// This notifies Spring that this is an Injectable class
@Component
public class FileExtensionRequestFilter implements ContainerRequestFilter {

    // Inject this map.  Parameter is optional, but for a HashMap, there would be no way to map it back to the config.
    // Can also map by method name in the Configuration class.
    // NOTE: @Value is used for primitives, strings, collections, and maps.
    //          Otherwise, it is similar to the @Autowire annotation.
    @Value("#{fileExtensionMap}")
    public Map<String, String> extMediaTypes;

    public FileExtensionRequestFilter() {}

    @Override
    public void filter(ContainerRequestContext rc) throws IOException {
        UriInfo uriInfo = rc.getUriInfo();
        String path = uriInfo.getRequestUri().getRawPath();
        int extIndex = path.lastIndexOf('.');

        if (extIndex == -1) { return; } // no extension, just return.

        String ext = path.substring(extIndex).toLowerCase();
        String basePath = path.substring(0, extIndex);
        String mediaType = extMediaTypes.get(ext);

        if (mediaType == null) { return; } // not a known type, just return.

        rc.getHeaders().putSingle(HttpHeaders.ACCEPT, mediaType);
        rc.setRequestUri(uriInfo.getRequestUriBuilder().replacePath(basePath).build());
    }
}