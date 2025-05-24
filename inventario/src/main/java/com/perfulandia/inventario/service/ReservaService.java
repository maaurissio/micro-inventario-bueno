package com.perfulandia.inventario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.inventario.model.Producto;
import com.perfulandia.inventario.model.Reserva;
import com.perfulandia.inventario.repository.ProductoRepository;
import com.perfulandia.inventario.repository.ReservaRepository;


@Service
public class ReservaService {
    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Reserva> reservas(){
        return reservaRepository.findAll();
    }
    
    public Reserva guardar(Reserva reserva){
        int idProducto = reserva.getProducto().getIdProducto();
        Producto producto = productoRepository.findById(idProducto).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        if(producto.getCantidadDisponible() < reserva.getCantidadReserva()){
            throw new RuntimeException("Stock insuficiente");
        }
        reserva.setProducto(producto);
        producto.setCantidadDisponible(producto.getCantidadDisponible() - reserva.getCantidadReserva());
        productoRepository.save(producto);
        return reservaRepository.save(reserva);
    }

}
