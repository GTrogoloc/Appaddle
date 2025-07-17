package com.gasber.appaddle.mappers;

import com.gasber.appaddle.dtos.CanchaDTO;
import com.gasber.appaddle.models.Cancha;

public class CanchaMapper {
     public static CanchaDTO toDTO(Cancha cancha) {
        CanchaDTO dto = new CanchaDTO();
        dto.setId(cancha.getId());
        dto.setNombre(cancha.getNombre());
        dto.setActiva(cancha.isActiva());
        return dto;
    }

    public static Cancha toEntity(CanchaDTO dto) {
        Cancha cancha = new Cancha();
        cancha.setId(dto.getId()); // opcional, se ignora si es null
        cancha.setNombre(dto.getNombre());
        // No seteamos "activa", lo dejamos como true por defecto
        return cancha;
    }

}
