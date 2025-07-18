package com.perfulandia.usuarioservice.controller;
import com.perfulandia.usuarioservice.model.Usuario;
import com.perfulandia.usuarioservice.service.UsuarioService;


//importamos junit para testeo jiji
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

//esta es para q solo tome el controlador para pruebas
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

//simulacion de bean
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

//Inyección automática del cliente de pruebas web wtm
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

//metodos estaticos de mockito
import static org.mockito.Mockito.*;

//6 Métodos para construir peticiones HTTP simuladas y verificar respuestas
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;

//7 Librería para convertir objetos a JSON (necesaria en peticiones POST)
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.DeleteMapping;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebMvcTest(UsuarioController.class)

public class UsuarioControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService service;

    //Convertir de texto a JSON y viceversa
    private final ObjectMapper mapper = new ObjectMapper();


    @Test
    @DisplayName("TEST 1: GET LISTA")
    void testGetAll() throws Exception{
        when(service.listar()).thenReturn(List.of(new Usuario(1L, "Juanito","juanit@gmail.com","Administrador")));
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juanito"));
    }

    @Test
    @DisplayName("TEST 2 GET ID")
    void getById() throws Exception{
        Usuario user = new Usuario(1L, "Juanito","juanit@gmail.com","Administrador");
        when(service.buscar(1L)).thenReturn(user);
        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juanito"));
    }

    @Test
    @DisplayName("TEST 3: POST")
    void testPost() throws Exception{
        Usuario user = new Usuario(1L, "Juanito","juanit@gmail.com","Administrador");
        when(service.guardar(any())).thenReturn(user);

        mockMvc.perform(post("/api/usuarios")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juanito"));
    }

    @Test
    @DisplayName("TESTING 4 PATCH")
    void testPatch() throws Exception{
        Usuario actualizado = new Usuario(1L, "Pedro", "pedro@gmail.com", "Administrador");
        Map<String,Object> campos = new HashMap<>();
        campos.put("nombre", "Pedro");
        campos.put("correo", "pedro@gmail.com");

        when(service.actualizar(1L,campos)).thenReturn(actualizado);
        mockMvc.perform(patch("/api/usuarios/1")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(campos)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Pedro"))
                .andExpect(jsonPath("$.correo").value("pedro@gmail.com"));
    }

    @Test
    @DisplayName("TEST 5 DELETE")
    void testDelete() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isOk());

        verify(service).eliminar(1);
    }
}
