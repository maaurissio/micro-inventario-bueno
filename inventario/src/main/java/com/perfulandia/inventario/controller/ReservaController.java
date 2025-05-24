package com.perfulandia.inventario.controller;

import java.util.List;

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

    // @PostMapping
    // public ResponseEntity<Reserva> guardar(@RequestBody Reserva reserva){
    //     try {
    //         Reserva nuevaReserva = reservaService.guardar(reserva);
    //         return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);
    //     } catch (Exception e) {
    //         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    //     }
    // }
    @PostMapping
    public ResponseEntity<ReservaDTO> guardar(@RequestBody Reserva reserva) {
        try {
            ReservaDTO responseDTO = reservaService.guardar(reserva);
            return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
