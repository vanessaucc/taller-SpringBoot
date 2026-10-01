package com.hotel.Hotel.service;

import com.hotel.Hotel.domain.HabitacionEstandar;
import com.hotel.Hotel.domain.SuitePresidencial;
import com.hotel.Hotel.dto.request.CrearHabitacionEstandarRequest;
import com.hotel.Hotel.dto.request.CrearSuitePresidencialRequest;
import com.hotel.Hotel.dto.response.HabitacionResponse;
import com.hotel.Hotel.mapper.HabitacionMapper;
import com.hotel.Hotel.repository.HabitacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class HabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final HabitacionMapper habitacionMapper;

    public HabitacionService(HabitacionRepository habitacionRepository, HabitacionMapper habitacionMapper) {
        this.habitacionRepository = habitacionRepository;
        this.habitacionMapper = habitacionMapper;
    }

    @Transactional
    public HabitacionResponse crearEstandar(CrearHabitacionEstandarRequest request) {
        HabitacionEstandar estandar = habitacionMapper.toEntity(request);
        return habitacionMapper.toResponse(habitacionRepository.save(estandar));
    }

    @Transactional
    public HabitacionResponse crearSuite(CrearSuitePresidencialRequest request) {
        SuitePresidencial suite = habitacionMapper.toEntity(request);
        return habitacionMapper.toResponse(habitacionRepository.save(suite));
    }

    @Transactional(readOnly = true)
    public List<HabitacionResponse> obtenerTodas() {
        return habitacionRepository.findAll().stream()
                .map(habitacionMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public HabitacionResponse obtenerPorId(UUID id) {
        return habitacionRepository.findById(id)
                .map(habitacionMapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Habitación no encontrada con ID: " + id));
    }
}