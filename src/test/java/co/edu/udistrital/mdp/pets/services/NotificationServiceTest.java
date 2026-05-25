package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.ClientEntity;
import co.edu.udistrital.mdp.pets.entities.NotificationEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;

@SpringBootTest
@Transactional
public class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ClientService clientService;

    @Test
    void testCreateNotification() {

        ClientEntity client = new ClientEntity();
        client.setClientName("Test Client");
        client.setClientEmail("test@test.com");
        client.setClientPhone("123");
        client = clientService.createClient(client);

        NotificationEntity notification = new NotificationEntity();
        notification.setMessage("Hello");
        notification.setDate(LocalDate.now());
        notification.setClient(client);

        NotificationEntity saved = notificationService.createNotification(notification);

        assertNotNull(saved);
        assertNotNull(saved.getId());
        assertEquals("Hello", saved.getMessage());
    }

    @Test
    void testGetNotification() throws EntityNotFoundException {

        ClientEntity client = new ClientEntity();
        client.setClientName("Client A");
        client.setClientEmail("a@test.com");
        client.setClientPhone("111");
        client = clientService.createClient(client);

        NotificationEntity notification = new NotificationEntity();
        notification.setMessage("Hi");
        notification.setDate(LocalDate.now());
        notification.setClient(client);

        NotificationEntity saved = notificationService.createNotification(notification);

        NotificationEntity found = notificationService.getNotification(saved.getId());

        assertEquals(saved.getId(), found.getId());
        assertEquals("Hi", found.getMessage());
    }

    @Test
    void testGetNotifications() {

        ClientEntity client = new ClientEntity();
        client.setClientName("Client B");
        client.setClientEmail("b@test.com");
        client.setClientPhone("222");
        client = clientService.createClient(client);

        NotificationEntity n1 = new NotificationEntity();
        n1.setMessage("Msg 1");
        n1.setDate(LocalDate.now());
        n1.setClient(client);
        notificationService.createNotification(n1);

        NotificationEntity n2 = new NotificationEntity();
        n2.setMessage("Msg 2");
        n2.setDate(LocalDate.now());
        n2.setClient(client);
        notificationService.createNotification(n2);

        List<NotificationEntity> list = notificationService.getNotifications();

        assertTrue(list.size() >= 2);
    }

    @Test
    void testUpdateNotification() throws EntityNotFoundException {

        ClientEntity client = new ClientEntity();
        client.setClientName("Client Update");
        client.setClientEmail("update@test.com");
        client.setClientPhone("333");
        client = clientService.createClient(client);

        NotificationEntity notification = new NotificationEntity();
        notification.setMessage("Old message");
        notification.setDate(LocalDate.now());
        notification.setClient(client);

        NotificationEntity saved = notificationService.createNotification(notification);

        NotificationEntity update = new NotificationEntity();
        update.setMessage("New message");
        update.setDate(LocalDate.now());
        update.setClient(client);

        NotificationEntity updated = notificationService.updateNotification(saved.getId(), update);

        assertEquals("New message", updated.getMessage());
    }

    @Test
    void testDeleteNotification() throws EntityNotFoundException {

        ClientEntity client = new ClientEntity();
        client.setClientName("Client Delete");
        client.setClientEmail("delete@test.com");
        client.setClientPhone("999");
        client = clientService.createClient(client);

        NotificationEntity notification = new NotificationEntity();
        notification.setMessage("To delete");
        notification.setDate(LocalDate.now());
        notification.setClient(client);

        NotificationEntity saved = notificationService.createNotification(notification);

        notificationService.deleteNotification(saved.getId());

        assertThrows(EntityNotFoundException.class, () -> {
            notificationService.getNotification(saved.getId());
        });
    }
}