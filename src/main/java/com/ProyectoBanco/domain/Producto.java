package com.ProyectoBanco.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.*;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "productos")
public class Producto implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoProducto tipo;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "precio", nullable = false)
    private Double precio;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoProducto estado = EstadoProducto.Activo;

    @Column(name = "fecha_creacion", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion = new Date();

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;
}
