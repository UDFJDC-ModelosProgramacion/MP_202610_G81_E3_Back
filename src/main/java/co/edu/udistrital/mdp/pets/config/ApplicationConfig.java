package co.edu.udistrital.mdp.pets.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationConfig {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**") // Aplica a todos los controladores (/auth, /adopters, /clients)
						.allowedOrigins("http://localhost:5173") // Origen explícito de tu Frontend
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Agregamos OPTIONS para el
																					// pre-vuelo del PUT
						.allowedHeaders("*") // Permite cualquier cabecera (Content-Type, etc.)
						.allowCredentials(true) // Permite el flujo de cookies o datos de sesión si se requieren
						.maxAge(3600);
			}
		};
	}
}