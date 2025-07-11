package com.gasber.appaddle.services;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gasber.appaddle.dtos.ClienteDTO;
import com.gasber.appaddle.mappers.ClienteMapper;
import com.gasber.appaddle.models.Cliente;
import com.gasber.appaddle.repositories.ClienteRepository;

import java.util.List;
//import java.util.stream.Collectors;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    public ClienteDTO crearCliente(ClienteDTO dto) {
        validar(dto);
        Cliente cliente = clienteMapper.toEntity(dto);
        Cliente guardado = clienteRepository.save(cliente);
        return clienteMapper.toDTO(guardado);
    }

    public List<ClienteDTO> obtenerTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(clienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ClienteDTO obtenerPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        return clienteMapper.toDTO(cliente);
    }

    public ClienteDTO actualizarCliente(Long id, ClienteDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        validar(dto);
        clienteMapper.updateEntityFromDTO(dto, cliente);
        Cliente actualizado = clienteRepository.save(cliente);
        return clienteMapper.toDTO(actualizado);
    }

    public void eliminarCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado");
        }
        clienteRepository.deleteById(id);
    }

    private void validar(ClienteDTO dto) {
        if (dto.getTelefono() == null || dto.getTelefono().isBlank()) {
            throw new RuntimeException("El teléfono no puede ser nulo ni vacío");
        }
    }
    
}
