package com.perfulandia.inventario.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.inventario.model.Producto;
import com.perfulandia.inventario.service.ProductoService;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetProductos_OK() throws Exception {
        List<Producto> productos = Arrays.asList(
            new Producto(1, "Producto 1", 10, "Aroma 1", "Descripción 1", "Marca 1", 100.0, 25.99, true, null),
            new Producto(2, "Producto 2", 5, "Aroma 2", "Descripción 2", "Marca 2", 200.0, 35.99, true, null)
        );
        when(productoService.productos()).thenReturn(productos);

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProductos_NoContent() throws Exception {
        when(productoService.productos()).thenReturn(List.of());

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetAllProductos_OK() throws Exception {
        List<Producto> productos = Arrays.asList(
            new Producto(1, "Producto 1", 10, "Aroma 1", "Descripción 1", "Marca 1", 100.0, 25.99, true, null),
            new Producto(2, "Producto 2", 0, "Aroma 2", "Descripción 2", "Marca 2", 200.0, 35.99, false, null)
        );
        when(productoService.allProductos()).thenReturn(productos);

        mockMvc.perform(get("/api/productos/todos"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllProductos_NoContent() throws Exception {
        when(productoService.allProductos()).thenReturn(List.of());

        mockMvc.perform(get("/api/productos/todos"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGuardarProducto_Created() throws Exception {
        Producto producto = new Producto(0, "Nuevo Producto", 15, "Nuevo Aroma", "Nueva Descripción", "Nueva Marca", 150.0, 29.99, true, null);
        Producto productoGuardado = new Producto(3, "Nuevo Producto", 15, "Nuevo Aroma", "Nueva Descripción", "Nueva Marca", 150.0, 29.99, true, null);
        
        when(productoService.guardar(any(Producto.class))).thenReturn(productoGuardado);

        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(producto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testActualizarStock_OK() throws Exception {
        Producto productoActualizado = new Producto(1, "Producto 1", 20, "Aroma 1", "Descripción 1", "Marca 1", 100.0, 25.99, true, null);
        when(productoService.actualizarStock(eq(1), eq(10))).thenReturn(productoActualizado);

        Map<String, Integer> stockRequest = new HashMap<>();
        stockRequest.put("cantidad", 10);

        mockMvc.perform(put("/api/productos/1/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stockRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void testActualizarStock_BadRequest_SinCantidad() throws Exception {
        Map<String, Integer> stockRequest = new HashMap<>();
        // No incluimos el campo "cantidad"

        mockMvc.perform(put("/api/productos/1/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stockRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizarStock_BadRequest_ProductoNoEncontrado() throws Exception {
        when(productoService.actualizarStock(eq(999), eq(10)))
                .thenThrow(new RuntimeException("Producto con ID 999 no encontrado"));

        Map<String, Integer> stockRequest = new HashMap<>();
        stockRequest.put("cantidad", 10);

        mockMvc.perform(put("/api/productos/999/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stockRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizarStock_BadRequest_StockNegativo() throws Exception {
        when(productoService.actualizarStock(eq(1), eq(-20)))
                .thenThrow(new RuntimeException("La cantidad disponible no puede ser negativa"));

        Map<String, Integer> stockRequest = new HashMap<>();
        stockRequest.put("cantidad", -20);

        mockMvc.perform(put("/api/productos/1/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stockRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizarVigencia_OK() throws Exception {
        Producto producto = new Producto(1, "Producto 1", 10, "Aroma 1", "Descripción 1", "Marca 1", 100.0, 25.99, true, null);
        Producto productoActualizado = new Producto(1, "Producto 1", 10, "Aroma 1", "Descripción 1", "Marca 1", 100.0, 25.99, false, null);
        
        when(productoService.actualizarStock(eq(1), eq(0))).thenReturn(producto);
        when(productoService.guardar(any(Producto.class))).thenReturn(productoActualizado);

        Map<String, Boolean> vigenteRequest = new HashMap<>();
        vigenteRequest.put("isVigente", false);

        mockMvc.perform(put("/api/productos/1/vigente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vigenteRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void testActualizarVigencia_BadRequest_SinCampo() throws Exception {
        Map<String, Boolean> vigenteRequest = new HashMap<>();
        // No incluimos el campo "isVigente"

        mockMvc.perform(put("/api/productos/1/vigente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vigenteRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizarVigencia_BadRequest_CampoNull() throws Exception {
        Map<String, Boolean> vigenteRequest = new HashMap<>();
        vigenteRequest.put("isVigente", null);

        mockMvc.perform(put("/api/productos/1/vigente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vigenteRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizarVigencia_BadRequest_ProductoNoEncontrado() throws Exception {
        when(productoService.actualizarStock(eq(999), eq(0)))
                .thenThrow(new RuntimeException("Producto con ID 999 no encontrado"));

        Map<String, Boolean> vigenteRequest = new HashMap<>();
        vigenteRequest.put("isVigente", false);

        mockMvc.perform(put("/api/productos/999/vigente")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vigenteRequest)))
                .andExpect(status().isBadRequest());
    }
}