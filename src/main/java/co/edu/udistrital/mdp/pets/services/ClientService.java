package co.edu.udistrital.mdp.pets.services;

// ===== IMPORTS INICIO =====
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.ClientEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.repositories.ClientRepository;

import jakarta.transaction.Transactional;
// ===== IMPORTS FIN =====


// ===== DEFINICIÓN DE CLASE INICIO =====
@Service
@Transactional
public class ClientService {
// ===== DEFINICIÓN DE CLASE FIN =====


    // ===== DEPENDENCIAS INICIO =====
    @Autowired
    private ClientRepository clientRepository;
    // ===== DEPENDENCIAS FIN =====


    // ===== MÉTODOS CRUD INICIO =====

    public ClientEntity createClient(ClientEntity client) {
        return clientRepository.save(client);
    }

    public ClientEntity getClient(Long id) throws EntityNotFoundException {
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));
    }

    public List<ClientEntity> getClients() {
        return clientRepository.findAll();
    }

    public ClientEntity updateClient(Long id, ClientEntity newClient) throws EntityNotFoundException {
        ClientEntity client = getClient(id);

        client.setClientName(newClient.getClientName());
        client.setClientEmail(newClient.getClientEmail());
        client.setClientPhone(newClient.getClientPhone());

        return clientRepository.save(client);
    }

    public void deleteClient(Long id) throws EntityNotFoundException {
        ClientEntity client = getClient(id);
        clientRepository.delete(client);
    }

    // ===== MÉTODOS CRUD FIN =====

}