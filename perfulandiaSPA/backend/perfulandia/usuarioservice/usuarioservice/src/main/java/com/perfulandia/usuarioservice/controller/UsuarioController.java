package com.perfulandia.usuarioservice.controller;

import com.perfulandia.usuarioservice.model.Usuario;
import com.perfulandia.usuarioservice.repository.UsuarioRepository;
import com.perfulandia.usuarioservice.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.perfulandia.usuarioservice.model.Usuario;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    private final UsuarioService service;
    //Constructor para poder consumir la interfaz
    public UsuarioController(UsuarioService service){
        this.service=service;
    }

    @GetMapping
    public List<Usuario> listar(){
        return service.listar();
    }

    @PostMapping
    public ResponseEntity<Usuario> guardar(@RequestBody Usuario usuario) {
        Usuario guardado = service.guardar(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @GetMapping("/{id}")
    public Usuario buscar(@PathVariable long id){
        return service.buscar(id);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable long id){service.eliminar(id);
    }

    @PatchMapping("/{id}") //Este mérecibe un ID desde la URL y un cuerpo con los campos a actualizar (en formato JSON)
    public Usuario actualizar(@PathVariable Long id, @RequestBody Map<String, Object> campos){
        return service.actualizar(id,campos);
    }


}
