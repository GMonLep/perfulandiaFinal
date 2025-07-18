package com.perfulandia.carritoservice.controller;
import com.perfulandia.carritoservice.model.Carrito;
import com.perfulandia.carritoservice.model.CarritoItem;
import com.perfulandia.carritoservice.model.ProductoDTO;
import com.perfulandia.carritoservice.service.CarritoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/carritos")
@CrossOrigin("*")
public class CarritoController {
    private final CarritoService servicio;
    private final RestTemplate restTemplate;

    public CarritoController(CarritoService servicio, RestTemplate restTemplate) {
        this.servicio = servicio;
        this.restTemplate = restTemplate;
    }

    //Listar los carritous
    @GetMapping
    public List<Carrito> getAll(){
        return servicio.listar();
    }

    @GetMapping("/{id}")
    public Carrito getById(@PathVariable int id){return servicio.buscarPorId(id);}

    //Guardar Carrito
    @PostMapping
    public Carrito guardarCarrito(@RequestBody List<ProductoDTO> items) {return servicio.guardar(items);}

    //Eliminar
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable long id){
        servicio.eliminar(id);
    }

    //Actualizar carritou
    @PutMapping("/{id}")
    public Carrito actualizarCarrito(@PathVariable int id, @RequestBody List<CarritoItem> items) {return servicio.actualizar(id,items);}

}