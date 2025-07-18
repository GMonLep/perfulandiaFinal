package com.perfulandia.productservice.config;
import com.perfulandia.productservice.repository.ProductoRepository;
import com.perfulandia.productservice.model.Producto;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Creacion {
    @Bean
    CommandLineRunner initDatabase(ProductoRepository repository) {
        return args -> {
            //iniciamos la cuenta vacia pq la base de datos va a estar vacia
            if (repository.count() == 0) {
                repository.save(Producto.builder()
                        .nombre("Rosado")
                        .precio(1000)
                        .stock(5)
                        .build());

                repository.save(Producto.builder()
                        .nombre("Azul")
                        .precio(2000)
                        .stock(2)
                        .build());

                repository.save(Producto.builder()
                        .nombre("Perfume Verde")
                        .precio(3000)
                        .stock(4)
                        .build());

                System.out.println("Productos creados");
            } else {
                System.out.println("Productos ya existen. No se agregaron");
            }
        };
}



}
