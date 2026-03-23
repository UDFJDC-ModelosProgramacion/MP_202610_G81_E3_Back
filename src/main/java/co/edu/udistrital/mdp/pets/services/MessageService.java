package co.edu.udistrital.mdp.pets.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.MessageEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageService {

    @Autowired
    //Repositorio de refugios, para persistencia.
    private MessageRepository messageRepository;

    //Metodo para evaluar la validez del evento.
    private void validateMessage(MessageEntity messageEntity)
    throws IllegalOperationException {
        //Valida si el autor es nula.
        if(messageEntity.getAuthor() == null)
            throw new IllegalOperationException("Author isn't valid.");

        //Valida si el contenido es nulo.
        if(messageEntity.getMessageContent() == null)
            throw new IllegalOperationException("Message content isn't valid.");

        //Valida si la fecha es nula.
        if(messageEntity.getDate() == null)
            throw new IllegalOperationException("Date isn't valid.");
    }

    //Metodo para crear un mensaje.
    @Transactional
    public MessageEntity createMessage(MessageEntity messageEntity)
    throws IllegalOperationException, EntityNotFoundException{
        log.info("Start message creation...");
        //Uso del metodo privado.
        validateMessage(messageEntity);

        //Si es correcto guarda el mensaje.
        return messageRepository.save(messageEntity);
    }

    //Metodo para editar un mensaje.
    @Transactional
    public MessageEntity updateMessageEntity (long messageId, MessageEntity message)
    throws IllegalOperationException, EntityNotFoundException{
        log.info("Starts update message with id: {}", messageId);

        //Busca el mensaje.
        Optional<MessageEntity> messageEntity = messageRepository.findById(messageId);

        //Verifica su existencia.
        if(messageEntity.isEmpty())
            throw new EntityNotFoundException("Message not found.");

        //Obtiene el mensaje a actualizar.
        MessageEntity messageToUpdate = messageEntity.get();

        //Uso del método.
        validateMessage(message);

        //Actualiza unicamente el contenido.
        messageToUpdate.setMessageContent(message.getMessageContent());

        log.info("End update message with the id: {}", messageId);
        return messageRepository.save(messageToUpdate);
    }

    //Metodo para borrar un mensaje existente.
    @Transactional
    public void deleteMessage(Long messageId) 
    throws EntityNotFoundException, IllegalOperationException{
        log.info("Start message delete with the id: {}", messageId);

        //Busca el mensaje.
        Optional<MessageEntity> messageEntity = messageRepository.findById(messageId);

        //Verifica que el mensaje exista.
        if(messageEntity.isEmpty())
            throw new EntityNotFoundException("Message not found.");

        messageRepository.deleteById(messageId);

        log.info("End delete message process with id: {}", messageId);
    }
}
