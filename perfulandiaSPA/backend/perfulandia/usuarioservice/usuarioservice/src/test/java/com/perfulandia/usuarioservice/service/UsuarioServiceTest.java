package com.perfulandia.usuarioservice.service;
import com.perfulandia.usuarioservice.model.Usuario;
import com.perfulandia.usuarioservice.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

//Librerías para usar mockito
import org.mockito.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*; //Mocks Simular inserciones, datos de listas etc.

public class UsuarioServiceTest {
    @InjectMocks
    private UsuarioService service;
    //Creando un mock del repositorio //objeto simulado
    @Mock
    private UsuarioRepository repo;

    //Constructor para inicializar test antes de cada prueba
    public UsuarioServiceTest(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    @DisplayName("TEST SERVICE 1: deleteById")
    void testDeleteById(){
        //llamamos al servicio a probar
        service.eliminar(1L);
        //verificamos llamado al repositorio
        verify(repo, times(1)).deleteById(1L);
    }
}
