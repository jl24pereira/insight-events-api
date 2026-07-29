package com.jlpereira.api.historial.web;

import java.util.List;
import java.util.UUID;

import com.jlpereira.api.historial.dto.HistorialResponse;
import com.jlpereira.api.historial.service.HistorialService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author Jose Luis Pereira
 */
@RestController
@RequestMapping("/api/v1/eventos/{eventoId}/historial")
@RequiredArgsConstructor
public class HistorialController {

    private final HistorialService service;

    @GetMapping
    public List<HistorialResponse> consultar(@PathVariable UUID eventoId) {
        return service.consultarPorEvento(eventoId);
    }

}
