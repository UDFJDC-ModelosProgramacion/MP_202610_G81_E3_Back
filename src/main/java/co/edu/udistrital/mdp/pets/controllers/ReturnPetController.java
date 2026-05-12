package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.dto.ReturnPetDTO;
import co.edu.udistrital.mdp.pets.entities.ReturnPetEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.ReturnPetService;

@RestController
@RequestMapping("/returnpets")
public class ReturnPetController {

    @Autowired
    private ReturnPetService returnPetService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReturnPetDTO create(@RequestBody ReturnPetDTO returnPetDTO) throws IllegalOperationException {
        ReturnPetEntity entity = modelMapper.map(returnPetDTO, ReturnPetEntity.class);
        return modelMapper.map(returnPetService.createReturn(entity), ReturnPetDTO.class);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ReturnPetDTO> findAll() {
        List<ReturnPetEntity> returns = returnPetService.getReturns();
        return modelMapper.map(returns, new TypeToken<List<ReturnPetDTO>>() {}.getType());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReturnPetDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        ReturnPetEntity entity = returnPetService.getReturn(id);
        return modelMapper.map(entity, ReturnPetDTO.class);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReturnPetDTO update(@PathVariable Long id, @RequestBody ReturnPetDTO returnPetDTO)
            throws EntityNotFoundException, IllegalOperationException {
        ReturnPetEntity entity = modelMapper.map(returnPetDTO, ReturnPetEntity.class);
        return modelMapper.map(returnPetService.updateReturn(id, entity), ReturnPetDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException {
        returnPetService.deleteReturn(id);
    }
}