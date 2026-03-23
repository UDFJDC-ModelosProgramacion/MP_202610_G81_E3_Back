package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.pets.entities.ReturnEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.ReturnRepository;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Import(ReturnService.class)
public class ReturnServiceTest {

    @Autowired
    private ReturnService returnService;

    @Autowired
    private ReturnRepository returnRepository;

    private PodamFactory factory = new PodamFactoryImpl();
    private ReturnEntity returnEntity;

    @BeforeEach
    void setUp() throws IllegalOperationException {
        returnRepository.deleteAll();

        // Crear una devolución válida base para los tests
        returnEntity = factory.manufacturePojo(ReturnEntity.class);
        returnEntity.setId(null);
        returnEntity.setReason("Incompatibilidad con otros animales");
        returnEntity.setReturnDate(LocalDate.now().minusDays(1));
        returnEntity = returnService.createReturn(returnEntity);
    }

    // Tests: createReturn

    @Test
    void testCreateReturnValid() throws IllegalOperationException {
        ReturnEntity newReturn = new ReturnEntity();
        newReturn.setReason("Problemas de salud del adoptante");
        newReturn.setReturnDate(LocalDate.now());

        ReturnEntity result = returnService.createReturn(newReturn);

        assertNotNull(result.getId());
        assertEquals("Problemas de salud del adoptante", result.getReason());
    }

    @Test
    void testCreateReturnReasonNull() {
        ReturnEntity newReturn = new ReturnEntity();
        newReturn.setReason(null);
        newReturn.setReturnDate(LocalDate.now());

        assertThrows(IllegalOperationException.class, () -> returnService.createReturn(newReturn));
    }

    @Test
    void testCreateReturnReasonEmpty() {
        ReturnEntity newReturn = new ReturnEntity();
        newReturn.setReason("   ");
        newReturn.setReturnDate(LocalDate.now());

        assertThrows(IllegalOperationException.class, () -> returnService.createReturn(newReturn));
    }

    @Test
    void testCreateReturnDateInFuture() {
        ReturnEntity newReturn = new ReturnEntity();
        newReturn.setReason("Razón válida");
        newReturn.setReturnDate(LocalDate.now().plusDays(5));

        assertThrows(IllegalOperationException.class, () -> returnService.createReturn(newReturn));
    }

    // Tests: getReturns

    @Test
    void testGetReturns() {
        List<ReturnEntity> returns = returnService.getReturns();
        assertFalse(returns.isEmpty());
    }

    // Tests: getReturn

    @Test
    void testGetReturnValid() throws EntityNotFoundException {
        ReturnEntity found = returnService.getReturn(returnEntity.getId());
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
        ReturnEntity updated = new ReturnEntity();
        updated.setReason("Nueva razón de devolución");
        updated.setReturnDate(LocalDate.now().minusDays(2));

        ReturnEntity result = returnService.updateReturn(returnEntity.getId(), updated);

        assertEquals("Nueva razón de devolución", result.getReason());
    }

    @Test
    void testUpdateReturnNotFound() {
        ReturnEntity updated = new ReturnEntity();
        updated.setReason("Razón válida");
        updated.setReturnDate(LocalDate.now());

        assertThrows(EntityNotFoundException.class, () -> returnService.updateReturn(0L, updated));
    }

    @Test
    void testUpdateReturnReasonNull() {
        ReturnEntity updated = new ReturnEntity();
        updated.setReason(null);
        updated.setReturnDate(LocalDate.now());

        assertThrows(IllegalOperationException.class,
                () -> returnService.updateReturn(returnEntity.getId(), updated));
    }

    @Test
    void testUpdateReturnDateInFuture() {
        ReturnEntity updated = new ReturnEntity();
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