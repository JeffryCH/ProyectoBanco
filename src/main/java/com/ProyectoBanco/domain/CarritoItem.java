package com.ProyectoBanco.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "carrito_items")
public class CarritoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
    
    private int cantidad;
}
