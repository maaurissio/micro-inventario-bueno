package com.perfulandia.inventario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Column(length = 50, nullable = true)
    private String aroma;

    @Column(length = 250, nullable = true)
    private String descripcion;

    @Column(length = 50, nullable = true)
    private String marca;

    private double ml;
    private double precio;

}
