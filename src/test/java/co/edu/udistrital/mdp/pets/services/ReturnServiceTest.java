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
class ReturnServiceTest {

    @Autowired
    private ReturnPetService returnService;

    @Autowired
    private ReturnPetRepository returnRepository;

    private PodamFactory factory = new PodamFactoryImpl();
    private ReturnPetEntity returnEntity;

    @BeforeEach
    void setUp() throws IllegalOperationException {
        returnRepository.deleteAll();

        // Crear una devolución válida base para los tests
        returnPetEntity = factory.manufacturePojo(ReturnPetEntity.class);
        returnPetEntity.setId(null);
        returnPetEntity.setReason("Incompatibilidad con otros animales");
        returnPetEntity.setReturnDate(LocalDate.now().minusDays(1));
        returnPetEntity = returnService.createReturn(returnPetEntity);
    }

    // Tests: createReturn

    @Test
    void testCreateReturnValid() throws IllegalOperationException {
        ReturnPetEntity newReturn = new ReturnPetEntity();
        newReturn.setReason("Problemas de salud del adoptante");
        newReturn.setReturnDate(LocalDate.now());

        ReturnPetEntity result = returnService.createReturn(newReturn);

        assertNotNull(result.getId());
        assertEquals("Problemas de salud del adoptante", result.getReason());
    }

    @Test
    void testCreateReturnReasonNull() {
        ReturnPetEntity newReturn = new ReturnPetEntity();
        newReturn.setReason(null);
        newReturn.setReturnDate(LocalDate.now());

        assertThrows(IllegalOperationException.class, () -> returnService.createReturn(newReturn));
    }

    @Test
    void testCreateReturnReasonEmpty() {
        ReturnPetEntity newReturn = new ReturnPetEntity();
        newReturn.setReason("   ");
        newReturn.setReturnDate(LocalDate.now());

        assertThrows(IllegalOperationException.class, () -> returnService.createReturn(newReturn));
    }

    @Test
    void testCreateReturnDateInFuture() {
        ReturnPetEntity newReturn = new ReturnPetEntity();
        newReturn.setReason("Razón válida");
        newReturn.setReturnDate(LocalDate.now().plusDays(5));

        assertThrows(IllegalOperationException.class, () -> returnService.createReturn(newReturn));
    }

    // Tests: getReturns

    @Test
    void testGetReturns() {
        List<ReturnPetEntity> returns = returnService.getReturns();
        assertFalse(returns.isEmpty());
    }

    // Tests: getReturn

    @Test
    void testGetReturnValid() throws EntityNotFoundException {
        ReturnPetEntity found = returnService.getReturn(returnEntity.getId());
        assertNotNull(found);
        assertEquals(returnEntity.getId(), found.getId());
    }

    @Test
    void testGetReturnNotFound() {
        assertThrows(EntityNotFoundException.class, () -> returnService.getReturn(0L));
    }

    // Tests: updateReturn

    @Test
    void testUpdateReturnValid() throws EntityNotFoundException, IllegalOperationException {
        ReturnPetEntity updated = new ReturnPetEntity();
        updated.setReason("Nueva razón de devolución");
        updated.setReturnDate(LocalDate.now().minusDays(2));

        ReturnPetEntity result = returnService.updateReturn(returnEntity.getId(), updated);

        assertEquals("Nueva razón de devolución", result.getReason());
    }

    @Test
    void testUpdateReturnNotFound() {
        ReturnPetEntity updated = new ReturnPetEntity();
        updated.setReason("Razón válida");
        updated.setReturnDate(LocalDate.now());

        assertThrows(EntityNotFoundException.class, () -> returnService.updateReturn(0L, updated));
    }

    @Test
    void testUpdateReturnReasonNull() {
        ReturnPetEntity updated = new ReturnPetEntity();
        updated.setReason(null);
        updated.setReturnDate(LocalDate.now());

        assertThrows(IllegalOperationException.class,
                () -> returnService.updateReturn(returnEntity.getId(), updated));
    }

    @Test
    void testUpdateReturnDateInFuture() {
        ReturnPetEntity updated = new ReturnPetEntity();
        updated.setReason("Razón válida");
        updated.setReturnDate(LocalDate.now().plusDays(3));

        assertThrows(IllegalOperationException.class,
                () -> returnService.updateReturn(returnEntity.getId(), updated));
    }

    // Tests: deleteReturn

    @Test
    void testDeleteReturnValid() throws EntityNotFoundException {
        returnService.deleteReturn(returnEntity.getId());
        assertThrows(EntityNotFoundException.class, () -> returnService.getReturn(returnEntity.getId()));
    }

    @Test
    void testDeleteReturnNotFound() {
        assertThrows(EntityNotFoundException.class, () -> returnService.deleteReturn(0L));
    }
}