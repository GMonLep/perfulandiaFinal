package com.perfulandia.productservice.model;

//dto para carrito

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductoDTO {
    private long productoId;
    private String nombre;
    private int cantidad;
    private double precio;


}
