package com.ProyectoBanco.service;

import com.ProyectoBanco.domain.CarritoItem;
import com.ProyectoBanco.domain.Cliente;
import com.ProyectoBanco.domain.Producto;
import com.ProyectoBanco.repository.CarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CarritoService {
    @Autowired
    private CarritoRepository carritoRepository;

    public void agregarAlCarrito(Cliente cliente, Producto producto, int cantidad) {
        CarritoItem item = new CarritoItem();
        item.setCliente(cliente);
        item.setProducto(producto);
        item.setCantidad(cantidad);
        carritoRepository.save(item);
    }

    public List<CarritoItem> obtenerCarrito(Cliente cliente) {
        return carritoRepository.findByCliente(cliente);
    }

    public int obtenerCantidadItems(Cliente cliente) {
        return carritoRepository.countByCliente(cliente);
    }

    public void eliminarItem(Long itemId) {
        carritoRepository.deleteById(itemId);
    }

    public void vaciarCarrito(Cliente cliente) {
        List<CarritoItem> items = carritoRepository.findByCliente(cliente);
        carritoRepository.deleteAll(items);
    }
}
