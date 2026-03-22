package co.edu.udistrital.mdp.pets.services;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.pets.entities.MessageEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(MessageService.class)
public class MessageTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<MessageEntity> messageList = new ArrayList<>();
    private ShelterEntity shelterEntity;

    //Configuración inicial.
    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    //Limpia las tablas.
    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from MessageEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
    }

    //Inserta datos de prueba.
    private void insertData() {
        //Crea un refugio.
        shelterEntity = factory.manufacturePojo(ShelterEntity.class);
        entityManager.persist(shelterEntity);
        //Crea 3 mensajes asociados al refugio.
        for (int i = 0; i < 3; i++) {
            MessageEntity message = factory.manufacturePojo(MessageEntity.class);
            //Asignar refugio al mensaje.
            message.setShelter(shelterEntity);
            //Asignar fecha válida.
            message.setDate(LocalDate.now().plusDays(i));
            entityManager.persist(message);
            messageList.add(message);
        }
    }

    //Test para crear un mensaje correctamente.
    @Test
    void createMessage() throws EntityNotFoundException, IllegalOperationException {
        //Crear mensaje.
        MessageEntity newMessage = factory.manufacturePojo(MessageEntity.class);
        newMessage.setShelter(shelterEntity);
        newMessage.setDate(LocalDate.now().plusDays(10));
        MessageEntity result = messageService.createMessage(newMessage);
        //Verificar que se creó.
        assertNotNull(result);
        MessageEntity entity = entityManager.find(MessageEntity.class, result.getId());
        assertEquals(newMessage.getAuthor(), entity.getAuthor());
        assertEquals(newMessage.getMessageContent(), entity.getMessageContent());
        assertEquals(newMessage.getDate(), entity.getDate());
    }

    //Test para actualizar un mensaje.
    @Test
    void updateMessage() throws EntityNotFoundException, IllegalOperationException {
        //Obtiene mensaje existnte.
        MessageEntity entity = messageList.get(0);
        MessageEntity pojoEntity = factory.manufacturePojo(MessageEntity.class);
        pojoEntity.setShelter(shelterEntity);
        pojoEntity.setDate(LocalDate.now().plusDays(20));
        messageService.updateMessageEntity(entity.getId(), pojoEntity);
        //Busca mensaje actualizado.
        MessageEntity resp = entityManager.find(MessageEntity.class, entity.getId());
        assertEquals(pojoEntity.getMessageContent(), resp.getMessageContent());
    }

    //Test para actualizar mensaje inexistente.
    @Test
    void updateNonExistingMessage() {

        assertThrows(EntityNotFoundException.class, () -> {
            MessageEntity newMessage = factory.manufacturePojo(MessageEntity.class);
            newMessage.setShelter(shelterEntity);
            newMessage.setDate(LocalDate.now().plusDays(5));
            messageService.updateMessageEntity(999L, newMessage);
        });
    }

    //Test para eliminar un mensaje.
    @Test
    void deleteMessage() throws EntityNotFoundException, IllegalOperationException {
        //Obtiene mensaje existente.
        MessageEntity entity = messageList.get(1);
        messageService.deleteMessage(entity.getId());
        MessageEntity deleted = entityManager.find(MessageEntity.class, entity.getId());
        //Verificar que fue eliminado.
        assertNull(deleted);
    }

    //Test para eliminar mensaje inexistente.
    @Test
    void deleteNonExistingMessage() {
        assertThrows(EntityNotFoundException.class, () -> {
            messageService.deleteMessage(999L);
        });
    }
}