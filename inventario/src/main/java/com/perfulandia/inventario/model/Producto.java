package com.perfulandia.inventario.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "productos")
@NoArgsConstructor
@AllArgsConstructor

public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idProducto;
    
    @Column(length = 50, nullable = true)
    private String nombre;

    @Column(nullable = true)
    private int cantidadDisponible;

    @Column(length = 50, nullable = true)
    private String aroma;

    @Column(length = 250, nullable = true)
    private String descripcion;

    @Column(length = 50, nullable = true)
    private String marca;

    @Column(nullable = true)
    private double ml;

    @Column(nullable = true)
    private double precio;

    //Relacion con la tabla reserva
    @OneToOne(mappedBy = "producto")
    @JsonIgnore
    private Reserva reserva;
}
