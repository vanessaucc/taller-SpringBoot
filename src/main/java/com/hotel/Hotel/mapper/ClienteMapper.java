package com.hotel.Hotel.mapper;

import com.hotel.Hotel.domain.Cliente;
import com.hotel.Hotel.domain.Reserva;
import com.hotel.Hotel.dto.request.ActualizarClienteRequest;
import com.hotel.Hotel.dto.request.CrearClienteRequest;
import com.hotel.Hotel.dto.response.ClienteResponse;
import com.hotel.Hotel.dto.response.ClienteResumenResponse;
import com.hotel.Hotel.dto.response.ReservaItemResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClienteMapper {

    ClienteResponse toResponse(Cliente cliente);

    List<ClienteResponse> toResponseList(List<Cliente> clientes);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "penalizaciones", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    Cliente toEntity(CrearClienteRequest request);

    @Mapping(target = "totalReservasRealizadas", expression = "java(cliente.getReservas() != null ? cliente.getReservas().size() : 0)")
    @Mapping(target = "montoTotalGastado", expression = "java(calcularMontoTotal(cliente.getReservas()))")
    @Mapping(target = "reservasRecientes", source = "reservas", qualifiedByName = "mapReservasList")
    ClienteResumenResponse toResumenResponse(Cliente cliente);

    @Named("mapReservasList")
    default List<ReservaItemResponse> mapReservasList(List<Reserva> reservas) {
        if (reservas == null)
            return List.of();
        return reservas.stream()
                .map(r -> new ReservaItemResponse(
                        r.getId(),
                        r.getHabitacion() != null ? r.getHabitacion().getNumero() : null,
                        r.getPeriodo() != null ? r.getPeriodo().fechaInicio().toLocalDate() : null,
                        r.getPeriodo() != null ? r.getPeriodo().fechaFin().toLocalDate() : null,
                        r.getEstado(),
                        r.getCostoTotal()))
                .toList();
    }

    default double calcularMontoTotal(List<Reserva> reservas) {
        if (reservas == null || reservas.isEmpty())
            return 0.0;
        return reservas.stream()
                .mapToDouble(Reserva::getCostoTotal)
                .sum();
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "activo", ignore = true)
    @Mapping(target = "penalizaciones", ignore = true)
    @Mapping(target = "reservas", ignore = true)
    void updateClienteFromDto(ActualizarClienteRequest dto, @MappingTarget Cliente entity);
}