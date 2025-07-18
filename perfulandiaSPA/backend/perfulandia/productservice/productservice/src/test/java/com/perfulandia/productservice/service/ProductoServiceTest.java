package com.perfulandia.productservice.service;
import com.perfulandia.productservice.model.Producto;
import com.perfulandia.productservice.repository.ProductoRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

//Librerías para usar mockito
import org.mockito.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*; //Mocks Simular inserciones, datos de listas etc.

public class ProductoServiceTest {
    //Creando una instancia de Mocks=Simular para poder iyectarlas donde sea necesario
    @InjectMocks
    private ProductoService service;
    //Creando un mock del repositorio //objeto simulado
    @Mock
    private ProductoRepository repo;

    //Constructor para inicializar test antes de cada prueba
    public ProductoServiceTest(){
        //Abre e inializa los mocks anotados con @Mock y @InjectMocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("TEST SERVICE 1: ACTUALIZAR")
    void actualizaProducto(){
        Producto producto = new Producto(1L, "Perfume durazno",23000,4);
        Map<String,Object> campos = new HashMap<>();
       producto.setNombre("Perfume cereza");
       producto.setPrecio(1000);

        when(repo.findById(1L)).thenReturn(Optional.of(producto));
        //Llamar al metodo de servicio que sera probado
        Producto resultado =  service.actualizar(1,campos);

        //Verificacion
        assertEquals("Perfume cereza", producto.getNombre());
        verify(repo).save(any(Producto.class));
    }
}
