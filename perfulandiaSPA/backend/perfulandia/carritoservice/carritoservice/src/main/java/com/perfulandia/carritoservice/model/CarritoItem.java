package com.perfulandia.carritoservice.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class CarritoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long productoId;
    private String nombre;

    @Column(nullable = false)
    private double precio;
    private int cantidad;
    private double precioTotal;

    @ManyToOne
    @JoinColumn(name = "carrito_id")
    @JsonBackReference
    private Carrito carrito;

    //calcula el precio total multiplicando la cantidad del producto x el precio unitario
    public void calcularPrecioTotal() {
        this.precioTotal = this.precio * this.cantidad;
    }

}
