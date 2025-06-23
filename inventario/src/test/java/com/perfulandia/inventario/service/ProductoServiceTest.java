package com.perfulandia.inventario.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.perfulandia.inventario.model.Producto;
import com.perfulandia.inventario.repository.ProductoRepository;

public class ProductoServiceTest {
    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAllProductos() {
        Producto p1 = new Producto(1, "Perfume A", 10, "Floral", "Descripción A", "Marca A", 50.0, 29.99, true, null);
        Producto p2 = new Producto(2, "Perfume B", 5, "Cítrico", "Descripción B", "Marca B", 30.0, 19.99, false, null);
        List<Producto> productos = Arrays.asList(p1, p2);

        when(productoRepository.findAll()).thenReturn(productos);

        List<Producto> resultado = productoService.allProductos();

        assertThat(resultado).hasSize(2).contains(p1, p2);
        verify(productoRepository).findAll();
    }

    @Test
    void testProductos() {
        Producto p1 = new Producto(1, "Perfume A", 10, "Floral", "Descripción A", "Marca A", 50.0, 29.99, true, new ArrayList<>());
        Producto p2 = new Producto(2, "Perfume B", 5, "Cítrico", "Descripción B", "Marca B", 30.0, 19.99, true, new ArrayList<>());
        Producto p3 = new Producto(3, "Perfume C", 0, "Madera", "Descripción C", "Marca C", 40.0, 24.99, false, new ArrayList<>());

        // El mock devuelve solo los productos con isVigente = true
        when(productoRepository.findByIsVigenteTrue()).thenReturn(Arrays.asList(p1, p2));

        List<Producto> resultado = productoService.productos();

        assertThat(resultado).hasSize(2).contains(p1, p2).doesNotContain(p3);
        verify(productoRepository).findByIsVigenteTrue();
    }

    @Test
    void testGuardarProductos(){
        Producto producto = new Producto(1, "perfume riko", 30, "papas fritas", "huele a papas fritas", "calvin klein", 15, 30000, true, null);
        //Producto producto2 = new Producto(2, "perfume muyriko", 30, "pan con palta", "huele a pan con palta", "toyota", 15, 30001, false, null);
        Producto productoGuardado = new Producto(1, "perfume riko", 30, "papas fritas", "huele a papas fritas", "calvin klein", 15, 30000, true, null);
        when(productoRepository.save(producto)).thenReturn(productoGuardado);

        Producto resultado = productoService.guardar(producto);
        assertThat(resultado.getIdProducto()).isEqualTo(1);
        verify(productoRepository).save(producto);
    }
    
    @Test
    void testActualizarStock() {
        Producto productoExistente = new Producto(1, "Perfume A", 10, "Floral", "Descripción A", "Marca A", 50.0, 29.99, true, new ArrayList<>());
        Producto productoActualizado = new Producto(1, "Perfume A", 15, "Floral", "Descripción A", "Marca A", 50.0, 29.99, true, new ArrayList<>());

        when(productoRepository.findById(1)).thenReturn(Optional.of(productoExistente));
        when(productoRepository.save(productoExistente)).thenReturn(productoActualizado);

        Producto resultado = productoService.actualizarStock(1, 5);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getCantidadDisponible()).isEqualTo(15);
        verify(productoRepository).findById(1);
        verify(productoRepository).save(productoExistente);
    }



}
