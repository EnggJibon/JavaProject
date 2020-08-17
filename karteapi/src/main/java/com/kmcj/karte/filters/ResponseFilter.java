/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.filters;

import com.kmcj.karte.constants.RequestParameters;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author f.kitaoji
 */
@Provider
public class ResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        String apiToken = (String)requestContext.getProperty(RequestParameters.APITOKEN);
        if (apiToken != null) {
            MultivaluedMap<String,Object> headers = responseContext.getHeaders();
            headers.add(RequestParameters.APITOKEN, apiToken);
        }
    }
    
}
