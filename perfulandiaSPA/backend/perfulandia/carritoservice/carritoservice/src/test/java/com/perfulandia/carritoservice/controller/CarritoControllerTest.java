package com.perfulandia.carritoservice.controller;
import com.perfulandia.carritoservice.model.Carrito;
import com.perfulandia.carritoservice.model.CarritoItem;
import com.perfulandia.carritoservice.model.ProductoDTO;
import com.perfulandia.carritoservice.service.CarritoService;

//1 Importaciones de JUnit 5
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

//2 Anotación para probar solo el controlador (no el contexto completo de Spring Boot)
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

//3 Anotación para simular un bean dentro del ApplicationContext de Spring
import org.springframework.test.context.bean.override.mockito.MockitoBean;

//4 Inyección automática del cliente de pruebas web
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

//5 Métodos estáticos de Mockito
import static org.mockito.Mockito.*;

//6 Métodos para construir peticiones HTTP simuladas y verificar respuestas
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//7 Librería para convertir objetos a JSON (necesaria en peticiones POST)
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Anotación que indica que solo probará el carritoController
@WebMvcTest(CarritoController.class)
public class CarritoControllerTest {

    //Cliente hhtp para poder realizar pruebas unitarias injectar
    @Autowired
    private MockMvc mockMvc;

    //Simulación  del servicio para evitar acceder a datos reales
    @MockitoBean
    private CarritoService servicio;

    //convertimos texto a JSON y viceversa
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    @DisplayName("TEST CONTROLLER 1 - GET LISTA")
    void testGetAll() throws Exception {
        //creamos carrito con id
        Carrito carrito = new Carrito();
        carrito.setId(1l);
        //inicializamos la lista, la dejamos vacia nms
        List<CarritoItem> items = new ArrayList<>();

        when(servicio.listar()).thenReturn(List.of(new Carrito(1L,items)));
        mockMvc.perform(get("/api/carritos"))
                //3 lo que esperamos en esa petición
                .andExpect(status().isOk())//código 200
                //4 verificamos que el primer elemento JSON de la lista de items sea PERFUME BACAN
                .andExpect(jsonPath("[0].id").value(1l));
    }


    @Test
    @DisplayName("TEST CONTROLLER 2 - POST")
    void testPost() throws Exception {
        //inventamos el producto q va a buscar, aka la info q el cliente "busca"
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setProductoId(1234L);
        productoDTO.setCantidad(2);

        //inventamos la lista d este producto
        List<ProductoDTO> productoDTOs = List.of(productoDTO);

        //creamos carrito cn su id
        Carrito carrito = new Carrito();
        carrito.setId(1L);

        //inventamos el primer item(y unico) de la lista
        CarritoItem item1 = new CarritoItem();
        item1.setId(1L);
        item1.setProductoId(1234L);
        item1.setNombre("PERFUME BACAN");
        item1.setPrecio(1000);
        item1.setCantidad(2);
        item1.setPrecioTotal(2000);
        item1.setCarrito(carrito);
        //lo guardamos en el atributo lista del carritou
        carrito.setItems(List.of(item1));

        //cuando se guarda un carrito le devolvemos el carrito q acabamos d crear
        when(servicio.guardar(any())).thenReturn(carrito);
        mockMvc.perform(post("/api/carritos")
                .contentType("application/json")
                //y le tiramos la lista d producto falso
                .content(mapper.writeValueAsString(productoDTOs)))
                .andExpect(status().isOk())
                //4 verificamos que el primer elemento JSON de la lista de items sea PERFUME BACAN
                .andExpect(jsonPath("$.items[0].nombre").value("PERFUME BACAN"));
    }

    @Test
    @DisplayName("TEST CONTROLLER 3 - DELETE")
    void testDelete() throws Exception {
        doNothing().when(servicio).eliminar(1l);
        mockMvc.perform(delete("/api/carritos/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("TEST CONTROLLER 4 - PUT")
    void testPut() throws Exception {
       List<CarritoItem> items = new ArrayList<>();

        Carrito actualizado = new Carrito(1L,items);

        when(servicio.actualizar(1L,items)).thenReturn(actualizado);
        mockMvc.perform(put("/api/carritos/1")
                       .contentType("application/json")
                       .content(mapper.writeValueAsString(items)))
                .andExpect(status().isOk());
    }
}
