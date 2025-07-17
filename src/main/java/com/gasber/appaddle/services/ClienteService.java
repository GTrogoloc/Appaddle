package com.gasber.appaddle.services;

import com.gasber.appaddle.dtos.ClienteDTO;
import com.gasber.appaddle.mappers.ClienteMapper;
import com.gasber.appaddle.models.Cliente;
import com.gasber.appaddle.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

import com.gasber.appaddle.exceptions.ValidationException;

@Service
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteMapper clienteMapper;

    public ClienteDTO crearCliente(ClienteDTO dto) {
        validarCliente(dto);
        Cliente cliente = clienteMapper.toEntity(dto);
        Cliente guardado = clienteRepository.save(cliente);
        return clienteMapper.toDTO(guardado);
    }

    public ClienteDTO actualizarCliente(Long id, ClienteDTO dto) {
        validarCliente(dto);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setTelefono(dto.getTelefono());
        return clienteMapper.toDTO(clienteRepository.save(cliente));
    }

    public ClienteDTO obtenerClientePorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        return clienteMapper.toDTO(cliente);
    }

    public List<ClienteDTO> listarClientes() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toDTO)
                .toList();
    }

    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }


    // ========================
    // Validaciones personalizadas
    // ========================
    private void validarCliente(ClienteDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new ValidationException("nombre", "El nombre no puede estar vacío.");
        }
        if (!esTexto(dto.getNombre())) {
            throw new ValidationException("nombre", "El nombre solo puede contener letras.");
        }

        if (dto.getApellido() == null || dto.getApellido().trim().isEmpty()) {
            throw new ValidationException("apellido", "El apellido no puede estar vacío.");
        }
        if (!esTexto(dto.getApellido())) {
            throw new ValidationException("apellido", "El apellido solo puede contener letras.");
        }

        if (dto.getTelefono() == null || dto.getTelefono().trim().isEmpty()) {
            throw new ValidationException("telefono", "El teléfono no puede estar vacío.");
        }
     
        if (!esNumerico(dto.getTelefono())) {   
            throw new ValidationException("telefono", "El teléfono solo puede contener números.");
        }
    }
    
    private boolean esTexto(String input) {
        return Pattern.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", input);
    }

    private boolean esNumerico(String input) {
        return Pattern.matches("^\\d+$", input);
    }
    
}
