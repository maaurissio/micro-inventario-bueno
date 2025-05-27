package com.perfulandia.inventario.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.inventario.model.Producto;
import com.perfulandia.inventario.model.Reserva;
import com.perfulandia.inventario.model.ReservaDTO;
import com.perfulandia.inventario.repository.ProductoRepository;
import com.perfulandia.inventario.repository.ReservaRepository;

import jakarta.transaction.Transactional;


@Service
public class ReservaService {
    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Reserva> reservas(){
        return reservaRepository.findAll();
    }

    @Transactional
    public ReservaDTO guardar(Reserva reserva, List<Integer> idProductos) {
        // Validar y buscar los productos
        List<Producto> productos = new ArrayList<>();
        for (int idProducto : idProductos) {
            Producto producto = productoRepository.findById(idProducto)
                    .orElseThrow(() -> new RuntimeException("Producto con ID " + idProducto + " no encontrado"));

            // Validar stock para cada producto
            if (producto.getCantidadDisponible() < reserva.getCantidadReserva()) {
                throw new RuntimeException("Stock insuficiente para el producto con ID " + idProducto);
            }
            productos.add(producto);
        }
        // Asignar los productos a la reserva
        reserva.setProductos(productos);
        // Reducir el stock de cada producto
        for (Producto producto : productos) {
            producto.setCantidadDisponible(producto.getCantidadDisponible() - reserva.getCantidadReserva());
            productoRepository.save(producto);
        }
        // Guardar la reserva
        Reserva savedReserva = reservaRepository.save(reserva);
        // Crear el DTO de respuesta
        ReservaDTO responseDTO = new ReservaDTO();
        responseDTO.setCantidadReserva(savedReserva.getCantidadReserva());

        int Stock = productos.get(0).getCantidadDisponible();
        if (Stock == 0) {
            responseDTO.setStockStatus("Sin stock");
        } else if (Stock < 10) {
            responseDTO.setStockStatus("Bajo stock");
        }

        return responseDTO;
    }
}
