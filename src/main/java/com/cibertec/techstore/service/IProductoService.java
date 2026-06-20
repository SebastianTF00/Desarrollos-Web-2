package com.cibertec.techstore.service;

import com.cibertec.techstore.model.Producto;
import java.util.List;

public interface IProductoService {
    List<Producto> listarTodos();
    Producto obtenerPorId(Integer id);
    List<Producto> listarPorCategoria(Integer idCategoria);
    Producto guardar(Producto producto);
    void eliminar(Integer id);
}