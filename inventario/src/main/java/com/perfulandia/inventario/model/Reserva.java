package com.perfulandia.inventario.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "reservas")
@NoArgsConstructor
@AllArgsConstructor

public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idReserva;
    @Column(nullable = true)
    private int idCliente;
    @Column(nullable = true)
    private int cantidadReserva;


    //Relacion
    @ManyToMany
    @JoinTable(
        name = "producto_reserva",
        joinColumns = @JoinColumn(name = "idReserva"),
        inverseJoinColumns = @JoinColumn(name = "idProducto"))
    private List<Producto> productos = new ArrayList<>();
}
