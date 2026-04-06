package co.edu.udistrital.mdp.pets.services;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.udistrital.mdp.pets.dto.AdopterDTO;
import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.repositories.AdopterRepository;

@SpringBootTest
@AutoConfigureMockMvc
class AdopterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AdopterRepository adopterRepository;

    private List<AdopterEntity> adopterList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        adopterRepository.deleteAll();
        adopterList.clear();

        for (int i = 0; i < 3; i++) {
            AdopterEntity adopter = new AdopterEntity();
            adopter.setHasChildren(i % 2 == 0);
            adopter.setHasPets(i % 2 != 0);
            adopterRepository.save(adopter);
            adopterList.add(adopter);
        }
    }

    @Test
    void testGetAllAdopters() throws Exception {
        mockMvc.perform(get("/adopters"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(adopterList.size())));
    }

    @Test
    void testGetAdopterById() throws Exception {
        AdopterEntity entity = adopterList.get(0);

        mockMvc.perform(get("/adopters/{id}", entity.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.hasChildren", is(entity.getHasChildren())))
            .andExpect(jsonPath("$.hasPets", is(entity.getHasPets())));
    }

    @Test
    void testCreateAdopter() throws Exception {
        AdopterDTO dto = new AdopterDTO();
        dto.setHasChildren(true);
        dto.setHasPets(false);

        mockMvc.perform(post("/adopters")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.hasChildren", is(true)))
            .andExpect(jsonPath("$.hasPets", is(false)));
    }

    @Test
    void testUpdateAdopter() throws Exception {
        AdopterEntity entity = adopterList.get(0);
        AdopterDTO dto = new AdopterDTO();
        dto.setHasChildren(!entity.getHasChildren());
        dto.setHasPets(!entity.getHasPets());

        mockMvc.perform(put("/adopters/{id}", entity.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.hasChildren", is(dto.getHasChildren())))
            .andExpect(jsonPath("$.hasPets", is(dto.getHasPets())));
    }

    @Test
    void testDeleteAdopter() throws Exception {
        AdopterEntity entity = adopterList.get(0);

        mockMvc.perform(delete("/adopters/{id}", entity.getId()))
            .andExpect(status().isNoContent());
    }
}