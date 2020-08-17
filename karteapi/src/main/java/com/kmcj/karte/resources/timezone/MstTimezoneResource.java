/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.timezone;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author f.kitaoji
 */
@RequestScoped
@Path("timezone")
public class MstTimezoneResource {

    @Context
    private UriInfo context;
    
    @Inject
    private MstTimezoneService mstTimezoneService;

    /**
     * Creates a new instance of MstTimezoneResource
     */
    public MstTimezoneResource() {
    }

    /**
     * Retrieves representation of an instance of com.kmcj.karte.resources.timezone.MstTimezoneResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("available")
    public MstTimezoneList getAvailableTimezone() {
        return mstTimezoneService.getAvailableMstLanguages();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MstTimezoneList getAllTimezone() {
        return mstTimezoneService.getAllMstLanguages();
    }

    /**
     * PUT method for updating or creating an instance of MstTimezoneResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}
