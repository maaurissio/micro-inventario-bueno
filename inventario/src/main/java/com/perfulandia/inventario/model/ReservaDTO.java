package com.perfulandia.inventario.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ReservaDTO {
    private int idReserva;
    private int idCliente;
    private int cantidadReserva;
    private int idProducto;
    private String stockStatus;
}
