package com.perfulandia.inventario.service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.perfulandia.inventario.model.Producto;
import com.perfulandia.inventario.model.Reserva;
import com.perfulandia.inventario.model.ReservaDTO;
import com.perfulandia.inventario.repository.ProductoRepository;
import com.perfulandia.inventario.repository.ReservaRepository;

public class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ReservaService reservaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReservas() {
        Reserva r1 = new Reserva(1L, 1, 5, new ArrayList<>());
        Reserva r2 = new Reserva(2L, 2, 3, new ArrayList<>());
        List<Reserva> reservas = Arrays.asList(r1, r2);

        when(reservaRepository.findAll()).thenReturn(reservas);

        List<Reserva> resultado = reservaService.reservas();

        assertThat(resultado).hasSize(2).contains(r1, r2);
}

    @Test
    void testGuardar() {
        Reserva reserva = new Reserva(null, 1, 2, new ArrayList<>());
        List<Integer> idProductos = Arrays.asList(1);
        Producto producto = new Producto(1, "Perfume A", 10, "Floral", "Descripción A", "Marca A", 50.0, 29.99, true, new ArrayList<>());
        Producto productoActualizado = new Producto(1, "Perfume A", 8, "Floral", "Descripción A", "Marca A", 50.0, 29.99, true, new ArrayList<>());

        when(productoRepository.findById(1)).thenReturn(java.util.Optional.of(producto));
        when(reservaRepository.save(reserva)).thenReturn(reserva);
        when(productoRepository.save(producto)).thenReturn(productoActualizado);

        ReservaDTO resultado = reservaService.guardar(reserva, idProductos);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getCantidadReserva()).isEqualTo(2);
        assertThat(resultado.getStockStatus()).isEqualTo("Bajo stock");
}


}
