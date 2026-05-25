package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.controllers.AdoptionController;
import co.edu.udistrital.mdp.pets.dto.AdoptionDTO;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@WebMvcTest(AdoptionController.class)
class AdoptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdoptionService adoptionService;

    @MockBean
    private ModelMapper modelMapper;

    private AdoptionEntity adoptionEntity;
    private AdoptionDTO adoptionDTO;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        initObjects();
        initMocks();
    }
    //INICIALIZACIÓN
    private void initObjects() {

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // ENTITY
        adoptionEntity = new AdoptionEntity();
        adoptionEntity.setId(1L);
        adoptionEntity.setStatus(AdoptionStatus.IN_TRIAL);
        adoptionEntity.setAdoptionDate(LocalDate.now());
        adoptionEntity.setTrialStartDate(LocalDate.now());

        // DTO
        adoptionDTO = new AdoptionDTO();
        adoptionDTO.setStatus(adoptionEntity.getStatus());
        adoptionDTO.setAdoptionDate(adoptionEntity.getAdoptionDate());
        adoptionDTO.setTrialStartDate(adoptionEntity.getTrialStartDate());
        adoptionDTO.setTrialEndDate(adoptionEntity.getTrialEndDate());
        adoptionDTO.setAdopterId(1L);
        adoptionDTO.setPetId(1L);
    }

    private void initMocks() {

        // Entity → DTO
        when(modelMapper.map(any(AdoptionEntity.class), eq(AdoptionDTO.class)))
                .thenReturn(adoptionDTO);

        // DTO → Entity
        when(modelMapper.map(any(AdoptionDTO.class), eq(AdoptionEntity.class)))
                .thenReturn(adoptionEntity);

        //  LISTA
        when(modelMapper.map(anyList(), any(java.lang.reflect.Type.class)))
                .thenReturn(Arrays.asList(adoptionDTO));
    }

    //TESTS

    @Test
    void testFindAll() throws Exception {

        List<AdoptionEntity> list = Arrays.asList(adoptionEntity);
        when(adoptionService.getAdoptions()).thenReturn(list);

        mockMvc.perform(get("/adoptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("IN_TRIAL"));
    }

    @Test
    void testFindById() throws Exception {

        when(adoptionService.getAdoption(1L)).thenReturn(adoptionEntity);

        mockMvc.perform(get("/adoptions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_TRIAL"));
    }

    @Test
    void testCreate() throws Exception {

        when(adoptionService.createAdoption(anyLong(), anyLong(), any(AdoptionEntity.class)))
                .thenReturn(adoptionEntity);

        String body = objectMapper.writeValueAsString(adoptionDTO);

        mockMvc.perform(post("/adoptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("IN_TRIAL"));
    }

    @Test
    void testUpdate() throws Exception {

        adoptionEntity.setStatus(AdoptionStatus.COMPLETED);
        adoptionEntity.setTrialEndDate(LocalDate.now().plusDays(30));

        when(adoptionService.updateAdoption(eq(1L), any(AdoptionEntity.class)))
                .thenReturn(adoptionEntity);

        adoptionDTO.setStatus(AdoptionStatus.COMPLETED);
        adoptionDTO.setTrialEndDate(LocalDate.now().plusDays(30));

        String body = objectMapper.writeValueAsString(adoptionDTO);

        mockMvc.perform(put("/adoptions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void testDelete() throws Exception {

        doNothing().when(adoptionService).deleteAdoption(1L);

        mockMvc.perform(delete("/adoptions/1"))
                .andExpect(status().isNoContent());
    }
}