package co.edu.udistrital.mdp.pets.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.services.ShelterService;

// Refugios de prueba.

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private ShelterService shelterService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!shelterService.getShelters().isEmpty()) return;

        String[][] refugios = {
            { "Patitas Suaves", "Bogotá", "Calle 40 #20-10", "patitassuaves@email.com", "https://images.pexels.com/photos/1108099/pexels-photo-1108099.jpeg?w=400" },
            { "Huellitas", "Medellín", "Carrera 30 #45-22", "huellitas@email.com", "https://images.pexels.com/photos/45201/kitty-cat-kitten-pet-45201.jpeg?w=400" },
            { "Miau Guau", "Bogotá", "Calle 75 #65-94", "miauguau@email.com", "https://images.pexels.com/photos/1633522/pexels-photo-1633522.jpeg?w=400" },};

        for (String[] r : refugios) {
            ShelterEntity s = new ShelterEntity();
            s.setName(r[0]);
            s.setCity(r[1]);
            s.setAddress(r[2]);
            s.setEmail(r[3]);
            s.setImage(r[4]);
            try {
                shelterService.createShelter(s);
            } catch (Exception e) {
                System.out.println("No se pudo crear refugio " + r[0] + ": " + e.getMessage());
            }
        }

        System.out.println("Refugios de prueba cargados correctamente.");
    }
}