/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.nrm.dina.collections.api.services;

import java.io.Serializable;
import javax.ejb.Stateless;
import javax.inject.Inject; 
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes; 
import javax.ws.rs.DELETE;
import javax.ws.rs.GET; 
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;  
import se.nrm.dina.collections.logic.CollectionsLogic; 

/**
 *
 * @author idali
 */
@Path("collections/api/v01")
@Consumes({MediaType.APPLICATION_JSON+";charset=UTF-8"})
@Produces({MediaType.APPLICATION_JSON + ";charset=UTF-8"})
@Stateless
@Slf4j
public class CollectionsServices implements Serializable {
    
    @Inject
    private CollectionsLogic logic;
     
    @GET
    @Produces("text/plain")
    public Response doGet() {
        log.info("doGet");
        return Response.ok("Hello from collections api").build();
    }
    
    @GET
    @Path("/physicalUnits") 
    public Response getPhysicalUnits() {
        log.info("getPhysicalUnits"); 
        
        return Response.ok(logic.getPhysicalUnits()).build();
//        return Response.ok(logic.getAll("physicalUnit")).build();
    }
    
    @GET
    @Path("/{entity}") 
    public Response getAll(@PathParam("entity") String entity) {
        log.info("getAll"); 
        
        return Response.ok(logic.getAll(entity)).build();
    }
    
    @GET
    @Path("/{entity}/{id}") 
    public Response getById(@PathParam("entity") String entity, @PathParam("id") long id) {
        log.info("getById");

        return Response.ok(logic.getById(entity, id)).build();
    }

    @POST
    @Path("{entity}") 
    public Response createNewEntity(@Context HttpServletRequest req,
                                    @PathParam("entity") String entity, String json) {
        log.info("createNewEntity - entity: {}", json);
       
        return Response.ok(logic.saveEntity(entity, json)).build();
    }
    
    @DELETE
    @Path("/{entity}/{id}")
    public Response delete(@PathParam("entity") String entity, @PathParam("id") int id) {
        log.info("delete : {}", id);

        logic.delete(entity, id);
        return Response.ok().build();
    } 
}
