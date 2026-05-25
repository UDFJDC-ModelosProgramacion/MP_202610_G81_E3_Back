package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.dto.MessageDTO;
import co.edu.udistrital.mdp.pets.entities.MessageEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.MessageService;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<MessageDTO> findAll() {
        List<MessageEntity> list = messageService.getMessages();
        return modelMapper.map(list, new TypeToken<List<MessageDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public MessageDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        MessageEntity entity = messageService.getMessage(id);
        return modelMapper.map(entity, MessageDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public MessageDTO create(@RequestBody MessageDTO dto)
            throws IllegalOperationException, EntityNotFoundException {

        MessageEntity entity = messageService.createMessage(
                modelMapper.map(dto, MessageEntity.class));

        return modelMapper.map(entity, MessageDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public MessageDTO update(@PathVariable Long id, @RequestBody MessageDTO dto)
            throws EntityNotFoundException, IllegalOperationException {

        MessageEntity entity = messageService.updateMessageEntity(
                id, modelMapper.map(dto, MessageEntity.class));

        return modelMapper.map(entity, MessageDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id)
            throws EntityNotFoundException, IllegalOperationException {

        messageService.deleteMessage(id);
    }
}