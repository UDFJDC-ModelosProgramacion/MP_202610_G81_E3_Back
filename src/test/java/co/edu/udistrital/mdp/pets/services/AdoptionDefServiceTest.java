package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.udistrital.mdp.pets.entities.AdoptionDefEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.repositories.AdoptionDefRepository;

@ExtendWith(MockitoExtension.class)
class AdoptionDefServiceTest {

    @Mock
    private AdoptionDefRepository adoptionDefRepository;

    @InjectMocks
    private AdoptionDefService adoptionDefService;

    
    // CREATE
    
    @Test
    void testCreateAdoptionDef() {
        AdoptionDefEntity entity = new AdoptionDefEntity();

        when(adoptionDefRepository.save(entity)).thenReturn(entity);

        AdoptionDefEntity result = adoptionDefService.createAdoptionDef(entity);

        assertNotNull(result);
        verify(adoptionDefRepository, times(1)).save(entity);
    }

    
    // GET BY ID SUCCESS
    
    @Test
    void testGetAdoptionDefSuccess() throws EntityNotFoundException {
        AdoptionDefEntity entity = new AdoptionDefEntity();

        when(adoptionDefRepository.findById(1L)).thenReturn(Optional.of(entity));

        AdoptionDefEntity result = adoptionDefService.getAdoptionDef(1L);

        assertNotNull(result);
        verify(adoptionDefRepository, times(1)).findById(1L);
    }

    
    // GET BY ID NOT FOUND
    
    @Test
    void testGetAdoptionDefNotFound() {
        when(adoptionDefRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            adoptionDefService.getAdoptionDef(1L);
        });

        verify(adoptionDefRepository, times(1)).findById(1L);
    }

    
    // GET ALL
    
    @Test
    void testGetAdoptionDefs() {
        List<AdoptionDefEntity> list = new ArrayList<>();

        when(adoptionDefRepository.findAll()).thenReturn(list);

        List<AdoptionDefEntity> result = adoptionDefService.getAdoptionDefs();

        assertNotNull(result);
        verify(adoptionDefRepository, times(1)).findAll();
    }

    
    // UPDATE
    
    @Test
    void testUpdateAdoptionDef() throws EntityNotFoundException {
        AdoptionDefEntity existing = new AdoptionDefEntity();
        AdoptionDefEntity updated = new AdoptionDefEntity();

        when(adoptionDefRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(adoptionDefRepository.save(existing)).thenReturn(existing);

        AdoptionDefEntity result = adoptionDefService.updateAdoptionDef(1L, updated);

        assertNotNull(result);
        verify(adoptionDefRepository, times(1)).findById(1L);
        verify(adoptionDefRepository, times(1)).save(existing);
    }

    
    // DELETE
    
    @Test
    void testDeleteAdoptionDef() throws EntityNotFoundException {
        AdoptionDefEntity entity = new AdoptionDefEntity();

        when(adoptionDefRepository.findById(1L)).thenReturn(Optional.of(entity));
        doNothing().when(adoptionDefRepository).delete(entity);

        adoptionDefService.deleteAdoptionDef(1L);

        verify(adoptionDefRepository, times(1)).findById(1L);
        verify(adoptionDefRepository, times(1)).delete(entity);
    }
}