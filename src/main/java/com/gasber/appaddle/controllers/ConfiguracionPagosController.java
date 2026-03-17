package com.gasber.appaddle.controllers;

import org.springframework.web.bind.annotation.*;

import com.gasber.appaddle.models.ConfiguracionPagos;
import com.gasber.appaddle.services.ConfiguracionPagosService;

@RestController
@RequestMapping("/configuracion-pagos")

public class ConfiguracionPagosController {
    private final ConfiguracionPagosService configuracionPagosService;

    public ConfiguracionPagosController(ConfiguracionPagosService configuracionPagosService) {
        this.configuracionPagosService = configuracionPagosService;
    }

    // Obtener configuración actual
    @GetMapping
    public ConfiguracionPagos obtenerConfiguracion() {
        return configuracionPagosService.obtenerConfiguracion();
    }

    // Actualizar configuración
    @PutMapping
    public ConfiguracionPagos actualizarConfiguracion(@RequestBody ConfiguracionPagos config) {
        return configuracionPagosService.actualizarConfiguracion(config);
    }
    
}
