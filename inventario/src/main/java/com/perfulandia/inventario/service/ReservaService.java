package com.perfulandia.inventario.service;

import java.util.List;
import java.util.Optional;

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
    
    // @Transactional
    // public Reserva guardar(Reserva reserva){
    //     int idProducto = reserva.getProducto().getIdProducto();
    //     Producto producto = productoRepository.findById(idProducto).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    //     if(producto.getCantidadDisponible() < reserva.getCantidadReserva()){
    //         throw new RuntimeException("Stock insuficiente");
    //     }
    //     reserva.setProducto(producto);
    //     producto.setCantidadDisponible(producto.getCantidadDisponible() - reserva.getCantidadReserva());
    //     productoRepository.save(producto);
    //     return reservaRepository.save(reserva);
    // }

    @Transactional
    public ReservaDTO guardar(Reserva reserva) {
        // Obtener el idProducto del objeto Producto dentro de Reserva
        int idProducto = reserva.getProducto().getIdProducto();

        // Buscar el producto por id
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Verificar si ya existe una Reserva para este Producto
        Optional<Reserva> existingReserva = reservaRepository.findAll().stream()
                .filter(r -> r.getProducto().getIdProducto() == idProducto)
                .findFirst();

        if (existingReserva.isPresent()) {
            throw new RuntimeException("El producto con id " + idProducto + " ya tiene una reserva asociada");
        }

        // Validar stock
        if (producto.getCantidadDisponible() < reserva.getCantidadReserva()) {
            throw new RuntimeException("Stock insuficiente");
        }

        // Asignar el producto encontrado a la reserva
        reserva.setProducto(producto);

        // Reducir stock
        producto.setCantidadDisponible(producto.getCantidadDisponible() - reserva.getCantidadReserva());
        productoRepository.save(producto);

        // Guardar reserva
        Reserva savedReserva = reservaRepository.save(reserva);

        // Crear el DTO de respuesta
        ReservaDTO responseDTO = new ReservaDTO();
        responseDTO.setIdReserva(savedReserva.getIdReserva());
        responseDTO.setIdCliente(savedReserva.getIdCliente());
        responseDTO.setCantidadReserva(savedReserva.getCantidadReserva());
        responseDTO.setIdProducto(idProducto);

        // Determinar el estado del stock
        int newStock = producto.getCantidadDisponible();
        if (newStock == 0) {
            responseDTO.setStockStatus("Sin stock");
        } else if (newStock < 10) {
            responseDTO.setStockStatus("Bajo stock");
        } else {
            responseDTO.setStockStatus("Stock suficiente");
        }

        return responseDTO;
    }
}
