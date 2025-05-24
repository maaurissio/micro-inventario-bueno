package com.perfulandia.inventario.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.inventario.model.Producto;
import com.perfulandia.inventario.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @GetMapping
    public ResponseEntity<List<Producto>> getProductos(){
        List<Producto> productos = productoService.productos();
        if(productos.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Producto> guardar(@RequestBody Producto producto){
        Producto nuevoProducto = productoService.guardar(producto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Producto> actualizarStock(@PathVariable("id") int id, @RequestBody Map<String, Integer> stockRequest) {
        try {
            if (!stockRequest.containsKey("cantidad")) {
                throw new RuntimeException("El campo no puede estar vacio");
            }
            int cantidadASumar = stockRequest.get("cantidad");
            Producto productoActualizado = productoService.actualizarStock(id, cantidadASumar);
            return new ResponseEntity<>(productoActualizado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
