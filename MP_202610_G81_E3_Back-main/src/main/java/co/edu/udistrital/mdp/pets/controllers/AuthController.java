package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.entities.ClientEntity;
import co.edu.udistrital.mdp.pets.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173") // Conexión limpia con tu Vite del Front
public class AuthController {

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        String role = credentials.get("role"); // "USER" o "COMPANY" de la pestaña del Front

        // 1. Buscar al cliente por email
        Optional<ClientEntity> clientOpt = clientRepository.findByClientEmail(email);

        if (clientOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "El correo electrónico no está registrado"));
        }

        ClientEntity client = clientOpt.get();

        // 2. Validar la contraseña que acabamos de agregar a la entidad
        if (client.getPassword() == null || !client.getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Contraseña incorrecta"));
        }

        // 3. Respuesta exitosa para React
        return ResponseEntity.ok(Map.of(
                "message", "¡Bienvenido de nuevo, " + client.getClientName() + "!",
                "clientId", client.getId(),
                "clientName", client.getClientName(),
                "role", role));
    }
}