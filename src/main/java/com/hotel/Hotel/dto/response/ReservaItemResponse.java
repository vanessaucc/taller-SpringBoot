package com.hotel.Hotel.dto.response;

import com.hotel.Hotel.domain.EstadoReserva;

import java.time.LocalDate;
import java.util.UUID;

public record ReservaItemResponse(
        UUID idReserva,
        String numeroHabitacion,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        EstadoReserva estado,
        Double costoTotal) {
}