package com.gasber.appaddle.services;

import org.springframework.stereotype.Service;
import com.gasber.appaddle.models.ConfiguracionPagos;
import com.gasber.appaddle.repositories.ConfiguracionPagosRepository;

import jakarta.annotation.PostConstruct;

@Service
public class ConfiguracionPagosService {

    private final ConfiguracionPagosRepository configuracionPagosRepository;

    public ConfiguracionPagosService(ConfiguracionPagosRepository configuracionPagosRepository) {
        this.configuracionPagosRepository = configuracionPagosRepository;
    }

    // Obtener configuración actual
    public ConfiguracionPagos obtenerConfiguracion() {
        return configuracionPagosRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay configuración de pagos cargada"));
    }

    // Actualizar configuración
    public ConfiguracionPagos actualizarConfiguracion(ConfiguracionPagos config) {
        return configuracionPagosRepository.save(config);
    }

    @PostConstruct
public void inicializarConfiguracion() {

    if (configuracionPagosRepository.count() == 0) {

        ConfiguracionPagos config = new ConfiguracionPagos();
        config.setPrecioTurno(28000.0);
        config.setSeniaTurno(10000.0);
        config.setHorasCancelacion(8);

        configuracionPagosRepository.save(config);
    }
    
}
}