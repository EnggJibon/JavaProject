/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.filters;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author f.kitaoji
 */
@Provider
@Priority(Priorities.AUTHORIZATION)
@RequestScoped
public class AuthorizationFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) {
        //System.out.println("Authorization Filter ---------------------*");
    }
    
}
