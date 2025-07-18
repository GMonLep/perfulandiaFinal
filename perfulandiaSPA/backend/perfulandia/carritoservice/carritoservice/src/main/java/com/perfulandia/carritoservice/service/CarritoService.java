package com.perfulandia.carritoservice.service;
import com.perfulandia.carritoservice.model.CarritoItem;
import com.perfulandia.carritoservice.model.ProductoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.perfulandia.carritoservice.model.Carrito;
import com.perfulandia.carritoservice.repository.CarritoRepository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//listar-guardar-buscar-eliminar-actualizar

@Service
public class CarritoService {
    //repositorio
    public final CarritoRepository repo;
    public CarritoService(CarritoRepository repo){
        this.repo=repo;
    }

    private final RestTemplate restTemplate = new RestTemplate();


    //listar todos los carritos
    public List<Carrito> listar() {return repo.findAll();}

    //guardar un carrito
    public Carrito guardar(List<ProductoDTO> productoDTOS) {
        Carrito carrito = new Carrito();
        List<CarritoItem> items = new ArrayList<>();
        for (ProductoDTO productoDTO : productoDTOS) {
            try {
                ProductoDTO producto = restTemplate.getForObject(
                        "http://localhost:8082/api/productos/producto/" + productoDTO.getProductoId(),
                        ProductoDTO.class
                );
                CarritoItem item = CarritoItem.builder()
                        .productoId(productoDTO.getProductoId())
                        .nombre(producto.getNombre())
                        .precio(producto.getPrecio())
                        .cantidad(productoDTO.getCantidad())
                        .carrito(carrito)
                        .build();
                item.calcularPrecioTotal();
                items.add(item);
            }catch (HttpClientErrorException.NotFound e) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El producto con esa ID no existe :((");
            }
        }
        carrito.setItems(items);
        return repo.save(carrito);
    }

    //buscar
    public Carrito buscarPorId(long id) {return repo.findById(id).orElse(null);}

    //eliminar
    public void eliminar(long id) {repo.deleteById(id);}

    //actualizar productos
    public Carrito actualizar(long id, List<CarritoItem> nuevosItems) {
        Carrito carrito = repo.findById(id).orElse(null);
        carrito.getItems().clear();

        for (CarritoItem itemNuevo : nuevosItems) {
            itemNuevo.setCarrito(carrito);
            itemNuevo.setId(itemNuevo.getId());
            itemNuevo.calcularPrecioTotal();
            carrito.getItems().add(itemNuevo);
        }
        return repo.save(carrito);
    }




}
