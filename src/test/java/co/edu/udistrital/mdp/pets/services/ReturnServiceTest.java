package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;


import co.edu.udistrital.mdp.pets.entities.ReturnPetEntity; 
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.ReturnPetRepository;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Import(ReturnPetService.class) 
class ReturnPetServiceTest { 

    @Autowired
    private ReturnPetService returnPetService; 

    @Autowired
    private ReturnPetRepository returnPetRepository; 

    private PodamFactory factory = new PodamFactoryImpl();
    private ReturnPetEntity returnPetEntity; 

    @BeforeEach
    void setUp() throws IllegalOperationException {
        returnPetRepository.deleteAll();

      
        returnPetEntity = factory.manufacturePojo(ReturnPetEntity.class);
        returnPetEntity.setId(null);
        returnPetEntity.setReason("Incompatibilidad con otros animales");
        returnPetEntity.setReturnDate(LocalDate.now().minusDays(1));
        returnPetEntity = returnPetService.createReturn(returnPetEntity);
    }

   

    @Test
    void testCreateReturnValid() throws IllegalOperationException {
        ReturnPetEntity newReturn = new ReturnPetEntity();
        newReturn.setReason("Problemas de salud del adoptante");
        newReturn.setReturnDate(LocalDate.now());

        ReturnPetEntity result = returnPetService.createReturn(newReturn);

        assertNotNull(result.getId());
        assertEquals("Problemas de salud del adoptante", result.getReason());
    }

    @Test
    void testCreateReturnReasonNull() {
        ReturnPetEntity newReturn = new ReturnPetEntity();
        newReturn.setReason(null);
        newReturn.setReturnDate(LocalDate.now());

        assertThrows(IllegalOperationException.class, () -> returnPetService.createReturn(newReturn));
    }

    @Test
    void testCreateReturnDateInFuture() {
        ReturnPetEntity newReturn = new ReturnPetEntity();
        newReturn.setReason("Razón válida");
        newReturn.setReturnDate(LocalDate.now().plusDays(5));

        assertThrows(IllegalOperationException.class, () -> returnPetService.createReturn(newReturn));
    }


    @Test
    void testGetReturns() {
        List<ReturnPetEntity> returns = returnPetService.getReturns();
        assertFalse(returns.isEmpty());
    }



    @Test
    void testGetReturnValid() throws EntityNotFoundException {
        ReturnPetEntity found = returnPetService.getReturn(returnPetEntity.getId());
        assertNotNull(found);
        assertEquals(returnPetEntity.getId(), found.getId());
    }

    @Test
    void testGetReturnNotFound() {
        assertThrows(EntityNotFoundException.class, () -> returnPetService.getReturn(0L));
    }


    @Test
    void testUpdateReturnValid() throws EntityNotFoundException, IllegalOperationException {
        ReturnPetEntity updated = new ReturnPetEntity();
        updated.setReason("Nueva razón de devolución");
        updated.setReturnDate(LocalDate.now().minusDays(2));

        ReturnPetEntity result = returnPetService.updateReturn(returnPetEntity.getId(), updated);

        assertEquals("Nueva razón de devolución", result.getReason());
    }


    @Test
    void testDeleteReturnValid() throws EntityNotFoundException {
        returnPetService.deleteReturn(returnPetEntity.getId());
        assertThrows(EntityNotFoundException.class, () -> returnPetService.getReturn(returnPetEntity.getId()));
    }
}//a