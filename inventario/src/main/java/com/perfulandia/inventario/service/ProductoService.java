package com.perfulandia.inventario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.inventario.model.Producto;
import com.perfulandia.inventario.repository.ProductoRepository;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> allProductos(){
        return productoRepository.findAll();
    }

    public List<Producto> productos(){
        return productoRepository.findByIsVigenteTrue();
    }

    public Producto guardar(Producto producto){
        return productoRepository.save(producto);
    }

    public Producto actualizarStock(int idProducto, int cantidadASumar) {
        Producto producto = productoRepository.findById(idProducto).orElseThrow(() -> new RuntimeException("Producto con ID " + idProducto + " no encontrado"));
        int nuevoStock = producto.getCantidadDisponible() + cantidadASumar;
        if (nuevoStock < 0) {
            throw new RuntimeException("La cantidad disponible no puede ser negativa");
        }
        producto.setCantidadDisponible(nuevoStock);
        return productoRepository.save(producto);
    }
}
