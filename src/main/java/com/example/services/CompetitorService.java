/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.services;

import com.example.PersistenceManager;
import com.example.models.Competitor;
import com.example.models.CompetitorDTO;
import com.example.models.Producto;
import com.example.models.ProductoDTO;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;

/**
 *
 * @author Mauricio
 */
@Path("/competitors")
@Produces(MediaType.APPLICATION_JSON)
public class CompetitorService {

    @PersistenceContext(unitName = "CompetitorsPU")
    EntityManager entityManager;
    
    @PostConstruct
    public void init(){
        
        try{
            entityManager = PersistenceManager.getInstance().getEntityManagerFactory().createEntityManager();
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {

        Query q  = entityManager.createQuery("select u from Competitor u order by u.surname ASC");
        
        List<Competitor> competitors = q.getResultList();
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(competitors).build();
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCompetitor(CompetitorDTO competitor) {

        JSONObject respuesta = new JSONObject();
        Competitor competitorTmp= new Competitor(competitor.getName(), competitor.getSurname(), competitor.getAge(), competitor.getTelephone(), competitor.getCellphone(), competitor.getAddress(), competitor.getCity(), competitor.getCountry(), false, competitor.getPassword());    
       
        Iterator it = competitor.getProductos().iterator();
        
        while (it.hasNext()) {
            Producto obj = (Producto)it.next();
            Producto pr = new Producto(obj.getName(), obj.getPrecio(), obj.getDescription());
            pr.setCompetitor(competitorTmp);
            competitorTmp.getProducts().add(pr);
            
        }
        
     try{    
        entityManager.getTransaction().begin();
        entityManager.persist(competitorTmp);
        entityManager.getTransaction().commit();
        entityManager.refresh(competitorTmp);
        respuesta.put("competitor_id", competitorTmp.getId());
        
        }catch(Throwable t){
            t.printStackTrace();
            if(entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
                competitorTmp = null;
        }finally{
         entityManager.clear();
         entityManager.close();
     }
     
        
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(respuesta).build();
    }
    
    @POST
    @Path("/addProduct")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createProduct(ProductoDTO producto) {

        JSONObject respuesta = new JSONObject();
        Producto productoTmp= new Producto(producto.getName(), producto.getPrecio(), producto.getDescription());    
        
        
     try{    
        entityManager.getTransaction().begin();
        entityManager.persist(productoTmp);
        entityManager.getTransaction().commit();
        entityManager.refresh(productoTmp);
        respuesta.put("producto_id", productoTmp.getId());
        
        }catch(Throwable t){
            t.printStackTrace();
            if(entityManager.getTransaction().isActive())
                entityManager.getTransaction().rollback();
                productoTmp = null;
        }finally{
         entityManager.clear();
         entityManager.close();
     }
     
      return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(respuesta).build();
      
    }
    
    @GET
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response Login(CompetitorDTO competitor){
        
        JSONObject respuesta = new JSONObject();
        Competitor competitorTmp = new Competitor(competitor.getName(), competitor.getSurname(), competitor.getAge(), competitor.getTelephone(), competitor.getCellphone(), competitor.getAddress(), competitor.getCity(), competitor.getCountry(), false, competitor.getPassword());
        
       Query q  = entityManager.createQuery("select u from Producto u where u.competitor.address = '"+competitorTmp.getAddress()+"' and u.competitor.password = '"+competitorTmp.getPassword()+"'");
       List<Competitor> competitors = q.getResultList();
        if (competitors.isEmpty()) {
            System.err.println("Error(?");
        }
    
        
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(competitors).build();
        
    }
    

}
