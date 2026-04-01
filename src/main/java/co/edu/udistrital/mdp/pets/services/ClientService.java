package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.ClientEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.repositories.ClientRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public ClientEntity createClient(ClientEntity client) {
        return clientRepository.save(client);
    }

    public ClientEntity getClient(Long id) throws EntityNotFoundException {
        Optional<ClientEntity> client = clientRepository.findById(id);

        if (client.isEmpty()) {
            throw new EntityNotFoundException("Client not found");
        }

        return client.get();
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
}