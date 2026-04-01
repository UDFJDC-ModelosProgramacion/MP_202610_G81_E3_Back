package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.ClientEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;

@SpringBootTest
@Transactional
public class ClientServiceTest {

    @Autowired
    private ClientService clientService;

    @Test
    void testCreateClient() {

        ClientEntity client = new ClientEntity();
        client.setClientName("Juan Perez");
        client.setClientEmail("juan@test.com");
        client.setClientPhone("3001234567");

        ClientEntity saved = clientService.createClient(client);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("Juan Perez", saved.getClientName());
    }

    @Test
    void testGetClient() throws EntityNotFoundException {

        ClientEntity client = new ClientEntity();
        client.setClientName("Maria Lopez");
        client.setClientEmail("maria@test.com");
        client.setClientPhone("3109876543");

        ClientEntity saved = clientService.createClient(client);

        ClientEntity found = clientService.getClient(saved.getId());

        assertEquals(saved.getId(), found.getId());
        assertEquals("Maria Lopez", found.getClientName());
    }

    @Test
    void testGetClients() {

        ClientEntity c1 = new ClientEntity();
        c1.setClientName("A");
        c1.setClientEmail("a@test.com");
        c1.setClientPhone("111");
        clientService.createClient(c1);

        ClientEntity c2 = new ClientEntity();
        c2.setClientName("B");
        c2.setClientEmail("b@test.com");
        c2.setClientPhone("222");
        clientService.createClient(c2);

        List<ClientEntity> list = clientService.getClients();

        assertTrue(list.size() >= 2);
    }

    @Test
    void testUpdateClient() throws EntityNotFoundException {

        ClientEntity client = new ClientEntity();
        client.setClientName("Old Name");
        client.setClientEmail("old@test.com");
        client.setClientPhone("000");

        ClientEntity saved = clientService.createClient(client);

        ClientEntity update = new ClientEntity();
        update.setClientName("New Name");
        update.setClientEmail("new@test.com");
        update.setClientPhone("999");

        ClientEntity updated = clientService.updateClient(saved.getId(), update);

        assertEquals("New Name", updated.getClientName());
        assertEquals("999", updated.getClientPhone());
    }

    @Test
    void testDeleteClient() throws EntityNotFoundException {

        ClientEntity client = new ClientEntity();
        client.setClientName("Delete Me");
        client.setClientEmail("delete@test.com");
        client.setClientPhone("123");

        ClientEntity saved = clientService.createClient(client);

        clientService.deleteClient(saved.getId());

        assertThrows(EntityNotFoundException.class, () -> {
            clientService.getClient(saved.getId());
        });
    }
}