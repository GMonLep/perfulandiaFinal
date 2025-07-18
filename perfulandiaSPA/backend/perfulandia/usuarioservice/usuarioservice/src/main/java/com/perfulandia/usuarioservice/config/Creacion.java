package com.perfulandia.usuarioservice.config;
import com.perfulandia.usuarioservice.repository.UsuarioRepository;
import com.perfulandia.usuarioservice.model.Usuario;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Creacion {
    @Bean
    CommandLineRunner initDatabase(UsuarioRepository repository) {
        return args -> {
            //iniciamos la cuenta vacia pq la base de datos va a estar vacia
            if (repository.count() == 0) {
                repository.save(Usuario.builder()
                        .nombre("Rosario Santanaz")
                        .correo("rosari@gmail.com")
                        .rol("Cajera")
                        .build());

                repository.save(Usuario.builder()
                        .nombre("Hernesto Hernandez")
                        .correo("gernestiti@gmail.com")
                        .rol("Administrador")
                        .build());

                repository.save(Usuario.builder()
                        .nombre("Perdo Gonzalez")
                        .correo("pedrito@gmail.com")
                        .rol("Gerente")
                        .build());

                System.out.println("Usuarios creados");
            } else {
                System.out.println("Usuarios ya existen. No se agregaron");
            }
        };
    }
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
