package com.perfulandia.carritoservice.service;
import com.perfulandia.carritoservice.model.Carrito;
import com.perfulandia.carritoservice.model.CarritoItem;
import com.perfulandia.carritoservice.repository.CarritoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

//Librerías para usar mockito
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*; //Mocks Simular inserciones, datos de listas etc.


public class CarritoServiceTest {
    //Creando una instancia de Mocks=Simular para poder iyectarlas donde sea necesario
    @InjectMocks
    private CarritoService service;

    //Creando un mock del repositorio //objeto simulado
    @Mock
    private CarritoRepository repo;

    //Constructor para inicializar test antes de cada prueba
    public CarritoServiceTest(){
        //Abre e inializa los mocks anotados con @Mock y @InjectMocks
        MockitoAnnotations.openMocks(this);
    }
    @Test
    @DisplayName("TEST SERVICE 1: OBTENER LISTA")
    void testFindAll(){
        //Simular la creación de un objeto de carrito
        Carrito carrito = new Carrito();
        carrito.setId(1l);
        //con su respectiva lista de items, la dejaremos vacia
        List<CarritoItem> items = new ArrayList<>();

        when(repo.findAll()).thenReturn(List.of(new Carrito(1L,items)));
        //Llamar al metodo de servicio que sera probado
        List<Carrito> resultado =  service.listar();
        //Verificacion
        assertEquals(1, resultado.size());
        //verificamos q se llamo 1 vez al metodo findAll() de parte del repositorio
        verify(repo, times(1)).findAll();
    }

}
