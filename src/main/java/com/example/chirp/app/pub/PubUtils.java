package com.example.chirp.app.pub;

import com.example.chirp.app.kernel.Chirp;
import com.example.chirp.app.kernel.User;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.*;

public class PubUtils {
    public static PubUser toPubUser(UriInfo uriInfo, User user) {
        Map<String, URI> links = new LinkedHashMap<>();

        links.put("self", uriInfo.getBaseUriBuilder()
                .path("users")
                .path(user.getUsername())
                .build());

        links.put("chirps", uriInfo.getBaseUriBuilder()
                .path("users")
                .path(user.getUsername())
                .path("chirps")
                .build());

        return new PubUser(links, user.getUsername(), user.getRealName());
    }

    public static PubChirp toPubChirp(UriInfo uriInfo, Chirp chirp) {
        User user = chirp.getUser();
        Map<String, URI> links = new LinkedHashMap<>();

        links.put("self", uriInfo.getBaseUriBuilder()
                .path("chirps").path(chirp.getId().getId()).build());

        links.put("chirps", uriInfo.getBaseUriBuilder()
                .path("users").path(user.getUsername()).path("chirps").build());

        links.put("user", uriInfo.getBaseUriBuilder()
                .path("users").path(user.getUsername()).build());

        return new PubChirp(links, chirp.getId().getId(), chirp.getContent());
    }

    private static boolean verifyDetail(String detailString) {
        boolean detail = false;
        if (detailString == null) {
            detail = false;
        } else if (detailString.trim().equalsIgnoreCase("true")) {
            detail = true;
        } else if (detailString.trim().equalsIgnoreCase("false")) {
            detail = false;
        }
        return detail;
    }

    private static int verifyParam(String name, String param) {
        int val;
        try {
            val = Integer.valueOf(param);
        } catch (Exception e) {
            throw new BadRequestException("The " + name + " must be an integral value.");
        }
        return val;
    }

    public static PubChirps toPubChirps(UriInfo uriInfo, User user, String limitString, String offsetString, String detailString) {
        boolean detail = verifyDetail(detailString);
        int limit = verifyParam("limit", limitString);
        int offset = verifyParam("offset", offsetString);

        int total = user.getChirps().size();
        int count = 0;

        int index = 0;
        List<PubChirp> pubChirps = new ArrayList<>();
        List<URI> itemLinks = new ArrayList<>();

        for (Chirp chirp : user.getChirps()) {
            if (index < offset) {
                index++;
                continue;

            } else if (index >= offset+limit) {
                index++;
                continue;
            }

            PubChirp pubChirp = PubUtils.toPubChirp(uriInfo, chirp);
            pubChirps.add(pubChirp);

            itemLinks.add(pubChirp.getLinks().get("self"));

            count++;
            index++;
        }

        Map<String, URI> links = createChirpsLinks(uriInfo, user, limit, offset);

        return new PubChirps(links,
                            detail ? pubChirps : null,
                            limit,
                            offset,
                            total,
                            count,
                            detail ? null : itemLinks);
    }

    private static Map<String, URI> createChirpsLinks(UriInfo uriInfo, User user, int limit, int offset) {
        Map<String,URI> links = new LinkedHashMap<>();

        links.put("self", uriInfo.getBaseUriBuilder()
                .path("users")
                .path(user.getUsername())
                .path("chirps")
                .queryParam("offset", offset)
                .queryParam("limit", limit)
                .build());

        links.put("user", uriInfo.getBaseUriBuilder()
                .path("users")
                .path(user.getUsername())
                .build());

        links.put("first", uriInfo.getBaseUriBuilder()
                .path("users")
                .path(user.getUsername())
                .path("chirps")
                .queryParam("offset", Math.min(0, offset))
                .queryParam("limit", limit)
                .build());

        links.put("last", uriInfo.getBaseUriBuilder()
                .path("users")
                .path(user.getUsername())
                .path("chirps")
                .queryParam("offset", Math.max(0, user.getChirps().size() - limit))
                .queryParam("limit", limit)
                .build());

        links.put("prev", uriInfo.getBaseUriBuilder()
                .path("users")
                .path(user.getUsername())
                .path("chirps")
                .queryParam("offset", offset - limit)
                .queryParam("limit", limit)
                .build());

        links.put("next", uriInfo.getBaseUriBuilder()
                .path("users")
                .path(user.getUsername())
                .path("chirps")
                .queryParam("offset", offset + limit)
                .queryParam("limit", limit)
                .build());
        return links;
    }

    public static Response.ResponseBuilder addLinks(Response.ResponseBuilder builder, Map<String, URI> links) {
        for (Map.Entry<String, URI> link : links.entrySet()) {
            builder.link(link.getValue(), link.getKey());
        }
        return builder;
    }
}