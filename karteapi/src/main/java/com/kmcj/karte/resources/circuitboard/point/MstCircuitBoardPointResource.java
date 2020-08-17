/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.circuitboard.point;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author h.ishihara
 */
@RequestScoped
@Path("circuitBoardPoint")
public class MstCircuitBoardPointResource {
    @Inject
    private MstCircuitBoardPointService  mstCircuitBoardPointService;
      
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CircuitBoardPointData getCircuitBoardBySerialNumber(){
       return this.mstCircuitBoardPointService.getCircuitBoardPoints();
    }
}
