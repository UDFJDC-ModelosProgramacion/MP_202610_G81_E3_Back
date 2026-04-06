package co.edu.udistrital.mdp.pets.controllers;

// ===== IMPORTS INICIO =====
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import co.edu.udistrital.mdp.pets.services.ClientService;
import co.edu.udistrital.mdp.pets.dto.ClientDTO;
import co.edu.udistrital.mdp.pets.entities.ClientEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
// ===== IMPORTS FIN =====


// ===== DEFINICIÓN DE CLASE INICIO =====
@RestController
@RequestMapping("/clients")
public class ClientController {

    // ===== ATRIBUTOS INICIO =====
    @Autowired
    private ClientService clientService;

    @Autowired
    private ModelMapper modelMapper;
    // ===== ATRIBUTOS FIN =====


    // ===== MÉTODOS INICIO =====

    // GET /clients → listar todos los clientes
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ClientDTO> findAll() {
        List<ClientEntity> clients = clientService.getClients();
        return modelMapper.map(clients, new TypeToken<List<ClientDTO>>() {}.getType());
    }

    // GET /clients/{id} → traer cliente por ID
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClientDTO findById(@PathVariable Long id) throws EntityNotFoundException {
        ClientEntity client = clientService.getClient(id);
        return modelMapper.map(client, ClientDTO.class);
    }

    // POST /clients → crear cliente
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDTO create(@RequestBody ClientDTO clientDTO) {
        ClientEntity clientEntity = modelMapper.map(clientDTO, ClientEntity.class);
        ClientEntity savedClient = clientService.createClient(clientEntity);
        return modelMapper.map(savedClient, ClientDTO.class);
    }

    // PUT /clients/{id} → actualizar cliente
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClientDTO update(@PathVariable Long id, @RequestBody ClientDTO clientDTO) throws EntityNotFoundException {
        ClientEntity clientEntity = modelMapper.map(clientDTO, ClientEntity.class);
        ClientEntity updatedClient = clientService.updateClient(id, clientEntity);
        return modelMapper.map(updatedClient, ClientDTO.class);
    }

    // DELETE /clients/{id} → eliminar cliente
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        clientService.deleteClient(id);
    }

    // ===== MÉTODOS FIN =====

}
// ===== DEFINICIÓN DE CLASE FIN =====