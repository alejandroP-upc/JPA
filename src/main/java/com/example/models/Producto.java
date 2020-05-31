 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.models;

import com.sun.istack.NotNull;
import java.io.Serializable;
import java.util.Calendar;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author usuario
 */
@Entity
public class Producto implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    @NotNull
    @Column(name = "created_at",updatable = false)
    @Temporal(TemporalType.DATE)
    private Calendar createdAt;
    
    @NotNull
    @Column(name = "updated_at")
    @Temporal(TemporalType.DATE)
    private Calendar updatedAt;
    
    private String name;
    private Double precio;
    private String description;
    @ManyToOne(cascade = ALL)
    private Competitor competitor;

    public Producto() {
    }

    public Producto(String name, Double precio, String description) {
        this.name = name;
        this.precio = precio;
        this.description = description;
    }

    public long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    
    public void setCompetitor(Competitor competitor) {
        this.competitor = competitor;
    }
    
    @PreUpdate
    private void updateTimestamp(){
        this.updatedAt=Calendar.getInstance();
    }
    
    @PrePersist
    private void creationTimestamp(){
        this.createdAt = Calendar.getInstance();
        this.updatedAt = Calendar.getInstance();
    }
    
    
    
}
