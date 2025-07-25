package com.perfulandia.productservice.controller;
import com.perfulandia.productservice.model.ProductoDTO;
import com.perfulandia.productservice.model.Usuario;
import com.perfulandia.productservice.model.Producto;
import com.perfulandia.productservice.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

//Nuevas importaciones DTO conexión al MS usuario
import org.springframework.web.client.RestTemplate;
//Para hacer peticiones HTTP a otros microservicios.


@RestController
@RequestMapping("/api/productos")
@CrossOrigin("*")
public class ProductoController {



    private final ProductoService servicio;
    private final RestTemplate restTemplate;
    public ProductoController(ProductoService servicio,  RestTemplate restTemplate){
        this.servicio = servicio;
        this.restTemplate = restTemplate;
    }


    //listar
    @GetMapping
    public List<Producto> listar(){
        return servicio.listar();
    }
    //guardar
    @PostMapping
    public Producto guardar(@RequestBody Producto producto){
        return servicio.guardar(producto);
    }
    //buscar x id
    @GetMapping("/buscar/{id}")
    public Producto buscar(@PathVariable long id){
        return servicio.bucarPorId(id);
    }

    //Eliminar
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable long id){
        servicio.eliminar(id);
    }

    //Nuevo método
    @GetMapping("/usuario/{id}")
    public Usuario obtenerUsuario(@PathVariable long id){
        return restTemplate.getForObject("http://localhost:8081/api/usuarios/"+id,Usuario.class);
    }
    @PatchMapping("/{id}")
    public Producto actualizar(@PathVariable Long id, @RequestBody Map<String, Object> campos){
        return servicio.actualizar(id,campos);
    }

    //get pal dto
    @GetMapping("/producto/{id}")
    public ResponseEntity<ProductoDTO> getProducto(@PathVariable Long id) {
        ProductoDTO producto = servicio.getProductoDTOById(id);
        if (producto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(producto);
    }
}
