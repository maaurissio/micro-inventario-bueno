package com.perfulandia.inventario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.perfulandia.inventario.model.Reserva;
import com.perfulandia.inventario.repository.ReservaRepository;

@Service
public class ReservaService {
    @Autowired
    private ReservaRepository reservaRepository;

    public List<Reserva> reservas(){
        return reservaRepository.findAll();
    }
    
    public Reserva guardar(Reserva reserva){
        return reservaRepository.save(reserva);
    }

}
