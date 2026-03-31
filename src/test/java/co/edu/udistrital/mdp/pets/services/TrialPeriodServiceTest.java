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

import co.edu.udistrital.mdp.pets.entities.TrialPeriodEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.repositories.TrialPeriodRepository;

@ExtendWith(MockitoExtension.class)
class TrialPeriodServiceTest {

    @Mock
    private TrialPeriodRepository trialPeriodRepository;

    @InjectMocks
    private TrialPeriodService trialPeriodService;

    // =========================
    // CREATE TEST
    // =========================
    @Test
    void testCreateTrial() {
        TrialPeriodEntity trial = new TrialPeriodEntity();

        when(trialPeriodRepository.save(trial)).thenReturn(trial);

        TrialPeriodEntity result = trialPeriodService.createTrial(trial);

        assertNotNull(result);
        verify(trialPeriodRepository, times(1)).save(trial);
    }

    // =========================
    // GET BY ID SUCCESS
    // =========================
    @Test
    void testGetTrialSuccess() throws EntityNotFoundException {
        TrialPeriodEntity trial = new TrialPeriodEntity();

        when(trialPeriodRepository.findById(1L)).thenReturn(Optional.of(trial));

        TrialPeriodEntity result = trialPeriodService.getTrial(1L);

        assertNotNull(result);
        verify(trialPeriodRepository, times(1)).findById(1L);
    }

    // =========================
    // GET BY ID NOT FOUND
    // =========================
    @Test
    void testGetTrialNotFound() {
        when(trialPeriodRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            trialPeriodService.getTrial(1L);
        });

        verify(trialPeriodRepository, times(1)).findById(1L);
    }

    // =========================
    // GET ALL
    // =========================
    @Test
    void testGetTrials() {
        List<TrialPeriodEntity> list = new ArrayList<>();

        when(trialPeriodRepository.findAll()).thenReturn(list);

        List<TrialPeriodEntity> result = trialPeriodService.getTrials();

        assertNotNull(result);
        verify(trialPeriodRepository, times(1)).findAll();
    }

    // =========================
    // UPDATE
    // =========================
    @Test
    void testUpdateTrial() throws EntityNotFoundException {
        TrialPeriodEntity existing = new TrialPeriodEntity();
        TrialPeriodEntity updated = new TrialPeriodEntity();

        when(trialPeriodRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(trialPeriodRepository.save(existing)).thenReturn(existing);

        TrialPeriodEntity result = trialPeriodService.updateTrial(1L, updated);

        assertNotNull(result);
        verify(trialPeriodRepository, times(1)).findById(1L);
        verify(trialPeriodRepository, times(1)).save(existing);
    }

    // =========================
    // DELETE
    // =========================
    @Test
    void testDeleteTrial() throws EntityNotFoundException {
        TrialPeriodEntity trial = new TrialPeriodEntity();

        when(trialPeriodRepository.findById(1L)).thenReturn(Optional.of(trial));
        doNothing().when(trialPeriodRepository).delete(trial);

        trialPeriodService.deleteTrial(1L);

        verify(trialPeriodRepository, times(1)).findById(1L);
        verify(trialPeriodRepository, times(1)).delete(trial);
    }
}