package co.edu.udistrital.mdp.pets.services;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.dto.ClientDTO;
import co.edu.udistrital.mdp.pets.entities.ClientEntity;
import co.edu.udistrital.mdp.pets.repositories.ClientRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ClientEntity client1;
    private ClientEntity client2;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();

        client1 = new ClientEntity();
        client1.setClientName("Juan Perez");
        client1.setClientEmail("juan@test.com");
        client1.setClientPhone("3001234567");
        clientRepository.save(client1);

        client2 = new ClientEntity();
        client2.setClientName("Maria Lopez");
        client2.setClientEmail("maria@test.com");
        client2.setClientPhone("3109876543");
        clientRepository.save(client2);
    }

    @Test
    void testGetAllClients() throws Exception {
        mockMvc.perform(get("/clients"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
               .andExpect(jsonPath("$[0].clientName", notNullValue()));
    }

    @Test
    void testGetClientById() throws Exception {
        mockMvc.perform(get("/clients/{id}", client1.getId()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.clientName").value("Juan Perez"));
    }

    @Test
    void testCreateClient() throws Exception {
        ClientDTO newClient = new ClientDTO();
        newClient.setClientName("Pedro Ruiz");
        newClient.setClientEmail("pedro@test.com");
        newClient.setClientPhone("3201112233");

        mockMvc.perform(post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newClient)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.clientName").value("Pedro Ruiz"))
               .andExpect(jsonPath("$.clientEmail").value("pedro@test.com"));
    }

    @Test
    void testUpdateClient() throws Exception {
        ClientDTO updateClient = new ClientDTO();
        updateClient.setClientName("Juan Actualizado");
        updateClient.setClientEmail("juanupdate@test.com");
        updateClient.setClientPhone("3110001111");

        mockMvc.perform(put("/clients/{id}", client1.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateClient)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.clientName").value("Juan Actualizado"))
               .andExpect(jsonPath("$.clientPhone").value("3110001111"));
    }

    @Test
    void testDeleteClient() throws Exception {
        mockMvc.perform(delete("/clients/{id}", client2.getId()))
               .andExpect(status().isNoContent());

        // Verifica que el cliente fue eliminado
        mockMvc.perform(get("/clients/{id}", client2.getId()))
               .andExpect(status().isNotFound());
    }
}