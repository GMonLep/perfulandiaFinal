package com.perfulandia.productservice.controller;
import com.perfulandia.productservice.model.Producto;
import com.perfulandia.productservice.service.ProductoService;

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

@WebMvcTest(ProductoController.class)

public class ProductoControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService service;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("TEST 1: GET LISTA")
    void testGetAll() throws Exception{
        when(service.listar()).thenReturn(List.of(new Producto(1L, "Perfume bacanono",100000,9)));
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Perfume bacanono"));
    }

    @Test
    @DisplayName("TEST 2 GET ID")
    void getById() throws Exception{
        Producto producto = new Producto(1L, "Perfume bacanono",100000,9);
        when(service.bucarPorId(1L)).thenReturn(producto);
        mockMvc.perform(get("/api/productos/buscar/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Perfume bacanono"));
    }

    @Test
    @DisplayName("TEST 3: POST")
    void testPost() throws Exception{
        Producto producto = new Producto(1L, "Perfume bacanono",100000,9);
        when(service.guardar(any())).thenReturn(producto);

        mockMvc.perform(post("/api/productos")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(producto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Perfume bacanono"));
    }

    @Test
    @DisplayName("TEST: 4 PATCH")
    void testPatch() throws Exception{
        Producto actualizado = new Producto(1L, "Perfume no-bacanono",100000,25);
        Map<String,Object> campos = new HashMap<>();
        campos.put("nombre", "Perfume no-bacanono");
        campos.put("stock", 25);

        when(service.actualizar(1L,campos)).thenReturn(actualizado);
        mockMvc.perform(patch("/api/productos/1")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(campos)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Perfume no-bacanono"))
                .andExpect(jsonPath("$.stock").value(25));
    }

    @Test
    @DisplayName("TEST 5 DELETE")
    void testDelete() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isOk());

        verify(service).eliminar(1);
    }

}
