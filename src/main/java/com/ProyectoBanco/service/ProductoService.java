package com.ProyectoBanco.service;

import com.ProyectoBanco.repository.*;
import com.ProyectoBanco.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

/**
 * Servicio para la gestión de productos.
 */
@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    /**
     * Obtiene todos los productos.
     * 
     * @return Lista de productos.
     */
    public List<Producto> obtenerTodosServicios() {
        return productoRepository.findAll();
    }

    /**
     * Obtiene un producto por su ID.
     * 
     * @param id ID del producto.
     * @return Producto encontrado o null si no existe.
     */
    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }
}
