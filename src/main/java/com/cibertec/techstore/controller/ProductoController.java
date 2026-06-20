package com.cibertec.techstore.controller;

import com.cibertec.techstore.model.Producto;
import com.cibertec.techstore.service.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*") // Permite que el Frontend se conecte sin bloqueos de CORS
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    // 1. Obtener todos los productos (Para el catálogo principal)
    @GetMapping
    public ResponseEntity<List<Producto>> listarTodos() {
        List<Producto> productos = productoService.listarTodos();
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    // 2. Obtener un solo producto por su ID (Para la vista de detalles)
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable("id") Integer id) {
        Producto producto = productoService.obtenerPorId(id);
        if (producto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(producto, HttpStatus.OK);
    }

    // 3. Filtrar productos por categoría
    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<Producto>> listarPorCategoria(@PathVariable("idCategoria") Integer idCategoria) {
        List<Producto> productos = productoService.listarPorCategoria(idCategoria);
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    // 4. Guardar o crear un nuevo producto (Por si agregas un panel de Admin)
    @PostMapping
    public ResponseEntity<Producto> guardar(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.guardar(producto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    // 5. Eliminar un producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) {
        Producto producto = productoService.obtenerPorId(id);
        if (producto == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productoService.eliminar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}