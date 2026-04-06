package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.ClientEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.repositories.ClientRepository;

@SpringBootTest
@Transactional
public class ClientServiceTest {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    private ClientEntity client1;
    private ClientEntity client2;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();

        client1 = new ClientEntity();
        client1.setClientName("Juan Perez");
        client1.setClientEmail("juan@test.com");
        client1.setClientPhone("3001234567");
        clientService.createClient(client1);

        client2 = new ClientEntity();
        client2.setClientName("Maria Lopez");
        client2.setClientEmail("maria@test.com");
        client2.setClientPhone("3109876543");
        clientService.createClient(client2);
    }

    @Test
    void testCreateClient() {
        ClientEntity client = new ClientEntity();
        client.setClientName("Pedro Ruiz");
        client.setClientEmail("pedro@test.com");
        client.setClientPhone("3201112233");

        ClientEntity saved = clientService.createClient(client);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("Pedro Ruiz", saved.getClientName());
    }

    @Test
    void testGetClient() throws EntityNotFoundException {
        ClientEntity found = clientService.getClient(client1.getId());

        assertEquals(client1.getId(), found.getId());
        assertEquals(client1.getClientName(), found.getClientName());
        assertEquals(client1.getClientEmail(), found.getClientEmail());
    }

    @Test
    void testGetClients() {
        List<ClientEntity> list = clientService.getClients();
        assertTrue(list.size() >= 2);
    }

    @Test
    void testUpdateClient() throws EntityNotFoundException {
        ClientEntity update = new ClientEntity();
        update.setClientName("Juan Actualizado");
        update.setClientEmail("juanupdate@test.com");
        update.setClientPhone("3110001111");

        ClientEntity updated = clientService.updateClient(client1.getId(), update);

        assertEquals("Juan Actualizado", updated.getClientName());
        assertEquals("3110001111", updated.getClientPhone());
    }

    @Test
    void testDeleteClient() throws EntityNotFoundException {
        clientService.deleteClient(client2.getId());

        assertThrows(EntityNotFoundException.class, () -> {
            clientService.getClient(client2.getId());
        });
    }
}