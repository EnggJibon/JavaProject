/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.language;

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
@Path("language")
public class MstLanguageResource {

    @Context
    private UriInfo context;
    
    @Inject
    private MstLanguageService mstLanguageService;

    /**
     * Creates a new instance of MstLanguageResource
     */
    public MstLanguageResource() {
    }

    /**
     * Retrieves representation of an instance of com.kmcj.karte.resources.language.MstLanguageResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MstLanguageList getLanguage() {
        return mstLanguageService.getMstLanguages();
    }

    /**
     * PUT method for updating or creating an instance of MstLanguageResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
}
