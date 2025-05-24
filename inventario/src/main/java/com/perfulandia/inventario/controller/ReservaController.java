package com.perfulandia.inventario.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.perfulandia.inventario.model.Reserva;
import com.perfulandia.inventario.model.ReservaDTO;
import com.perfulandia.inventario.service.ReservaService;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {
    @Autowired
    private ReservaService reservaService;

    @GetMapping
    public ResponseEntity<List<Reserva>> getReservas(){
        List<Reserva> reservas = reservaService.reservas();
        if(reservas.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reservas, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ReservaDTO> guardar(@RequestBody Map<String, Object> reservaRequest) {
        try {
            // Extraer los campos del mapa
            if (!reservaRequest.containsKey("idCliente") || !reservaRequest.containsKey("cantidadReserva") || !reservaRequest.containsKey("idProductos")) {
                throw new RuntimeException("Los campos no pueden estar vacios");
            }

            int idCliente = (int) reservaRequest.get("idCliente");
            int cantidadReserva = (int) reservaRequest.get("cantidadReserva");
            @SuppressWarnings("unchecked")
            List<Integer> idProductos = (List<Integer>) reservaRequest.get("idProductos");

            // Crear la reserva
            Reserva reserva = new Reserva();
            reserva.setIdCliente(idCliente);
            reserva.setCantidadReserva(cantidadReserva);

            // Llamar al servicio
            ReservaDTO responseDTO = reservaService.guardar(reserva, idProductos);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
