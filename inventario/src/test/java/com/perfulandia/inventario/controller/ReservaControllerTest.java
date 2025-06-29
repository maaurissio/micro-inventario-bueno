package com.perfulandia.inventario.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.perfulandia.inventario.model.Reserva;
import com.perfulandia.inventario.model.ReservaDTO;
import com.perfulandia.inventario.service.ReservaService;

@WebMvcTest(ReservaController.class)
class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservaService reservaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetReservas_OK() throws Exception {
        List<Reserva> reservas = Arrays.asList(
            new Reserva(1L, 1, 5, null),
            new Reserva(2L, 2, 3, null)
        );
        when(reservaService.reservas()).thenReturn(reservas);

        mockMvc.perform(get("/api/reservas"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetReservas_NoContent() throws Exception {
        when(reservaService.reservas()).thenReturn(List.of());

        mockMvc.perform(get("/api/reservas"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGuardarReserva_Created() throws Exception {
        ReservaDTO responseDTO = new ReservaDTO();
        responseDTO.setCantidadReserva(5);
        responseDTO.setStockStatus("Bajo stock");

        when(reservaService.guardar(any(Reserva.class), anyList())).thenReturn(responseDTO);

        Map<String, Object> reservaRequest = new HashMap<>();
        reservaRequest.put("idCliente", 1);
        reservaRequest.put("cantidadReserva", 5);
        reservaRequest.put("idProductos", Arrays.asList(1, 2, 3));

        mockMvc.perform(post("/api/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservaRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void testGuardarReserva_BadRequest_SinIdCliente() throws Exception {
        Map<String, Object> reservaRequest = new HashMap<>();
        // Falta idCliente
        reservaRequest.put("cantidadReserva", 5);
        reservaRequest.put("idProductos", Arrays.asList(1, 2, 3));

        mockMvc.perform(post("/api/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservaRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGuardarReserva_BadRequest_SinCantidadReserva() throws Exception {
        Map<String, Object> reservaRequest = new HashMap<>();
        reservaRequest.put("idCliente", 1);
        // Falta cantidadReserva
        reservaRequest.put("idProductos", Arrays.asList(1, 2, 3));

        mockMvc.perform(post("/api/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservaRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGuardarReserva_BadRequest_SinIdProductos() throws Exception {
        Map<String, Object> reservaRequest = new HashMap<>();
        reservaRequest.put("idCliente", 1);
        reservaRequest.put("cantidadReserva", 5);
        // Falta idProductos

        mockMvc.perform(post("/api/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservaRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGuardarReserva_BadRequest_ProductoNoEncontrado() throws Exception {
        when(reservaService.guardar(any(Reserva.class), anyList()))
                .thenThrow(new RuntimeException("Producto con ID 999 no encontrado"));

        Map<String, Object> reservaRequest = new HashMap<>();
        reservaRequest.put("idCliente", 1);
        reservaRequest.put("cantidadReserva", 5);
        reservaRequest.put("idProductos", Arrays.asList(999));

        mockMvc.perform(post("/api/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservaRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGuardarReserva_BadRequest_StockInsuficiente() throws Exception {
        when(reservaService.guardar(any(Reserva.class), anyList()))
                .thenThrow(new RuntimeException("Stock insuficiente para el producto con ID 1"));

        Map<String, Object> reservaRequest = new HashMap<>();
        reservaRequest.put("idCliente", 1);
        reservaRequest.put("cantidadReserva", 100); // Cantidad muy alta
        reservaRequest.put("idProductos", Arrays.asList(1));

        mockMvc.perform(post("/api/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservaRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGuardarReserva_BadRequest_ListaProductosVacia() throws Exception {
        Map<String, Object> reservaRequest = new HashMap<>();
        reservaRequest.put("idCliente", 1);
        reservaRequest.put("cantidadReserva", 5);
        reservaRequest.put("idProductos", Arrays.asList()); // Lista vacía

        when(reservaService.guardar(any(Reserva.class), anyList()))
                .thenThrow(new RuntimeException("La lista de productos no puede estar vacía"));

        mockMvc.perform(post("/api/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservaRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGuardarReserva_Created_ConStockStatus_SinStock() throws Exception {
        ReservaDTO responseDTO = new ReservaDTO();
        responseDTO.setCantidadReserva(5);
        responseDTO.setStockStatus("Sin stock");

        when(reservaService.guardar(any(Reserva.class), anyList())).thenReturn(responseDTO);

        Map<String, Object> reservaRequest = new HashMap<>();
        reservaRequest.put("idCliente", 1);
        reservaRequest.put("cantidadReserva", 5);
        reservaRequest.put("idProductos", Arrays.asList(1));

        mockMvc.perform(post("/api/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservaRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void testGuardarReserva_Created_ConMultiplesProductos() throws Exception {
        ReservaDTO responseDTO = new ReservaDTO();
        responseDTO.setCantidadReserva(3);
        responseDTO.setStockStatus("Bajo stock");

        when(reservaService.guardar(any(Reserva.class), anyList())).thenReturn(responseDTO);

        Map<String, Object> reservaRequest = new HashMap<>();
        reservaRequest.put("idCliente", 2);
        reservaRequest.put("cantidadReserva", 3);
        reservaRequest.put("idProductos", Arrays.asList(1, 2, 3, 4));

        mockMvc.perform(post("/api/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reservaRequest)))
                .andExpect(status().isCreated());
    }
}