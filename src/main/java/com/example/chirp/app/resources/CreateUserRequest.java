package com.example.chirp.app.resources;


import javax.ws.rs.BadRequestException;
import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

public class CreateUserRequest {
    @PathParam("username")
    private String username;

    @FormParam("realName")
    private String realName;

    @Context
    private UriInfo uriInfo;

    public void validate(){
        if(getUsername().contains(" ")){
            throw new BadRequestException("The username cannot contain a space.");
        }
    }

    public UriInfo getUriInfo() {
        return uriInfo;
    }

    public void setUriInfo(UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

}
