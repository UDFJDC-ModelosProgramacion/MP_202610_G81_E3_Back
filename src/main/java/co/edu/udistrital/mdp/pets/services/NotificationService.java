package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.NotificationEntity;
import co.edu.udistrital.mdp.pets.repositories.NotificationRepository;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // CREAR NOTIFICACIÓN
    public NotificationEntity createNotification(NotificationEntity notification) {
        return notificationRepository.save(notification);
    }

    // OBTENER POR ID
    public NotificationEntity getNotification(Long id) throws EntityNotFoundException {

        Optional<NotificationEntity> notification = notificationRepository.findById(id);

        if (notification.isEmpty()) {
            throw new EntityNotFoundException("Notification not found");
        }

        return notification.get();
    }

    // LISTAR TODAS
    public List<NotificationEntity> getNotifications() {
        return notificationRepository.findAll();
    }

    // ACTUALIZAR
    public NotificationEntity updateNotification(Long id, NotificationEntity newNotification)
            throws EntityNotFoundException {

        NotificationEntity notification = getNotification(id);

        notification.setMessage(newNotification.getMessage());
        notification.setDate(newNotification.getDate());
        notification.setClient(newNotification.getClient());

        return notificationRepository.save(notification);
    }

    // ELIMINAR
    public void deleteNotification(Long id) throws EntityNotFoundException {

        NotificationEntity notification = getNotification(id);
        notificationRepository.delete(notification);
    }
}